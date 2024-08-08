package com.slide_todo.slide_todoApp.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.slide_todo.slide_todoApp.TestGenerator;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.note.NoteSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.todo.GroupTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoCreateDTO;
import com.slide_todo.slide_todoApp.repository.goal.GoalRepository;
import com.slide_todo.slide_todoApp.repository.goal.GroupGoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.repository.note.NoteRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.service.todo.TodoService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class NoteRepositoryTest {

  @Autowired
  private NoteRepository noteRepository;
  @Autowired
  private TestGenerator generator;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private GroupRepository groupRepository;
  @Autowired
  private GroupGoalRepository groupGoalRepository;
  @Autowired
  private TodoRepository todoRepository;
  @Autowired
  private TodoService todoService;
  @Autowired
  private GoalRepository goalRepository;

  @Test
  @Transactional
  public void 그룹노트_테스트() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Group group = groupRepository.save(generator.createGroup(member));
    GroupGoal goal = groupGoalRepository.save(generator.createGroupGoal(group));
    TodoCreateDTO request = new TodoCreateDTO(goal.getId(), "title", "content");
    GroupTodoDTO expected = (GroupTodoDTO) todoService
        .createTodo(member.getId(), request).getData();
    GroupTodo todo = (GroupTodo) todoRepository.findByTodoId(expected.getId());
    Note note = noteRepository.save(generator.createNote(todo, "title", "note"));

    /*when*/
    NoteSearchResultDTO searched = noteRepository.findGroupNoteByAdmin(null, null, null, null, 0,
        10);
    Long noteId = searched.getNotes().get(0).getId();
    Note result = noteRepository.findByNoteId(noteId);

    /*then*/
    assertEquals(note, result);
  }


  @Test
  @Transactional
  public void 개인노트_테스트() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());
    Goal goal = goalRepository.save(generator.createIndividualGoal(member));
    Todo todo = todoRepository.save(generator.createIndividualTodo(goal));
    Note note = noteRepository.save(generator.createNote(todo, "title", "note"));

    /*when*/
    NoteSearchResultDTO searched = noteRepository.findIndividualNoteByAdmin(null, null, null, null, 0,
        10);
    Long noteId = searched.getNotes().get(0).getId();
    Note result = noteRepository.findByNoteId(noteId);

    /*then*/
    assertEquals(note, result);
  }
}
