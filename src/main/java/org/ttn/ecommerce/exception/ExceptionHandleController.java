package org.ttn.ecommerce.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandleController extends ResponseEntityExceptionHandler {


    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errMessage="";
        if(exception.getFieldError()==null){
            errMessage = exception.getGlobalError().getDefaultMessage();
        }else{
            errMessage = exception.getFieldError().getDefaultMessage();
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), "Validation Failed", errMessage);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UserNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(),"Not Found",ex.getMessage());
        return new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<Object> handleAddressNotFoundException(AddressNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(),"Address Not Found",ex.getMessage());
        return new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(),"Not Found",ex.getMessage());
        return  new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(),"Not Found",ex.getMessage());
        return  new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(),"Not Found",ex.getMessage());
        return  new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }
}
