package com.slide_todo.slide_todoApp.service;

import static org.junit.jupiter.api.Assertions.*;

import com.slide_todo.slide_todoApp.TestGenerator;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.dto.todo.GroupTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.RetrieveIndividualTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoCreateDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoUpdateDTO;
import com.slide_todo.slide_todoApp.repository.goal.GoalRepository;
import com.slide_todo.slide_todoApp.repository.goal.GroupGoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.service.goal.IndividualGoalService;
import com.slide_todo.slide_todoApp.service.todo.TodoService;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TodoServiceTest {

  @Autowired
  private TodoService todoService;
  @Autowired
  private TestGenerator generator;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private TodoRepository todoRepository;
  @Autowired
  private GoalRepository goalRepository;
  @Autowired
  private GroupGoalRepository groupGoalRepository;
  @Autowired
  private GroupRepository groupRepository;
  @Autowired
  private IndividualGoalService individualGoalService;


  @Test
  @Transactional
  public void 개인_할일_생성() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Long goalId = individualGoalService.createIndividualGoal("title", member.getId()).getData()
        .getId();
    TodoCreateDTO request = new TodoCreateDTO(goalId, "title", "linkUrl");

    /*when*/
    IndividualTodoDTO expected = (IndividualTodoDTO) todoService.createTodo(member.getId(), request)
        .getData();
    IndividualTodo todo = (IndividualTodo) todoRepository.findByTodoId(expected.getId());
    IndividualTodoDTO result = new IndividualTodoDTO(todo, todo.getGoal());

    /*then*/
    assertEquals(expected, result);
  }


  @Test
  @Transactional
  public void 그룹_할일_생성() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Group group = groupRepository.save(generator.createGroup(member));
    GroupGoal goal = groupGoalRepository.save(generator.createGroupGoal(group));
    TodoCreateDTO request = new TodoCreateDTO(goal.getId(), "title", "linkUrl");

    /*when*/
    GroupTodoDTO expected = (GroupTodoDTO) todoService
        .createTodo(member.getId(), request).getData();
    GroupTodo todo = (GroupTodo) todoRepository.findByTodoId(expected.getId());
    GroupTodoDTO result = new GroupTodoDTO(todo, todo.getGoal());

    /*then*/
    assertEquals(expected, result);
  }

  @Test
  @Transactional
  public void 개인_할일_수정() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Long goalId = individualGoalService.createIndividualGoal("title", member.getId()).getData()
        .getId();
    TodoCreateDTO request = new TodoCreateDTO(goalId, "title", "linkUrl");
    IndividualTodoDTO dto = (IndividualTodoDTO) todoService.createTodo(member.getId(), request)
        .getData();
    Long todoId = dto.getId();

    /*when*/
    IndividualTodoDTO result = (IndividualTodoDTO) todoService.updateTodo(
        member.getId(), todoId, new TodoUpdateDTO("title2", "linkUrl2")
    ).getData();

    IndividualTodoDTO expected = new IndividualTodoDTO(
        (IndividualTodo) todoRepository.findByTodoId(result.getId()),
        goalRepository.findByGoalId(goalId)
    );

    /*then*/
    assertEquals(expected, result);
  }

  @Test
  @Transactional
  public void 그룹_할일_수정() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Group group = groupRepository.save(generator.createGroup(member));
    GroupGoal goal = groupGoalRepository.save(generator.createGroupGoal(group));
    TodoCreateDTO request = new TodoCreateDTO(goal.getId(), "title", "linkUrl");
    GroupTodoDTO dto = (GroupTodoDTO) todoService.createTodo(member.getId(), request).getData();
    Long todoId = dto.getId();

    /*when*/
    GroupTodoDTO result = (GroupTodoDTO) todoService.updateTodo(
        member.getId(), todoId, new TodoUpdateDTO("title2", "linkUrl2")
    ).getData();

    GroupTodoDTO expected = new GroupTodoDTO(
        (GroupTodo) todoRepository.findByTodoId(result.getId()),
        goalRepository.findByGoalId(goal.getId())
    );

    /*then*/
    assertEquals(expected, result);
  }

  @Test
  @Transactional
  public void 개인_할일_완료() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Long goalId = individualGoalService.createIndividualGoal("title", member.getId()).getData()
        .getId();
    TodoCreateDTO request = new TodoCreateDTO(goalId, "title", "linkUrl");
    IndividualTodoDTO dto = (IndividualTodoDTO) todoService.createTodo(member.getId(), request)
        .getData();
    Long todoId = dto.getId();

    /*when*/
    todoService.changeTodoDone(member.getId(), todoId);
    IndividualTodo todo = (IndividualTodo) todoRepository.findByTodoId(todoId);

    /*then*/
    assertTrue(todo.getIsDone());
  }

  @Test
  @Transactional
  public void 그룹_할일_완료() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Group group = groupRepository.save(generator.createGroup(member));
    GroupGoal goal = groupGoalRepository.save(generator.createGroupGoal(group));
    TodoCreateDTO request = new TodoCreateDTO(goal.getId(), "title", "linkUrl");
    GroupTodoDTO dto = (GroupTodoDTO) todoService.createTodo(member.getId(), request).getData();
    Long todoId = dto.getId();

    /*when*/
    todoService.changeTodoDone(member.getId(), todoId);
    GroupTodo todo = (GroupTodo) todoRepository.findByTodoId(todoId);

    /*then*/
    assertTrue(todo.getIsDone());
  }

  @Test
  @Transactional
  public void 개인_할일_삭제() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Long goalId = individualGoalService.createIndividualGoal("title", member.getId()).getData()
        .getId();
    TodoCreateDTO request = new TodoCreateDTO(goalId, "title", "linkUrl");
    IndividualTodoDTO dto = (IndividualTodoDTO) todoService.createTodo(member.getId(), request)
        .getData();
    Long todoId = dto.getId();

    /*when*/
    todoService.deleteTodo(member.getId(), todoId);
    Exception exception = assertThrows(
        CustomException.class, () -> todoRepository.findByTodoId(todoId)
    );

    /*then*/
    assertEquals(exception.getMessage(), Exceptions.TODO_NOT_FOUND.getMsg());
  }

  @Test
  @Transactional
  public void 그룹_할일_삭제() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Group group = groupRepository.save(generator.createGroup(member));
    GroupGoal goal = groupGoalRepository.save(generator.createGroupGoal(group));
    TodoCreateDTO request = new TodoCreateDTO(goal.getId(), "title", "linkUrl");
    GroupTodoDTO dto = (GroupTodoDTO) todoService.createTodo(member.getId(), request).getData();
    Long todoId = dto.getId();

    /*when*/
    todoService.deleteTodo(member.getId(), todoId);
    Exception exception = assertThrows(
        CustomException.class, () -> todoRepository.findByTodoId(todoId)
    );

    /*then*/
    assertEquals(exception.getMessage(), Exceptions.TODO_NOT_FOUND.getMsg());
  }

  @Test
  @Transactional
  public void 개인의_모든_할일_조회() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Long goalId = individualGoalService.createIndividualGoal("title", member.getId()).getData()
        .getId();

    int todos = generator.generateRandomInt(50, 10);
    for (int i = 0; i < todos; i++) {
      TodoCreateDTO request = new TodoCreateDTO(goalId, "title", "linkUrl");
      todoService.createTodo(member.getId(), request);
    }

    /*when*/
    List<IndividualTodoDTO> result = todoService
        .getIndividualTodoList(member.getId(), new RetrieveIndividualTodoDTO(null, null)).getData();

    /*then*/
    assertEquals(todos, result.size());
  }

  @Test
  @Transactional
  public void 개인의_할일_조건_검색() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Long goalId1 = individualGoalService.createIndividualGoal("title1", member.getId()).getData()
        .getId();
    Long goalId2 = individualGoalService.createIndividualGoal("title2", member.getId()).getData()
        .getId();

    int expected1 = 0;
    for (int i = 0; i < 100; i++) {
      if (generator.generateRandomBoolean()) {
        TodoCreateDTO request = new TodoCreateDTO(goalId1, "title", "linkUrl");
        IndividualTodoDTO dto = (IndividualTodoDTO) todoService.createTodo(member.getId(), request)
            .getData();
        expected1++;

        if (generator.generateRandomBoolean()) {
          todoService.changeTodoDone(member.getId(), dto.getId());
          expected1--;
        }
      } else {
        TodoCreateDTO request = new TodoCreateDTO(goalId2, "title", "linkUrl");
        todoService.createTodo(member.getId(), request);
      }
    }

    /*when*/

    List<IndividualTodoDTO> result = todoService
        .getIndividualTodoList(member.getId(),
            new RetrieveIndividualTodoDTO(List.of(goalId1), false)).getData();

    /*then*/
    assertEquals(expected1, result.size());
  }
}
