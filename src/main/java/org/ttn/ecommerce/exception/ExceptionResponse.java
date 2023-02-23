package org.ttn.ecommerce.exception;

import java.time.LocalDateTime;
@Getter
@Setter
public class ExceptionResponse{
  LocalDateTime timestamp;
  String message;
  String details;
  
  ExceptionResponse(LocalDateTime timestamp,String message,String details){
  this.timestamp=timestamp;
  this.message=message;
  this.details=details;
  }
  
  ExceptionResponse(){}

}
