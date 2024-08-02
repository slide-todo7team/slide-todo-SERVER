package com.slide_todo.slide_todoApp.util.exception;

import lombok.Getter;

@Getter
public enum Exceptions {

  /*400 BAD_REQUEST*/
  BAD_REQUEST(400, "Bad Request"),
  INVALID_SIZE(400, "size는 숫자로 입력해주세요."),
  CONTENT_REQUIRED(400, "content를 입력해 주세요"),
  INVALID_NOTE_ID(400, "noteId는 숫자로 입력해주세요."),
  UPDATE_CONTENT_REQUIRED(400, "수정 내용을 입력해 주세요."),
  INVALID_GOAL_ID(400, "goalId는 숫자로 입력해주세요."),
  TITLE_TOO_LONG(400, "title은 최대 30자 이하로 작성해주세요"),
  FILE_TOO_LARGE(400, "파일 크기는 3MB 이하만 가능합니다."),


  /*401 UNAUTHORIZED*/
  UNAUTHORIZED(401, "Unauthorized"),
  BLACKLISTED_TOKEN(401, "Blacklisted Token"),
  INVALID_TOKEN(401, "Invalid Token"),
  INVALID_ISSUED_TIME(401, "Invalid Issued Time"),
  NOT_ACCESS_TOKEN(401, "Not Access Token"),
  TOKEN_NOT_FOUND(401, "Token Not Found"),
  EXPIRED_TOKEN(401, "Expired Token"),
  PREMATURE_TOKEN(401, "Premature Token"),

  /*403 FORBIDDEN*/
  DELETE_FORBIDDEN(403, "삭제 권한이 없습니다."),
  CREATE_FORBIDDEN(403, "생성 권한이 없습니다."),
  RETRIEVE_FORBIDDEN(403, "조회 권한이 없습니다."),
  UPDATE_FORBIDDEN(403, "수정 권한이 없습니다."),


  /*404 NOT_FOUND*/
  GOAL_NOT_FOUND(404, "목표를 찾을 수 없습니다."),
  USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
  TASK_NOT_FOUND(404, "할 일을 찾을 수 없습니다."),
  NOTE_NOT_FOUND(404, "노트를 찾을 수 없습니다."),

  /*405 METHOD_NOT_ALLOWED*/
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  /*408 REQUEST_TIMEOUT*/
  REQUEST_TIMEOUT(408, "Request Timeout"),

  /*409 CONFLICT*/
  NOTE_ALREADY_EXISTS(409, "이미 노트가 존재합니다."),

  /*500 INTERNAL_SERVER_ERROR*/
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  private final int code;
  private final String msg;

  Exceptions(int code, String message) {
    this.code = code;
    this.msg = message;
  }
}
