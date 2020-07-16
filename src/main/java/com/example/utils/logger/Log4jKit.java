package com.example.utils.logger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName Log4jKit  日志打印工具,方便后期统一日志系统维护
 * @Author yupanpan
 * @Date 2020/5/13 10:27
 */
public class Log4jKit {

    /**
     * 获取最原始被调用的堆栈信息
     * @return
     */
    private static StackTraceElement findCaller() {
        // 获取堆栈信息
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        if (null == callStack) {
            return null;
        }
        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;
        String logClassName = Log4jKit.class.getName();
        // 循环遍历到日志类标识
        boolean isEachLogClass = false;

        // 遍历堆栈信息，获取出最原始被调用的方法信息
        for (StackTraceElement strackTraceEle : callStack) {
            // 遍历到日志类
            if (logClassName.equals(strackTraceEle.getClassName())) {
                isEachLogClass = true;
            }
            // 下一个非日志类的堆栈，就是最原始被调用的方法
            if (isEachLogClass) {
                if (!logClassName.equals(strackTraceEle.getClassName())) {
                    isEachLogClass = false;
                    caller = strackTraceEle;
                    break;
                }
            }
        }
        return caller;
    }

    /**
     * 自动匹配请求类名，生成logger对象，此处 logger name 值为 [className].[methodName]() Line: [fileLine]
     * @return
     */
    private static Logger logger() {
        StackTraceElement caller = findCaller();//最原始被调用的堆栈对象
        if (caller == null) {
            return LoggerFactory.getLogger(Log4jKit.class);
        } else {
            String methodName = caller.getMethodName();
//            System.out.println("getLineNumber: " + caller.getLineNumber());
            if (StringUtils.isBlank(methodName)) {
                return LoggerFactory.getLogger(caller.getClassName() + " Line:" + caller.getLineNumber() + "   " + Log4jKit.class.getSimpleName() + "==>>");
            }
            return LoggerFactory.getLogger(caller.getClassName() + "." + methodName + "() Line: " + caller.getLineNumber() + "   " + Log4jKit.class.getSimpleName() + "==>>");
        }
    }


    public static void trace(String msg) {
        logger().trace(msg);
    }

    public static void trace(String msg, Throwable e) {
        logger().trace(msg, e);
    }

    public static void trace(String format, Object... argArray) {
        logger().trace(format, argArray);
    }

    public static void debug(String msg) {
        logger().debug(msg);
    }

    public static void debug(String msg, Throwable e) {
        logger().debug(msg, e);
    }

    public static void debug(String format, Object... argArray) {
        logger().debug(format, argArray);
    }

    public static void info(String msg) {
        logger().info(msg);
    }

    public static void info(String msg, Throwable e) {
        logger().info(msg, e);
    }

    public static void info(String format, Object... argArray) {
        logger().info(format, argArray);
    }

    public static void warn(String msg) {
        logger().warn(msg);
    }

    public static void warn(String msg, Throwable e) {
        logger().warn(msg, e);
    }

    public static void warn(String format, Object... argArray) {
        logger().warn(format, argArray);
    }

    public static void error(String msg) {
        logger().error(msg);
    }

    public static void error(String msg, Throwable e) {
        logger().error(msg, e);
    }

    public static void error(String format, Object... argArray) {
        logger().error(format, argArray);
    }
}
