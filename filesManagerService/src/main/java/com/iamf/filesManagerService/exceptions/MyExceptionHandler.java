package com.iamf.filesManagerService.exceptions;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.responses.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.sql.SQLException;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ResponseMessage> myExceptionHandler(MyException me){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(me.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseMessage> nullPointerExceptionHandler(NullPointerException me){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(me.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> runtimeExceptionHandler(RuntimeException me){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(me.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseMessage> sqlExceptionHandler(SQLException me){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(me.getMessage()));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ResponseMessage> fileNotFoundExceptionHandler(FileNotFoundException me){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(me.getMessage()));
    }

}
