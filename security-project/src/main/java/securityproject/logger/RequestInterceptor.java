package securityproject.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import securityproject.repository.mongo.LogRepository;
import securityproject.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final TokenUtils tokenUtils = new TokenUtils();
    String format = "yyyy-MM-dd'T'HH:mm:ss.nn";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    LogRepository logRepository;


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
        // Log the request details
        logger.info("Request URL: {}", request.getRequestURL());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request Parameters: {}", getParameters(request));
        Log log = new Log(request, tokenUtils, LogType.INFO);
        logRepository.insert(log);

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
        // This method is called after the response is sent to the client
    }
}