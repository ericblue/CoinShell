package io.coinshell.util;

import org.apache.log4j.Logger;

/**
 * <p>Title:		ClassCaller</p><br>
 * <p>Description:	Utility class to help determine who has called a particular Class and/or Method</p><br>
 * @author		    <a href="mailto:ericblue76@gmail.com">Eric T. Blue</a><br>
 *
 * $Date: 2011-08-15 03:37:39 $
 * $Author: eblue $
 * $Revision: 1.2 $
 *
 */

public class ClassCaller {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ClassCaller.class);


    /**
     * Gets full class name of the caller.
     *
     * stackDepth = 1: Provides the immediate caller
     * StackDepth > 1: Provides the chain of callers in the stack trace
     *
     * @param stackDepth
     * @return Class.getName() of the caller
     */
    public static String getCallingClassName(int stackDepth) {

        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        if (!isDepthValid(stackDepth)) {
            throw new IllegalArgumentException("Stack depth is too large!");
        }

        // Increment stackDepth to ignore who is calling this class
        String className = stackTrace[stackDepth+1].getClassName();
        logger.debug("getCallingClass[" + stackDepth + "] = " + className);

        return className;

    }

    /**
     * Gets method name of the caller.
     *
     * stackDepth = 1: Provides the immediate caller
     * StackDepth > 1: Provides the chain of callers in the stack trace
     *
     * @param stackDepth
     * @return Method name of the caller
     */
    public static String getCallingMethod(int stackDepth) {

        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        if (!isDepthValid(stackDepth)) {
            throw new IllegalArgumentException("Stack depth is too large!");
        }

        // Increment stackDepth to ignore who is calling this class
        String methodName = stackTrace[stackDepth+1].getMethodName();
        logger.debug("getCallingMethod[" + stackDepth + "] = " + methodName);

        return methodName;

    }

    /**
     * Validates the requested stack depth and compares against the stack trace
     *
     * @param stackDepth
     * @return boolean
     */
    private static boolean isDepthValid(int stackDepth) {

        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        logger.debug("isDepthValid: stackDepth = " + stackDepth + ", strackTrace.length = " + stackTrace.length);

        if ( (stackDepth >= stackTrace.length) || (stackDepth < 1)) {
            return false;
        }
        else {
            return true;
        }

    }


}
