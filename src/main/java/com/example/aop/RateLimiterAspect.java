package com.example.aop;

import com.example.ann.RateLimiter;
import com.example.config.redis.RedisUtils;
import com.example.exception.RateLimiterException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName RateLimiterAspect
 * @Author yupanpan
 * @Date 2020/5/6 13:46
 */
@Aspect
@Component
public class RateLimiterAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ThreadLocal<String> ipThreadLocal = new ThreadLocal();

    private DefaultRedisScript<Number> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<Number>();
        redisScript.setResultType(Number.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/req_ratelimit.lua")));
    }

    @Around("@annotation(com.example.ann.RateLimiter)")
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Class<?> targetClass = method.getDeclaringClass();
            RateLimiter rateLimit = method.getAnnotation(RateLimiter.class);

            if (rateLimit != null) {
                HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                boolean restrictionsIp = rateLimit.restrictionsIp();
                if (restrictionsIp) {
                    ipThreadLocal.set(getIpAddr(request));
                }

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(rateLimit.key());
                if (StringUtils.isNotBlank(ipThreadLocal.get())) {
                    stringBuffer.append(ipThreadLocal.get()).append("-");
                }
                stringBuffer.append("-").append(targetClass.getName()).append("- ").append(method.getName());

                List<String> keys = Collections.singletonList(stringBuffer.toString());

                Number number = RedisUtils.execute(redisScript, keys, rateLimit.count(), rateLimit.time());

                if (number != null && number.intValue() != 0 && number.intValue() <= rateLimit.count()) {
                    logger.info("限流时间段内访问第：{} 次", number.toString());
                    return joinPoint.proceed();
                } else {
                    logger.error("，最大访问次数:{}，已经到设置限流次数,当前次数:{}次", rateLimit.count(), number.toString());
                    //如果恶意访问刷接口，做相应封禁IP的操作
                    throw new RateLimiterException("服务器繁忙,请稍后再试");
                }
            } else {
                return joinPoint.proceed();
            }
        } finally {
            ipThreadLocal.remove();
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // "***.***.***.***".length()= 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}
