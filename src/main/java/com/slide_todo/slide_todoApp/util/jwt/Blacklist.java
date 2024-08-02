package com.slide_todo.slide_todoApp.util.jwt;

public interface Blacklist {

  /* 블랙리스트에 토큰 삽입 */
  void putToken(String token, String date);

  /* 블랙리스트에 토큰이 있는지 확인*/
  boolean containsToken(String token);
}
