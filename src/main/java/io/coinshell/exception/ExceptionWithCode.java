package io.coinshell.exception;

import io.coinshell.exception.codes.ExceptionCode;
import lombok.Data;

@Data
public class ExceptionWithCode extends RuntimeException {


    private ExceptionCode code = ExceptionCode.NONE;


    public ExceptionWithCode() {

        super();

    }

    /**
     * Exception with a specified detail message
     *
     * @param message Detail message
     */
    public ExceptionWithCode(String message) {

        super(message);

    }


    /**
     * Exception with a specified detail message and throwing Exception
     *
     * @param message Detail message
     * @param e       Throwing exception
     */
    public ExceptionWithCode(String message, Exception e) {

        super(message, e);

    }


    /**
     * Exception with throwing Exception
     *
     * @param e Throwing exception
     */
    public ExceptionWithCode(Exception e) {

        super(e);

    }


}
