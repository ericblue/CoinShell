package io.coinshell.exception;

import io.coinshell.exception.codes.ExceptionCode;

public class ServiceException extends ExceptionWithCode {

    private ExceptionCode exceptionCode = ExceptionCode.SERVICE_EXCEPTION;


    public ServiceException() {

        super();
        super.setCode(exceptionCode);

    }

    public ServiceException(Exception e) {

        super(e);
        super.setCode(exceptionCode);

    }


    /**
     * Exception with a specified detail message
     *
     * @param message   Detail message
     */
    public ServiceException(String message) {

        super(message);
        super.setCode(exceptionCode);

    }

    /**
     * Exception with a specified detail message and throwing Exception
     *
     * @param message   Detail message
     * @param e         Throwing exception
     */
    public ServiceException(String message, Exception e) {

        super(message, e);
        super.setCode(exceptionCode);


    }

}
