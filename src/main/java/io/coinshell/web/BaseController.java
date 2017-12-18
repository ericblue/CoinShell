package io.coinshell.web;

import io.coinshell.exception.ExceptionWithCode;
import io.coinshell.exception.codes.ExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BaseController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public BaseController() {

    }
    @SuppressWarnings("rawtypes")
    @ExceptionHandler({RuntimeException.class, Error.class, Exception.class})
    protected @ResponseBody
    Map<String, Map> handleRuntimeException(Exception ex, HttpServletRequest request) {

        logger.debug("Called handleRuntimeException");
        logger.debug("Got exception " + ex);

        String fullStackTrace = getStackTrace(ex);
        logger.debug("Got stracktrace " + fullStackTrace);


        // TODO Figure out why RuntimeExceptions are handled here
        //  but in the cases of 400 bad request the errorMessage is not returned. For now, hack and set root
        // exception to a session variable and render in handleHttpErrors

        request.getSession().setAttribute("lastException", ex.getMessage());

        Map<String, Map> errorMessage = new HashMap<String, Map>();

        Map<String, String> details = new HashMap<>();
        details.put("description", ex.getMessage());
        details.put("exceptionType", ex.getClass().toString());
        if (ex.getCause() != null) {

            details.put("exceptionCause", ex.getCause().getMessage());

        }
        details.put("code", String.valueOf(ExceptionCode.UNKNOWN.getCode()));
        details.put("codeName", String.valueOf(ExceptionCode.UNKNOWN));


        errorMessage.put("error", details);

        return errorMessage;

    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler({ ExceptionWithCode.class })
    protected @ResponseBody
    Map<String, Map> handleAPIException(ExceptionWithCode ex, HttpServletRequest request) {

        logger.debug("Called handleAPIException");

        String fullStackTrace = getStackTrace(ex);
        logger.debug("Got stracktrace " + fullStackTrace);

        Map<String, Map> errorMessage = new HashMap<String, Map>();

        Map<String, String> details = new HashMap<String, String>();

        details.put("description", ex.getMessage());
        details.put("exceptionType", ex.getClass().toString());
        if (ex.getCause() != null) {
            details.put("exceptionCause", ex.getCause().getMessage());
        }

        ExceptionCode code = ex.getCode();
        if (code != null) {
            details.put("code", String.valueOf(code.getCode()));
            details.put("codeName", code.name());
        } else {
            details.put("code", String.valueOf(ExceptionCode.UNKNOWN.getCode()));
            details.put("codeName", String.valueOf(ExceptionCode.UNKNOWN));
        }

        errorMessage.put("error", details);


        return errorMessage;


    }

    protected static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);

        pw.flush();
        sw.flush();
        return sw.toString();
    }

}
