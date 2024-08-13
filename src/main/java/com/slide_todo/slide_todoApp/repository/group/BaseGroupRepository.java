package com.slide_todo.slide_todoApp.repository.group;

public interface BaseGroupRepository {

  /*그룹에 대한 유저의 소속 여부 확인*/
  boolean checkGroupPermission(Long memberId, Long groupId);
}
