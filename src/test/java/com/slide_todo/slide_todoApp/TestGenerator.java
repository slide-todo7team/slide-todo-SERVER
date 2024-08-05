package com.slide_todo.slide_todoApp;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.group.GroupCreateDTO;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestGenerator {

  private final SecureRandom secureRandom = new SecureRandom();
  private static final String CHARACTERS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * 랜덤 문자열 생성기
   *
   * @param length 길이
   * @return
   */
  public String generateRandomString(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(CHARACTERS.length());
      char randomChar = CHARACTERS.charAt(randomIndex);
      sb.append(randomChar);
    }
    return sb.toString();
  }

  /**
   * 랜덤 long 정수 생성기
   *
   * @return
   */
  public long generateRandomLong() {
    return secureRandom.nextLong();
  }

  /**
   * 랜덤 long 정수 생성기
   *
   * @param max 최대값
   * @param min 최소값
   * @return
   */
  public long generateRandomLong(long max, long min) {
    return secureRandom.nextLong(max - min) + min;
  }

  /**
   * 랜덤 int 정수 생성기
   *
   * @return
   */
  public int generateRandomInt() {
    return secureRandom.nextInt();
  }

  /**
   * 랜덤 int 정수 생성기
   *
   * @param max 최대값
   * @param min 최소값
   * @return
   */
  public int generateRandomInt(int max, int min) {
    return secureRandom.nextInt(max - min) + min;
  }

  /**
   * 랜덤 boolean 생성기
   *
   * @return
   */
  public boolean generateRandomBoolean() {
    return secureRandom.nextBoolean();
  }

  /**
   * 랜덤 이메일 생성기
   *
   * @return
   */
  public String generateRandomEmail() {
    return generateRandomString(10) + "@" + generateRandomString(5) + ".com";
  }

  public Member createMember() {
    return Member.builder()
        .email(generateRandomEmail())
        .password(passwordEncoder.encode(generateRandomString(10)))
        .name(generateRandomString(10))
        .nickname(generateRandomString(10))
        .build();
  }

  public Member createMember(String password) {
    return Member.builder()
        .email(generateRandomEmail())
        .password(passwordEncoder.encode(password))
        .name(generateRandomString(10))
        .nickname(generateRandomString(10))
        .build();
  }

  public IndividualTodo createIndividualTodo(Goal goal) {
    return IndividualTodo.builder()
        .individualGoal(goal)
        .title(generateRandomString(10))
        .content(generateRandomString(10))
        .build();
  }

  public GroupTodo createGroupTodo(Goal goal) {
    return GroupTodo.builder()
        .groupGoal(goal)
        .title(generateRandomString(10))
        .content(generateRandomString(10))
        .build();
  }

  public IndividualGoal createIndividualGoal(Member member) {
    return IndividualGoal.builder()
        .member(member)
        .title(generateRandomString(10))
        .build();
  }

  public GroupGoal createGroupGoal(Group group) {
    return GroupGoal.builder()
        .group(group)
        .title(generateRandomString(10))
        .build();
  }

  public Group createGroup(Member member) {
    GroupCreateDTO dto = GroupCreateDTO.builder()
        .title(generateRandomString(10))
        .secretCode(generateRandomString(10))
        .build();
    return Group.builder()
        .groupCreateDTO(dto)
        .member(member)
        .build();
  }

  public Note createNote(Todo todo, String title, String content) {
    return Note.builder()
        .todo(todo)
        .title(title)
        .content(content)
        .build();
  }
}
