package com.slide_todo.slide_todoApp.service.note;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.group.GroupMemberDTO;
import com.slide_todo.slide_todoApp.dto.note.GroupNoteDTO;
import com.slide_todo.slide_todoApp.dto.note.GroupNoteListDTO;
import com.slide_todo.slide_todoApp.dto.note.IndividualNoteDTO;
import com.slide_todo.slide_todoApp.dto.note.IndividualNoteListDTO;
import com.slide_todo.slide_todoApp.dto.note.NoteCreateDTO;
import com.slide_todo.slide_todoApp.dto.note.NoteSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.note.NoteUpdateDTO;
import com.slide_todo.slide_todoApp.repository.goal.GoalRepository;
import com.slide_todo.slide_todoApp.repository.goal.GroupGoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupMemberRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.note.NoteRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

  private final NoteRepository noteRepository;
  private final TodoRepository todoRepository;
  private final GroupMemberRepository groupMemberRepository;
  private final GroupRepository groupRepository;
  private final GoalRepository goalRepository;
  private final GroupGoalRepository groupGoalRepository;

  @Override
  @Transactional
  public ResponseDTO<?> createNote(Long memberId, NoteCreateDTO request) {
    validateNote(request.getTitle(), request.getContent(), request.getLinkUrl());
    Todo todo = todoRepository.findByTodoId(request.getTodoId());

    if (todo.getDtype().equals("G")) {
      GroupTodo groupTodo = (GroupTodo) todo;
      GroupGoal groupGoal = groupGoalRepository.findById(groupTodo.getGoal().getId())
          .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

      groupMemberRepository.findByMemberIdAndGroupId(memberId, groupGoal.getGroup().getId());

      GroupNoteDTO response = createGroupNote(memberId, (GroupTodo) todo, request);
      return new ResponseDTO<>(response, Responses.CREATED);
    }
    IndividualNoteDTO response = createIndividualNote((IndividualTodo) todo, request);
    return new ResponseDTO<>(response, Responses.CREATED);
  }

  @Override
  @Transactional
  public ResponseDTO<?> updateNote(Long memberId, Long noteId, NoteUpdateDTO request) {
    validateNote(request.getTitle(), request.getContent(), request.getLinkUrl());
    Note note = noteRepository.findByNoteId(noteId);
    Long todoId = note.getTodo().getId();
    Todo todo = todoRepository.findByTodoId(todoId);

    if (todo.getDtype().equals("G")) {
      GroupTodo groupTodo = (GroupTodo) todo;
      if (groupTodo.getMemberInCharge() != null) { // 담당자가 존재하는 경우

        /*해당 할 일이 소속된 그룹 목표*/
        GroupGoal groupGoal = groupGoalRepository.findById(groupTodo.getGoal().getId())
            .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

        /*사용자의 그룹 멤버*/
        GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(
            memberId, groupGoal.getGroup().getId()
        );

        if (!groupTodo.getMemberInCharge().equals(groupMember)) {
          throw new CustomException(Exceptions.MUST_CHARGE_BEFORE_UPDATE_GROUP_NOTE);
        }
        note.updateNote(request.getTitle(), request.getContent(), request.getLinkUrl());
        return new ResponseDTO<>(new GroupNoteDTO(note), Responses.OK);
      }
    }
    note.updateNote(request.getTitle(), request.getContent(), request.getLinkUrl());
    return new ResponseDTO<>(new IndividualNoteDTO(note), Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> deleteNote(Long memberId, Long noteId) {
    Note note = noteRepository.findByNoteId(noteId);
    Long todoId = note.getTodo().getId();
    Todo todo = todoRepository.findByTodoId(todoId);

    if (todo.getDtype().equals("G")) {
      GroupTodo groupTodo = (GroupTodo) todo;
      if (groupTodo.getMemberInCharge() != null) {

        GroupGoal groupGoal = (GroupGoal) groupTodo.getGoal();
        GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(
            memberId, groupGoal.getGroup().getId()
        ); // 사용자의 그룹 멤버

        if (!groupTodo.getMemberInCharge().equals(groupMember)) {
          throw new CustomException(Exceptions.MUST_CHARGE_BEFORE_DELETE_GROUP_NOTE);
        }
        note.deleteNote();
        noteRepository.delete(note);
        return new ResponseDTO<>(null, Responses.NO_CONTENT);
      }
    }
    note.deleteNote();
    noteRepository.delete(note);
    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }

  @Override
  public ResponseDTO<?> getOneNote(Long memberId, Long noteId) {
    Note note = noteRepository.findByNoteId(noteId);
    Long todoId = note.getTodo().getId();
    Todo todo = todoRepository.findByTodoId(todoId);

    if (todo.getDtype().equals("G")) {
      GroupGoal groupGoal = groupGoalRepository.findById(todo.getGoal().getId())
          .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

      Group group = groupGoal.getGroup();
      groupMemberRepository.findByMemberIdAndGroupId(memberId, group.getId());
      return new ResponseDTO<>(new GroupNoteDTO(note), Responses.OK);
    }
    return new ResponseDTO<>(new IndividualNoteDTO(note), Responses.OK);
  }

  @Override
  public ResponseDTO<?> getNotesByGoal(Long memberId, long page, long limit, Long goalId) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = noteRepository.count();
    }
    NoteSearchResultDTO searchResult = noteRepository.findAllByGoalId(goalId, start, limit);
    List<Note> notes = searchResult.getNotes();
    Goal goal = goalRepository.findByGoalId(goalId);

    if (goal.getDtype().equals("G")) {
      GroupGoal groupGoal = (GroupGoal) goal;
      Group group = groupGoal.getGroup();
      groupMemberRepository.findByMemberIdAndGroupId(memberId, group.getId());

      List<Long> todoIds = notes.stream().map(note -> note.getTodo().getId()).toList();
      GroupNoteListDTO response = new GroupNoteListDTO(searchResult.getTotalCount(), page, notes);
//      List<GroupNoteDTO> response = notes.stream().map(GroupNoteDTO::new).toList();

      Map<Long, GroupMember> assignees = groupMemberRepository.findAllByGroupTodoIds(todoIds);
      response.getNotes().forEach(note -> {
        if (assignees.containsKey(note.getTodo().getId())) {
          note.getTodo().setMemberInCharge(new GroupMemberDTO(assignees.get(note.getTodo().getId())));
        }
      });
      return new ResponseDTO<>(response, Responses.OK);
    }
    IndividualNoteListDTO response = new IndividualNoteListDTO(searchResult.getTotalCount(), page, notes);
//    List<IndividualNoteDTO> response = notes.stream().map(IndividualNoteDTO::new).toList();
    return new ResponseDTO<>(response, Responses.OK);
  }


  /**
   * 개인 할 일의 노트 작성
   *
   * @param todo
   * @param request
   * @return
   */
  private IndividualNoteDTO createIndividualNote(IndividualTodo todo, NoteCreateDTO request) {
    /*이미 노트가 존재하는 경우*/
    if (todo.getNote() != null) {
      throw new CustomException(Exceptions.NOTE_ALREADY_EXISTS);
    }

    Note note = Note.builder().todo(todo)
        .title(request.getTitle())
        .content(request.getContent())
        .linkUrl(request.getLinkUrl())
        .build();

    noteRepository.save(note);
    return new IndividualNoteDTO(note);
  }

  /**
   * 그룹 할 일의 노트 작성
   *
   * @param memberId
   * @param todo
   * @param request
   * @return
   */
  private GroupNoteDTO createGroupNote(Long memberId, GroupTodo todo, NoteCreateDTO request) {
    /*이미 노트가 존재하는 경우*/
    if (todo.getNote() != null) {
      throw new CustomException(Exceptions.NOTE_ALREADY_EXISTS);
    }

    GroupGoal groupGoal = (GroupGoal) todo.getGoal();
    Group group = groupRepository.findGroupWithGroupMembers(groupGoal.getGroup().getId());
    GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(memberId,
        group.getId());

    if (!group.getGroupMembers().contains(groupMember)) {
      throw new CustomException(Exceptions.NO_PERMISSION_FOR_THE_GROUP);
    }
    if (todo.getMemberInCharge() != null) {
      if (!todo.getMemberInCharge().equals(groupMember)) {
        throw new CustomException(Exceptions.MUST_CHARGE_BEFORE_CREATE_GROUP_NOTE);
      }
    }
    todo.updateMemberInCharge(groupMember);
    Note note = Note.builder().todo(todo)
        .title(request.getTitle())
        .content(request.getContent())
        .linkUrl(request.getLinkUrl())
        .build();
    noteRepository.save(note);
    return new GroupNoteDTO(note);
  }

  private void validateNote(String title, String content, String url) {
    if (title.length() > 30) {
      throw new CustomException(Exceptions.TITLE_TOO_LONG);
    }
    if (content.length() > 10000) {
      throw new CustomException(Exceptions.NOTE_TOO_LONG);
    }
    if ((url != null) && (url.length() > 255)) {
      throw new CustomException(Exceptions.URL_TOO_LONG);
    }
  }
}
