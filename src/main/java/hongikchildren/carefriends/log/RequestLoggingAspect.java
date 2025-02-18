package hongikchildren.carefriends.log;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RequestLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

    // 모든 컨트롤러 메서드 실행 전에 로그 출력
    @Before("execution(* hongikchildren.carefriends.api..*(..))") // 패키지명 수정
    public void logBefore(JoinPoint joinPoint) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        logger.info("---- HTTP Request ----");
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("HTTP Method: {}", request.getMethod());

        logger.info("Calling: {}", joinPoint.getSignature().toShortString());
    }

    // 컨트롤러 실행 후 로그 출력
    @AfterReturning(pointcut = "execution(* hongikchildren.carefriends.api..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("---- HTTP Response ----");
        logger.info("Completed: {}", joinPoint.getSignature().toShortString());
        logger.info("Response: {}", result);
    }

    // 예외 발생 시 로그 출력
    @AfterThrowing(pointcut = "execution(* hongikchildren.carefriends.api..*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {}", joinPoint.getSignature().toShortString(), exception);
    }

    // 실행 시간 측정 (옵션)
    @Around("execution(* hongikchildren.carefriends.api..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        logger.info("{} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
        return proceed;
    }
}

