package com.macro.mall.tiny.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一日志处理切面
 * Created by macro on 2018/4/26.
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.macro.mall.tiny.controller.*.*(..))")
    public void webLog() {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            printLogInfo(joinPoint,result,startTime,endTime);
            return result;
        } catch (Exception e) {
            printLogInfo(joinPoint,null,null,null);
            throw e;
        }
    }
    
    public void printLogInfo(ProceedingJoinPoint joinPoint,Object result,Long startTime,Long endTime){
        StringBuffer sb = new StringBuffer();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        String description = "";
        String spendTime = startTime == null ? "--" : (endTime - startTime)+"ms";
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            description = method.getAnnotation(ApiOperation.class).value();
        }
        sb.append("\n----------------------------"+DateFormatUtils.format(startTime,"yyyy-MM-dd HH:mm:ss")+"----------------------------");
        sb.append("\nURL : "+request.getRequestURL());
        sb.append("\nDescription : "+description);
        sb.append("\nBasePath : "+StrUtil.removeSuffix(request.getRequestURL(), URLUtil.url(String.valueOf(request.getRequestURL())).getPath()));
        sb.append("\nURI( "+request.getMethod()+" ) : "+request.getRequestURI());
        sb.append("\nSpendTime  : "+spendTime);
        sb.append("\nUserName   : "+request.getRemoteUser());
        sb.append("\nController : "+joinPoint.getTarget().getClass().getName()+"("+joinPoint.getTarget().getClass().getSimpleName()+".java:1)");
        sb.append("\nMethod     : "+joinPoint.getSignature().getName());
        sb.append("\nParameters : "+JSONUtil.parse(getParameter(method, joinPoint.getArgs())));
        sb.append("\nReturn     : "+JSONUtil.parse(result));
        sb.append("\n-------------------------------------------------------------------------------------------\n");
        log.info(String.valueOf(sb));
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
