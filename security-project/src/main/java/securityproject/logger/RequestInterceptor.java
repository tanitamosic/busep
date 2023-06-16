package securityproject.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import securityproject.logger.logs.LogType;
import securityproject.logger.logs.UserRequestLog;
import securityproject.logger.logs.UserResponseLog;
import securityproject.repository.mongo.UserRequestLogRepository;
import securityproject.repository.mongo.UserResponseLogRepository;
import securityproject.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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


    public String index() {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        logger.info("Request URL: {}", request.getRequestURL());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request Parameters: {}", getParameters(request));

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