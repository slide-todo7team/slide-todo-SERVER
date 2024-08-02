package com.slide_todo.slide_todoApp.util.exception;

import lombok.Getter;

@Getter
public enum Exceptions {

  /*400 BAD_REQUEST*/
  BAD_REQUEST(400, "Bad Request"),
  INVALID_SIZE(400, "Size is number only"),
  CONTENT_REQUIRED(400, "Content is required"),
  INVALID_NOTE_ID(400, "Note id is number only"),
  UPDATE_CONTENT_REQUIRED(400, "Content is required"),
  INVALID_GOAL_ID(400, "Goal id is number only"),
  TITLE_TOO_LONG(400, "The title can be up to 30 characters."),


  /*401 UNAUTHORIZED*/
  UNAUTHORIZED(401, "Unauthorized"),
  BLACKLISTED_TOKEN(401, "Blacklisted token"),
  INVALID_TOKEN(401, "Invalid token"),
  NOT_ACCESS_TOKEN(401, "Not access token"),
  TOKEN_NOT_FOUND(401, "Token not found"),
  EXPIRED_TOKEN(401, "Expired token"),
  PREMATURE_TOKEN(401, "Premature token"),

  /*403 FORBIDDEN*/
  DELETE_FORBIDDEN(403, "Have no permission to delete"),
  CREATE_FORBIDDEN(403, "Have no permission to create"),
  RETRIEVE_FORBIDDEN(403, "Have no permission to retrieve"),
  UPDATE_FORBIDDEN(403, "Have no permission to update"),


  /*404 NOT_FOUND*/
  GOAL_NOT_FOUND(404, "Goal not exists"),
  MEMBER_NOT_FOUND(404, "Member not exists"),
  TASK_NOT_FOUND(404, "Task not exists"),
  NOTE_NOT_FOUND(404, "Note not exists"),

  /*405 METHOD_NOT_ALLOWED*/
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  /*408 REQUEST_TIMEOUT*/
  REQUEST_TIMEOUT(408, "Request Timeout"),

  /*409 CONFLICT*/
  NOTE_ALREADY_EXISTS(409, "Note of the task already exists"),

  /*500 INTERNAL_SERVER_ERROR*/
  INTERNAL_SERVER_ERROR(500, "Internal server error");

  private final int code;
  private final String msg;

  Exceptions(int code, String message) {
    this.code = code;
    this.msg = message;
  }
}
