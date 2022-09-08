package zti.restaurantmatcher.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Aspect
@Component
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private HttpServletResponse response; //I can use it!

    @Pointcut("within(zti.restaurantmatcher..*)" +
            "&& @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void logMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Class<?>[] parameters = signature.getParameterTypes();
        try {
            logger.info("INFO BEFORE- path: {}, method: {}, arguments: {} ",
                    mapper.writeValueAsString(request.getRequestURL()),
                    request.getMethod(),
                    mapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            logger.error("Error", e);
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, Object entity) {
        try {
            logger.info("INFO AFTER - path: {}, method: {}, retuning: {}",
                    mapper.writeValueAsString(request.getRequestURL()),
                    request.getMethod(),
                    mapper.writeValueAsString(response.getStatus()));
        } catch (JsonProcessingException e) {
            logger.error("Error", e);
        }
    }
}
