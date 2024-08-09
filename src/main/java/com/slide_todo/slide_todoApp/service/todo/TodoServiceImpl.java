package com.slide_todo.slide_todoApp.service.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.todo.GroupTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.RetrieveIndividualTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoCreateDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoUpdateDTO;
import com.slide_todo.slide_todoApp.repository.goal.GoalRepository;
import com.slide_todo.slide_todoApp.repository.goal.GroupGoalRepository;
import com.slide_todo.slide_todoApp.repository.goal.IndividualGoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupMemberRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
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
  private final GroupRepository groupRepository;

  @Override
  @Transactional
  public ResponseDTO<?> createTodo(Long memberId, TodoCreateDTO request) {
    validateTodo(request.getTitle(), request.getContent());
    Goal goal = goalRepository.findByGoalId(request.getGoalId());
    if (goal.getDtype().equals("G")) {
      GroupTodo todo = createGroupTodo(memberId, request);
      return new ResponseDTO<>(new GroupTodoDTO(todo, todo.getGoal()), Responses.CREATED);
    }
    IndividualTodo todo = createIndividualTodo(memberId, request);
    return new ResponseDTO<>(new IndividualTodoDTO(todo, todo.getGoal()), Responses.CREATED);
  }

  @Override
  @Transactional
  public ResponseDTO<?> updateTodo(Long memberId, Long todoId, TodoUpdateDTO request) {
    validateTodo(request.getTitle(), request.getContent());
    Todo todo = todoRepository.findByTodoId(todoId);
    if (todo.getDtype().equals("G")) {
      GroupTodo newTodo = updateGroupTodo(memberId, (GroupTodo) todo, request);
      return new ResponseDTO<>(new GroupTodoDTO(newTodo, newTodo.getGoal()),
          Responses.OK);
    }
    IndividualTodo newTodo = updateIndividualTodo(memberId, (IndividualTodo) todo, request);
    return new ResponseDTO<>(new IndividualTodoDTO(newTodo, newTodo.getGoal()),
        Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> changeTodoDone(Long memberId, Long todoId) {
    Todo todo = todoRepository.findByTodoId(todoId);
    if (todo.getDtype().equals("G")) {
      GroupTodo newTodo = doneGroupTodo(memberId, (GroupTodo) todo);
      return new ResponseDTO<>(new GroupTodoDTO(newTodo, newTodo.getGoal()), Responses.OK);
    }
    IndividualTodo newTodo = doneIndividualTodo(memberId, (IndividualTodo) todo);
    return new ResponseDTO<>(new IndividualTodoDTO(newTodo, newTodo.getGoal())
        , Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> deleteTodo(Long memberId, Long todoId) {
    Todo todo = todoRepository.findByTodoId(todoId);
    Goal goal = goalRepository.findWithTodos(todo.getGoal().getId());

    if (todo.getDtype().equals("G")) {
      GroupGoal groupGoal = groupGoalRepository.findById(todo.getGoal().getId())
          .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));
      checkGroupPermission(memberId, groupGoal.getGroup().getId());
    }

    todo.deleteTodo();
    goal.updateProgressRate();
    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }

  @Override
  public ResponseDTO<IndividualTodoListDTO> getIndividualTodoList(Long memberId,
      Long page, Long limit, RetrieveIndividualTodoDTO request
  ) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = todoRepository.count();
    }


    List<IndividualTodo> individualTodos = todoRepository
        .findAllIndividualTodoByMemberId(memberId, start, limit, request.getGoalIds(),
            request.getIsDone());

    List<IndividualTodoDTO> result = individualTodos.stream()
        .map(todo -> new IndividualTodoDTO(todo, todo.getGoal()))
        .toList();
    Long total = todoRepository.countAllIndividualTodoByMemberId(memberId, request.getGoalIds(),
        request.getIsDone());

    IndividualTodoListDTO response = new IndividualTodoListDTO(total, page, result);

    return new ResponseDTO<>(response, Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<GroupTodoDTO> updateChargingGroupMember(Long memberId, Long todoId) {
    GroupTodo groupTodo = (GroupTodo) todoRepository.findByTodoId(todoId);

    GroupGoal groupGoal = (GroupGoal) groupTodo.getGoal();
    Group group = groupRepository.findGroupWithGroupMembers(groupGoal.getGroup().getId());
    GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(memberId,
        group.getId());

    if (!group.getGroupMembers().contains(groupMember)) {
      throw new CustomException(Exceptions.NO_PERMISSION_FOR_THE_GROUP);
    }
    if (groupTodo.getMemberInCharge() != null) {
      if (groupTodo.getMemberInCharge().equals(groupMember)) {
        groupTodo.updateMemberInCharge(groupMember);
      } else {
        throw new CustomException(Exceptions.ALREADY_CHARGED_TODO);
      }
    } else {
      groupTodo.updateMemberInCharge(groupMember);
    }

    return new ResponseDTO<>(
        new GroupTodoDTO(groupTodo, groupGoal),
        Responses.OK
    );
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
  private GroupTodo createGroupTodo(Long memberId, TodoCreateDTO request) {
    GroupGoal goal = groupGoalRepository.findById(request.getGoalId())
        .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));
    checkGroupPermission(memberId, goal.getGroup().getId());

    GroupTodo groupTodo = GroupTodo.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .groupGoal(goal)
        .build();
    GroupTodo todo = todoRepository.save(groupTodo);
    goal.updateProgressRate();
    return todo;
  }

  /**
   * 개인 할 일 생성
   *
   * @param memberId
   * @param request
   * @return
   */
  private IndividualTodo createIndividualTodo(Long memberId, TodoCreateDTO request) {
    IndividualGoal goal = individualGoalRepository.findById(request.getGoalId())
        .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

    IndividualTodo individualTodo = IndividualTodo.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .individualGoal(goal)
        .build();
    IndividualTodo todo = todoRepository.save(individualTodo);
    goal.updateProgressRate();
    return todo;
  }

  /**
   * 그룹 할 일 수정
   *
   * @param memberId
   * @param todo
   * @param request
   * @return
   */
  private GroupTodo updateGroupTodo(Long memberId, GroupTodo todo, TodoUpdateDTO request) {
    GroupGoal goal = groupGoalRepository.findById(todo.getGoal().getId())
        .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));
    checkGroupPermission(memberId, goal.getGroup().getId());

    todo.updateTodo(
        request.getTitle(),
        request.getContent()
    );
    return todo;
  }

  /**
   * 개인 할 일 수정
   *
   * @param memberId
   * @param todo
   * @param request
   * @return
   */
  private IndividualTodo updateIndividualTodo(Long memberId, IndividualTodo todo,
      TodoUpdateDTO request) {
    todo.updateTodo(
        request.getTitle(),
        request.getContent()
    );
    return todo;
  }

  /**
   * 그룹 할 일 완료 처리
   *
   * @param memberId
   * @param todo
   * @return
   */
  private GroupTodo doneGroupTodo(Long memberId, GroupTodo todo) {
    GroupGoal goal = groupGoalRepository.findGroupGoalWithTodos(todo.getGoal().getId());
    GroupMember groupMember = checkGroupPermission(memberId, goal.getGroup().getId());

    if (todo.getMemberInCharge() == null) {
      throw new CustomException(Exceptions.MUST_CHARGE_BEFORE_DONE_GROUP_TODO);
    }

    if (!todo.getMemberInCharge().equals(groupMember)) {
      throw new CustomException(Exceptions.NOT_CHARGED_GROUP_MEMBER);
    }

    groupMember.increaseTodoCount();
    todo.updateGroupTodoDone();
    goal.updateProgressRate();
    return todo;
  }

  private IndividualTodo doneIndividualTodo(Long memberId, IndividualTodo todo) {
    todo.updateIndividualTodoDone();
    IndividualGoal goal = individualGoalRepository.findIndividualGoalWithTodos(todo.getGoal().getId());
    goal.updateProgressRate();
    return todo;
  }

  private void validateTodo(String title, String content) {
    if (title.length() > 30) {
      throw new CustomException(Exceptions.TITLE_TOO_LONG);
    }
    if (content.length() > 255) {
      throw new CustomException(Exceptions.TODO_CONTENT_TOO_LONG);
    }
  }
}
