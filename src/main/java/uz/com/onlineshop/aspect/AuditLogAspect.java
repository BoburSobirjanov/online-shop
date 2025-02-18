package uz.com.onlineshop.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.model.entity.log.AuditLogsEntity;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.repository.AuditLogsRepository;
import uz.com.onlineshop.repository.UserRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogsRepository auditLogsRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        ip = getStringIp(ip, request);

        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        if (requestURI.startsWith("/swagger") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/api/v1/auth")) {
            return joinPoint.proceed();
        }

        if (httpMethod.startsWith("GET")) {
            return joinPoint.proceed();
        }

        UserEntity userEntity = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserEntity) {
                userEntity = (UserEntity) principal;
            } else if (principal instanceof String username) {
                userEntity = userRepository.findUserEntityByEmail(username);
            }

            if (userEntity == null) {
                throw new DataNotFoundException("User not found!");
            }
        }
        String requestData;
        try {
            requestData = objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (Exception e) {
            requestData = "Error converting request args to JSON: " + e.getMessage();
        }

        Object result = joinPoint.proceed();

        String responseData;
        try {
            responseData = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            responseData = "Error converting response to JSON: " + e.getMessage();
        }
        AuditLogsEntity auditLog = new AuditLogsEntity();
        auditLog.setUrl(requestURI);
        auditLog.setHttpMethod(httpMethod);
        auditLog.setUser(userEntity);
        auditLog.setRequest(requestData);
        auditLog.setResponse(responseData);
        auditLog.setFromIpAddress(ip);
        auditLogsRepository.save(auditLog);

        return result;
    }

    private static String getStringIp(String ip, HttpServletRequest request) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

}
