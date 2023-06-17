package securityproject.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import securityproject.model.enums.LogType;
import securityproject.model.logs.BlacklistedIpLog;
import securityproject.model.logs.UserRequestLog;
import securityproject.model.logs.UserResponseLog;
import securityproject.repository.mongo.BlacklistedIpLogRepository;
import securityproject.repository.mongo.UserRequestLogRepository;
import securityproject.repository.mongo.UserResponseLogRepository;
import securityproject.service.AlarmService;
import securityproject.service.RequestService;
import securityproject.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final TokenUtils tokenUtils = new TokenUtils();
    String format = "yyyy-MM-dd'T'HH:mm:ss.nn";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    UserRequestLogRepository requestLogRepository;
    @Autowired
    UserResponseLogRepository responseLogRepository;
    @Autowired
    BlacklistedIpLogRepository blacklistedIpLogRepository;
    @Autowired
    AlarmService alarmService;

    private final List<String> blacklistedAddresses = new ArrayList<>() {{
        add("1.0.136.29");
        add("1.0.136.215");
        add("1.0.156.75");
        add("52.179.4.12");
        add("52.179.5.76");
        add("103.15.245.145");
        add("103.16.61.130");
        add("103.16.71.13");
        add("150.136.149.141");
        add("170.147.251.215");
        add("197.255.255.68");
//        add("0:0:0:0:0:0:0:1");
    }};



    public String index() {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request URL: " + request.getRequestURL() + "; Request params " + getParameters(request) + "; IP: " + request.getRemoteAddr());
        alarmService.parseAnyRequest(request);
        String remoteAddr = request.getRemoteAddr();
        if (blacklistedAddresses.contains(remoteAddr)) {
            BlacklistedIpLog log = new BlacklistedIpLog(remoteAddr);
            blacklistedIpLogRepository.insert(log);
            // TODO: DODAJ ALARM
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "IP address is blacklisted");
            return false;}
        // device request
        if (request.getRequestURL().toString().startsWith("https://localhost:8081/device")) return true;

        // user request
        UserRequestLog userRequestLog = new UserRequestLog(request, tokenUtils, LogType.INFO);
        requestLogRepository.insert(userRequestLog);

        return true; // Proceed with the request
    }
    private String getParameters(HttpServletRequest request) {
        String params = "";
        for (String key: request.getParameterMap().keySet()) {
            params = params.concat(key + "=" + Arrays.toString(request.getParameterMap().get(key)) + "; ");
        }
        return params;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)  {
        // This method is called after the request is processed by the controller
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  {
        // ignore response to device requests
        if (request.getRequestURL().toString().startsWith("https://localhost:8081/device")) return;
        // This method is called after the response is sent to the client
        UserResponseLog userRequestLog = null;
        switch (response.getStatus()) {
            case 200: userRequestLog = new UserResponseLog(request, tokenUtils, LogType.INFO); userRequestLog.setResponseStatus("OK"); break;
            case 400: userRequestLog = new UserResponseLog(request, tokenUtils, LogType.WARN); userRequestLog.setResponseStatus("BAD REQUEST"); break;
            case 401: userRequestLog = new UserResponseLog(request, tokenUtils, LogType.WARN); userRequestLog.setResponseStatus("UNAUTHORIZED"); break;
            case 403: userRequestLog = new UserResponseLog(request, tokenUtils, LogType.WARN); userRequestLog.setResponseStatus("FORBIDDEN"); break;
            case 405: userRequestLog = new UserResponseLog(request, tokenUtils, LogType.WARN); userRequestLog.setResponseStatus("NOT ALLOWED"); break;
            case 415: userRequestLog = new UserResponseLog(request, tokenUtils, LogType.WARN); userRequestLog.setResponseStatus("UNSUPPORTED MEDIA TYPE"); break;
            case 500: userRequestLog = new UserResponseLog(request, tokenUtils, LogType.ERROR); userRequestLog.setResponseStatus("INTERNAL SERVER ERROR"); break;
            default: break;
        }
        if (null != userRequestLog) {
            responseLogRepository.insert(userRequestLog);
        }
    }
}