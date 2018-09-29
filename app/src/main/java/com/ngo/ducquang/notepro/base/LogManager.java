package com.ngo.ducquang.notepro.base;

import android.util.Log;

import com.ngo.ducquang.notepro.BuildConfig;

/**
 * Created by ducqu on 9/17/2018.
 */

public class LogManager {
    private static final String DEFAULT_TAG = "FSI";
    private static final String CHAT_TAG = "tagChat";

    private static final String SPCACE = " ";
    private static final String CLASS = "(at ";
    private static final String CLOSE_CLASS = ") ";
    private static final String METHOD = "()";

    private String tag = DEFAULT_TAG;
    private final boolean DEBUG = BuildConfig.DEBUG; // true false;

    public static String exception(Throwable throwable)
    {
        String stackTrace = "";
        if(throwable != null)
        {
            StackTraceElement[] elementArray = throwable.getStackTrace();
            stackTrace = "\n".concat((throwable == null ? "" : throwable.toString()));
            for(int i = 0; elementArray != null && i < elementArray.length; i++)
            {
                stackTrace = String.format("%s\nat %s", stackTrace, elementArray[i].toString());
            }
        }

        return stackTrace;
    }

    public static LogManager tagDefault()
    {
        return new LogManager(DEFAULT_TAG);
    }

    public static LogManager tagChat()
    {
        return new LogManager(CHAT_TAG);
    }

    private LogManager(String tag)
    {
        this.tag = tag;
    }

    private String processObjectList(Object... objectList)
    {
        return (objectList == null || objectList.length < 1) ? "" : String.valueOf(objectList[0]);
    }

    public void verbose(Object... objectList)
    {
        if(DEBUG)
        {
            Exception exception = new Exception();
            String className = exception.getStackTrace()[1].getFileName();
            String methodName = exception.getStackTrace()[1].getMethodName();
            int lineNumber = exception.getStackTrace()[1].getLineNumber();

            Log.v(tag, processObjectList(objectList) + SPCACE + className + CLASS + lineNumber + CLOSE_CLASS + methodName + METHOD);
        }
    }

    public void debug(Object... objectList)
    {
        if(DEBUG)
        {
            Exception exception = new Exception();
            String className = exception.getStackTrace()[1].getFileName();
            String methodName = exception.getStackTrace()[1].getMethodName();
            int lineNumber = exception.getStackTrace()[1].getLineNumber();

            Log.d(tag, processObjectList(objectList) + SPCACE + className + CLASS + lineNumber + CLOSE_CLASS + methodName + METHOD);
        }
    }

    public void info(Object... objectList)
    {
        if(DEBUG)
        {
            Exception exception = new Exception();
            String className = exception.getStackTrace()[1].getFileName();
            String methodName = exception.getStackTrace()[1].getMethodName();
            int lineNumber = exception.getStackTrace()[1].getLineNumber();

            Log.i(tag, processObjectList(objectList) + SPCACE + className + CLASS + lineNumber + CLOSE_CLASS + methodName + METHOD);
        }
    }

    public void warn(Object... objectList)
    {
        if(DEBUG)
        {
            Exception exception = new Exception();
            String className = exception.getStackTrace()[1].getFileName();
            String methodName = exception.getStackTrace()[1].getMethodName();
            int lineNumber = exception.getStackTrace()[1].getLineNumber();

            Log.w(tag, processObjectList(objectList) + SPCACE + className + CLASS + lineNumber + CLOSE_CLASS + methodName + METHOD);
        }
    }

    public void error(Object... objectList)
    {
        if(DEBUG)
        {
            Exception exception = new Exception();
            String className = exception.getStackTrace()[1].getFileName();
            String methodName = exception.getStackTrace()[1].getMethodName();
            int lineNumber = exception.getStackTrace()[1].getLineNumber();

            Log.e(tag, processObjectList(objectList) + SPCACE + className + CLASS + lineNumber + CLOSE_CLASS + methodName + METHOD);
        }
    }

//    public void Assert(Object... objectList)
//    {
//        if(DEBUG)
//        {
//            Exception exception = new Exception();
//            String className = exception.getStackTrace()[1].getFileName();
//            String methodName = exception.getStackTrace()[1].getMethodName();
//            int lineNumber = exception.getStackTrace()[1].getLineNumber();
//
//            Log.wtf(tag, processObjectList(objectList) + SPCACE + className + CLASS + lineNumber + CLOSE_CLASS + methodName + METHOD);
//        }
//    }
}
