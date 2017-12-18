package io.coinshell.web;

import io.coinshell.exception.ServiceException;
import io.coinshell.license.LicenseNotice;
import io.coinshell.web.response.VersionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import java.io.IOException;
import java.util.Properties;

@RestController
public class CustomErrorController {
//public class CustomErrorController implements ErrorController {
//    private final ErrorAttributes errorAttributes;
//
//    @Autowired
//    public CustomErrorController(ErrorAttributes errorAttributes) {
//        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
//        this.errorAttributes = errorAttributes;
//    }
//
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//
//    @RequestMapping
//    public Map<String, Object> error(HttpServletRequest aRequest){
//        Map<String, Object> body = getErrorAttributes(aRequest,getTraceParameter(aRequest));
//        String trace = (String) body.get("trace");
//        if(trace != null){
//            String[] lines = trace.split("\n\t");
//            body.put("trace", lines);
//        }
//        return body;
//    }
//
//    private boolean getTraceParameter(HttpServletRequest request) {
//        String parameter = request.getParameter("trace");
//        if (parameter == null) {
//            return false;
//        }
//        return !"false".equals(parameter.toLowerCase());
//    }
//
//    private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
//        RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
//        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
//    }

}
