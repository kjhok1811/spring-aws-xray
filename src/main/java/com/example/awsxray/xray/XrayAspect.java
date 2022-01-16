package com.example.awsxray.xray;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class XrayAspect {
    private static final String DELIMITER = " || ";
    private static final String TARGET_CONTROLLER = "execution(* com.example.awsxray.controller..*Controller.*(..))";
    private static final String TARGET_SERVICE = "execution(* com.example.awsxray.service..*Service.*(..))";
    private static final String TARGET_REPOSITORY = "execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))";
    private static final String TARGET_ADVICE = TARGET_CONTROLLER + DELIMITER + TARGET_SERVICE + DELIMITER + TARGET_REPOSITORY;

    @Pointcut(value = TARGET_ADVICE)
    public void xrayTargetClass() {

    }

    @Around(value = "xrayTargetClass()")
    public Object processTargetXrayTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String subsegmentName = getSubsegmentName(methodSignature);

            Subsegment subsegment = AWSXRay.beginSubsegment(subsegmentName);
            if (subsegment == null) {
                return null;
            }
            Parameter[] parameters = methodSignature.getMethod().getParameters();
            Object[] args = joinPoint.getArgs();

            Map<String, Map<String, Object>> metadata = new HashMap<>();
            requestTrace(metadata, parameters, args);

            proceed = args.length == 0 ? joinPoint.proceed() : joinPoint.proceed(args);

            responseTrace(metadata, proceed);
            subsegment.setMetadata(metadata);
            return proceed;
        } catch (Throwable throwable) {
            AWSXRay.getCurrentSubsegmentOptional().ifPresent(currentSubsegment -> {
                currentSubsegment.addException(throwable);
            });
            throw throwable;
        } finally {
            AWSXRay.endSubsegment();
        }
    }

    private String getSubsegmentName(MethodSignature methodSignature) {
        Class className = methodSignature.getDeclaringType();
        String methodName = methodSignature.getName();
        return className + "." + methodName;
    }

    private void requestTrace(Map<String, Map<String, Object>> metadata, Parameter[] parameters, Object[] args) {
        Map<String, Object> request = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            request.put(parameters[i].getName(), ObjectUtils.nullSafeToString(args[i]));
        }
        metadata.put("request", request);
    }

    private void responseTrace(Map<String, Map<String, Object>> metadata, Object proceed) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", returnValue(proceed));
        metadata.put("response", response);
    }

    private Object returnValue(Object proceed) {
        if (proceed == null) {
            return "empty";
        }
        if (proceed instanceof List) {
            List list = (List) proceed;
            return !CollectionUtils.isEmpty(list) ? list : "[]";
        }
        return proceed;
    }
}
