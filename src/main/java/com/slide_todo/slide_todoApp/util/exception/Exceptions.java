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
  TODO_CONTENT_TOO_LONG(400, "The todo content can be up to 255 characters"),
  NOTE_TOO_LONG(400, "The note content can be up to 10,000 characters"),
  URL_TOO_LONG(400, "The URL can be up to 255 characters"),
  REGISTERED_EMAIL(400, "Email already exists"),
  EXIST_NICKNAME(400, "Nickname already exists"),
  INVALID_EMAIL(400, "Invalid email format"),
  INVALID_PASSWORD(400, "Invalid password. Password must be at least 8 characters"
      + " long and include a mix of letters, numbers, and symbols."),
  ALREADY_CHARGED_TODO(400, "Already charged todo"),


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
  NO_PERMISSION_FOR_THE_GROUP(403, "No permission for the group"),
  NOT_CHARGED_GROUP_MEMBER(403, "Not charged group member"),
  MUST_CHARGE_BEFORE_DONE_GROUP_TODO(403, "Must charge before done group todo"),
  MUST_CHARGE_BEFORE_CREATE_GROUP_NOTE(403, "Must charge before create group note"),
  MUST_CHARGE_BEFORE_UPDATE_GROUP_NOTE(403, "Must charge before update group note"),
  MUST_CHARGE_BEFORE_DELETE_GROUP_NOTE(403, "Must charge before delete group note"),
  ADMIN_ONLY(403, "Admin only"),


  /*404 NOT_FOUND*/
  GOAL_NOT_FOUND(404, "Goal not exists"),
  MEMBER_NOT_FOUND(404, "Member not exists"),
  MEMBER_WITH_EMAIL_NOT_FOUND(404, "Member with email not found"),
  TODO_NOT_FOUND(404, "Todo not exists"),
  NOTE_NOT_FOUND(404, "Note not exists"),
  CODE_NOT_FOUND(404, "없는 초대코드 입니다."),
  GROUP_NOT_FOUND(404, "없는 그룹입니다."),

  /*405 METHOD_NOT_ALLOWED*/
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  /*408 REQUEST_TIMEOUT*/
  REQUEST_TIMEOUT(408, "Request Timeout"),

  /*409 CONFLICT*/
  NOTE_ALREADY_EXISTS(409, "Note of the task already exists"),
  GROUP_ALREADY_EXISTS(409, "이미 사용 중인 이름입니다."),
  ALREADY_JOINED_GROUP(409, "Already joined group"),

  /*500 INTERNAL_SERVER_ERROR*/
  INTERNAL_SERVER_ERROR(500, "Internal server error");

  private final int code;
  private final String msg;

  Exceptions(int code, String message) {
    this.code = code;
    this.msg = message;
  }
}
