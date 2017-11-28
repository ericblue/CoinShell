package io.coinshell.exception;

public class RestClientException extends RuntimeException {


    /**
     *
     */
    private static final long serialVersionUID = -7546776083585771502L;


    /**
     * Exception with a specified detail message
     *
     * @param message Detail message
     */
    public RestClientException(String message) {

        super(message);

    }


    /**
     * Exception with a specified detail message and throwing Exception
     *
     * @param message Detail message
     * @param e       Throwing exception
     */
    public RestClientException(String message, Exception e) {

        super(message, e);

    }


    /**
     * Exception with throwing Exception
     *
     * @param e Throwing exception
     */
    public RestClientException(Exception e) {

        super(e);


    }
}
