package com.slide_todo.slide_todoApp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.slide_todo.slide_todoApp.TestGenerator;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
//import com.slide_todo.slide_todoApp.repository.goal.GoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.repository.note.NoteRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TodoRepositoryTest {

//  @Autowired
//  private TestGenerator generator;
//  @Autowired
//  private TodoRepository todoRepository;
//  @Autowired
//  private GoalRepository goalRepository;
//  @Autowired
//  private MemberRepository memberRepository;
//  @Autowired
//  private GroupRepository groupRepository;
//  @Autowired
//  private NoteRepository noteRepository;
//
//
//  @Test
//  @Transactional
//  public void 개인_할일_생성() throws Exception {
//    /*given*/
//    Member testMember = generator.createMember();
//    memberRepository.save(testMember);
//
//    IndividualGoal testGoal = generator.createIndividualGoal(testMember);
//    goalRepository.save(testGoal);
//
//    /*when*/
//    IndividualTodo individualTodo = generator.createIndividualTodo(testGoal);
//    IndividualTodo result = todoRepository.save(individualTodo);
//
//    /*then*/
//    assertEquals(individualTodo, result);
//  }
//
//  @Test
//  @Transactional
//  public void 그룹_할일_생성() throws Exception {
//    /*given*/
//    Member testMember = generator.createMember();
//    memberRepository.save(testMember);
//
//    Group group = generator.createGroup(testMember);
//    groupRepository.save(group);
//
//    GroupGoal testGoal = generator.createGroupGoal(group);
//    goalRepository.save(testGoal);
//
//    /*when*/
//    GroupTodo groupTodo = generator.createGroupTodo(testGoal);
//    GroupTodo result = todoRepository.save(groupTodo);
//
//    /*then*/
//    assertEquals(groupTodo, result);
//  }
//
//  @Test
//  @Transactional
//  public void 개인_할일_조회() throws Exception {
//    /*given*/
//    Member testMember = generator.createMember();
//    memberRepository.save(testMember);
//
//    IndividualGoal testGoal = generator.createIndividualGoal(testMember);
//    goalRepository.save(testGoal);
//
//    /*when*/
//    int created = 0;
//    int done = 0;
//
//    for (int i = 0; i < 100; i++) {
//      if (generator.generateRandomBoolean()) {
//        IndividualTodo individualTodo = generator.createIndividualTodo(testGoal);
//        todoRepository.save(individualTodo);
//        created++;
//        if (generator.generateRandomBoolean()) {
//          individualTodo.doneTodo();
//          done++;
//          if (generator.generateRandomBoolean()) {
//            individualTodo.deleteTodo();
//            created--;
//            done--;
//          }
//        }
//      }
//    }
//
//    List<Long> goalIds = new ArrayList<>();
//    goalIds.add(testGoal.getId());
//
//    List<IndividualTodo> createdList = todoRepository
//        .findAllIndividualTodoByMemberId(testMember.getId(), goalIds, null);
//
//    List<IndividualTodo> doneList = todoRepository
//        .findAllIndividualTodoByMemberId(testMember.getId(), goalIds, true);
//
//    List<IndividualTodo> notDoneList = todoRepository
//        .findAllIndividualTodoByMemberId(testMember.getId(), goalIds, false);
//
//    /*then*/
//    assertEquals(created, createdList.size());
//    assertEquals(done, doneList.size());
//    assertEquals(created - done, notDoneList.size());
//  }
//
//  @Test
//  @Transactional
//  public void 노트_생성() throws Exception {
//    /*given*/
//    Member testMember = generator.createMember();
//    memberRepository.save(testMember);
//
//    IndividualGoal testGoal = generator.createIndividualGoal(testMember);
//    goalRepository.save(testGoal);
//
//    IndividualTodo individualTodo = generator.createIndividualTodo(testGoal);
//    todoRepository.save(individualTodo);
//
//    /*when*/
//    String title = generator.generateRandomString(10);
//    String content = generator.generateRandomString(100);
//    Note note = generator.createNote(individualTodo, title, content);
//    Note result = noteRepository.save(note);
//
//    /*then*/
//    assertEquals(note, result);
//    assertEquals(note.getTitle(), title);
//    assertEquals(note.getContent(), content);
//    assertEquals(note.getTodo(), individualTodo);
//  }
//
//  @Test
//  @Transactional
//  public void 노트_삭제() throws Exception {
//    /*given*/
//    Member testMember = generator.createMember();
//    memberRepository.save(testMember);
//
//    IndividualGoal testGoal = generator.createIndividualGoal(testMember);
//    goalRepository.save(testGoal);
//
//    IndividualTodo individualTodo = generator.createIndividualTodo(testGoal);
//    todoRepository.save(individualTodo);
//
//    String title = generator.generateRandomString(10);
//    String content = generator.generateRandomString(100);
//    Note note = generator.createNote(individualTodo, title, content);
//    noteRepository.save(note);
//
//    note.deleteNote();
//
//    /*when*/
//    List<IndividualTodo> individualTodos = todoRepository.findAllIndividualTodoByMemberId(
//        testMember.getId(), null, null);
//
//    /*then*/
//    assertEquals(note, individualTodos.get(0).getNote());
//  }
}
