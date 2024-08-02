package com.slide_todo.slide_todoApp.util.exception;

import lombok.Data;

@Data
public class ExceptionDTO {

      private String msg;
      private int code;

      public ExceptionDTO(CustomException e) {
          this.msg = e.getExceptions().getMsg();
          this.code = e.getExceptions().getCode();
      }
}
