package com.slide_todo.slide_todoApp.service.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.todo.GroupTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.RetrieveIndividualTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoCreateDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoUpdateDTO;
import com.slide_todo.slide_todoApp.repository.goal.GoalRepository;
import com.slide_todo.slide_todoApp.repository.goal.GroupGoalRepository;
import com.slide_todo.slide_todoApp.repository.goal.IndividualGoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupMemberRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService {

  private final TodoRepository todoRepository;
  private final GoalRepository goalRepository;
  private final GroupMemberRepository groupMemberRepository;
  private final GroupGoalRepository groupGoalRepository;
  private final IndividualGoalRepository individualGoalRepository;

  @Override
  @Transactional
  public ResponseDTO<?> createTodo(Long memberId, TodoCreateDTO request) {
    Goal goal = goalRepository.findByGoalId(request.getGoalId());
    if (goal.getDtype().equals("G")) {
      return new ResponseDTO<>(createGroupTodoDTO(memberId, request), Responses.CREATED);
    }
    return new ResponseDTO<>(createIndividualTodoDTO(memberId, request), Responses.CREATED);
  }

  @Override
  @Transactional
  public ResponseDTO<?> updateTodo(Long memberId, Long todoId, TodoUpdateDTO request) {
    Todo todo = todoRepository.findByTodoId(todoId);
    if (todo.getDtype().equals("G")) {
      return new ResponseDTO<>(updateGroupTodoDTO(memberId, (GroupTodo) todo, request),
          Responses.OK);
    }
    return new ResponseDTO<>(updateIndividualTodoDTO(memberId, (IndividualTodo) todo, request),
        Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> changeTodoDone(Long memberId, Long todoId) {
    Todo todo = todoRepository.findByTodoId(todoId);
    if (todo.getDtype().equals("G")) {

      return new ResponseDTO<>(doneGroupTodo(memberId, (GroupTodo) todo), Responses.OK);
    }
    return new ResponseDTO<>(doneIndividualTodo(memberId, (IndividualTodo) todo), Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> deleteTodoById(Long memberId, Long todoId) {
    Todo todo = todoRepository.findByTodoId(todoId);
    if (todo.getDtype().equals("G")) {
      GroupGoal goal = groupGoalRepository.findById(todo.getGoal().getId())
          .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));
      checkGroupPermission(memberId, goal.getGroup().getId());
    }

    todo.deleteTodo();
    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }

  @Override
  public ResponseDTO<List<IndividualTodoDTO>> getIndividualTodoList(Long memberId, RetrieveIndividualTodoDTO request) {
    List<IndividualTodo> individualTodos = todoRepository
        .findAllIndividualTodoByMemberId(memberId, request.getGoalIds(), request.getIsDone());

    List<IndividualTodoDTO> response = individualTodos.stream()
        .map(todo -> new IndividualTodoDTO(todo, todo.getGoal()))
        .toList();

    return new ResponseDTO<>(response, Responses.OK);
  }


  /**
   * 그룹에 대한 권한 조회
   *
   * @param memberId
   * @param groupId
   */
  private GroupMember checkGroupPermission(Long memberId, Long groupId) {
    List<GroupMember> groupMembers = groupMemberRepository.findAllByGroupId(groupId);
    for (GroupMember groupMember : groupMembers) {
      if (groupMember.getMember().getId().equals(memberId)) {
        return groupMember;
      }
    }
    throw new CustomException(Exceptions.NO_PERMISSION_FOR_THE_GROUP);
  }


  /**
   * 그룹 할 일 생성
   *
   * @param memberId
   * @param request
   * @return
   */
  private GroupTodoDTO createGroupTodoDTO(Long memberId, TodoCreateDTO request) {
    GroupGoal goal = groupGoalRepository.findById(request.getGoalId())
        .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));
    checkGroupPermission(memberId, goal.getGroup().getId());

    GroupTodo groupTodo = GroupTodo.builder()
        .title(request.getTitle())
        .linkUrl(request.getLinkUrl())
        .groupGoal(goal)
        .build();
    todoRepository.save(groupTodo);

    return new GroupTodoDTO(groupTodo, goal);
  }

  /**
   * 개인 할 일 생성
   *
   * @param memberId
   * @param request
   * @return
   */
  private IndividualTodoDTO createIndividualTodoDTO(Long memberId, TodoCreateDTO request) {
    IndividualGoal goal = individualGoalRepository.findById(request.getGoalId())
        .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

    IndividualTodo individualTodo = IndividualTodo.builder()
        .title(request.getTitle())
        .linkUrl(request.getLinkUrl())
        .individualGoal(goal)
        .build();
    todoRepository.save(individualTodo);

    return new IndividualTodoDTO(individualTodo, goal);
  }

  /**
   * 그룹 할 일 수정
   *
   * @param memberId
   * @param todo
   * @param request
   * @return
   */
  private GroupTodoDTO updateGroupTodoDTO(Long memberId, GroupTodo todo, TodoUpdateDTO request) {
    GroupGoal goal = groupGoalRepository.findById(todo.getGoal().getId())
        .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));
    checkGroupPermission(memberId, goal.getGroup().getId());

    todo.updateTodo(
        request.getTitle(),
        request.getLinkUrl()
    );
    return new GroupTodoDTO(todo, todo.getGoal());
  }

  /**
   * 개인 할 일 수정
   *
   * @param memberId
   * @param todo
   * @param request
   * @return
   */
  private IndividualTodoDTO updateIndividualTodoDTO(Long memberId, IndividualTodo todo,
      TodoUpdateDTO request) {
    todo.updateTodo(
        request.getTitle(),
        request.getLinkUrl()
    );
    return new IndividualTodoDTO(todo, todo.getGoal());
  }

  /**
   * 그룹 할 일 완료 처리
   *
   * @param memberId
   * @param todo
   * @return
   */
  private GroupTodoDTO doneGroupTodo(Long memberId, GroupTodo todo) {
    GroupGoal goal = groupGoalRepository.findById(todo.getGoal().getId())
        .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));
    GroupMember groupMember = checkGroupPermission(memberId, goal.getGroup().getId());

    todo.doneGroupTodo(groupMember);
    return new GroupTodoDTO(todo, goal);
  }

  private IndividualTodoDTO doneIndividualTodo(Long memberId, IndividualTodo todo) {
    todo.updateIndividualTodoDone();
    return new IndividualTodoDTO(todo, todo.getGoal());
  }
}
