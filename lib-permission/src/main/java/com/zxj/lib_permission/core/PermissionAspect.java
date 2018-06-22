package com.zxj.lib_permission.core;

import android.content.Context;
import android.util.Log;

import com.zxj.lib_permission.PermissionActivity;
import com.zxj.lib_permission.PermissionUtils;
import com.zxj.lib_permission.annotation.Permission;
import com.zxj.lib_permission.annotation.PermissionCanceled;
import com.zxj.lib_permission.annotation.PermissionDenied;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 用@Aspect标注切面类
 * Created by jay on 2018/6/22.
 */
@Aspect
public class PermissionAspect {

    private static final String TAG = PermissionAspect.class.getSimpleName();

    /**
     * 定义PointCut（切入点）
     * @param permission
     */
    @Pointcut("execution(@com.zxj.lib_permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission) {}


    /**
     * 对进入切面的内容如何处理
     * Advance比较常用的有：Before():方法执行前,After():方法执行后,Around():代替原有逻辑
     * @param joinPoint
     * @param permission
     */
    @Around("requestPermission(permission)")
    public void aroundJointPoint(final ProceedingJoinPoint joinPoint, Permission permission){
        //初始化context
        Context context = null;

        final Object object = joinPoint.getThis();
        if (joinPoint.getThis() instanceof Context) {
            context = (Context) object;
        } else if (joinPoint.getThis() instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else if (joinPoint.getThis() instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else {
        }

        if (context == null || permission == null) {
            Log.d(TAG, "aroundJonitPoint error ");
            return;
        }

        final Context finalContext = context;
        PermissionActivity.requestPermission(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void ganted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancled() {
                PermissionUtils.invokeAnnotation(object, PermissionCanceled.class);
            }

            @Override
            public void denied() {
                PermissionUtils.invokeAnnotation(object, PermissionDenied.class);
                PermissionUtils.goToMenu(finalContext);
            }
        });
    }
}
