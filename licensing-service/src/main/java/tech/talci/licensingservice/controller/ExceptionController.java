package tech.talci.licensingservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tech.talci.licensingservice.model.ErrorMessage;
import tech.talci.licensingservice.model.ResponseWrapper;
import tech.talci.licensingservice.model.RestErrorList;

import javax.servlet.http.HttpServletRequest;

import static java.util.Collections.singletonMap;

@ControllerAdvice
@EnableWebMvc
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    public @ResponseBody
    ResponseEntity<ResponseWrapper> handleException(HttpServletRequest request,
                                                    ResponseWrapper responseWrapper){

        return ResponseEntity.ok(responseWrapper);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseWrapper> handleIOException(HttpServletRequest request, RuntimeException e){

        RestErrorList errorList = new RestErrorList(HttpStatus.NOT_ACCEPTABLE, new ErrorMessage(e.getMessage(), e.getMessage()));
        ResponseWrapper responseWrapper = new ResponseWrapper(null, singletonMap("status", HttpStatus.NOT_ACCEPTABLE), errorList);


        return ResponseEntity.ok(responseWrapper);
    }
}
