package lxy.com.aoplibrary;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;

/**
 * Creator : lxy
 * date: 2019/4/17
 */
@Aspect
public class ViewClickAspect {

    private static final String TAG = "ViewClickAspect";

    @Before("execution(* android.app.Activity.on*(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        codeSignature.getDeclaringTypeName();

        Log.e(TAG, "onActivityMethodBefore: " + key + "," + codeSignature.getDeclaringTypeName());
    }

    @Before("execution(* android.support.v4.app.Fragment.on*(..))")
    public void onFragmentMethodBefore(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        codeSignature.getDeclaringTypeName();

        Log.e(TAG, "onFragmentMethodBefore: " + key + "," + codeSignature.getDeclaringTypeName());
    }

    @Before("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onViewClick(JoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        String cls = codeSignature.getDeclaringTypeName(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        String[] parameterNames = codeSignature.getParameterNames(); //方法参数名集合
        Object[] parameterValues = joinPoint.getArgs(); //方法参数集合

        Log.i(TAG, "onViewClick: " + cls);
        Log.i(TAG, "onViewClick: " + methodName);
        for (String str : parameterNames) {
            Log.i(TAG, "onViewClick: " + str);
        }
        for (Object str : parameterValues) {
            Log.i(TAG, "onViewClick: " + str);
        }
        Log.i(TAG,cls + "-> " + parameterValues[0]);
    }
}
