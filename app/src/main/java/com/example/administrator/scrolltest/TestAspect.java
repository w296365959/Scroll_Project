package com.example.administrator.scrolltest;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2019/5/30 0030 13:17 </br>
 * Summary:
 */
@Aspect
public class TestAspect {
    public TestAspect() {
        super();
    }

    @Pointcut("execution(* *(..))")
    public void pointcut() {

    }
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint)throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String name = signature.getName();
        if (name.equals("onClick")) {
            Log.e("Mr.S", "preClick ");

            joinPoint.proceed();
            Log.e("Mr.S", "afterClick ");
        }else {
            return  joinPoint.proceed();
        }


        return null;
    }
}
