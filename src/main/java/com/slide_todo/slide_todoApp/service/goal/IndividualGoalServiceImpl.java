package com.slide_todo.slide_todoApp.service.goal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.goal.GoalTodosResponseDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualGoalDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualGoalTodoDTO;
import com.slide_todo.slide_todoApp.repository.goal.IndividualGoalRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndividualGoalServiceImpl implements IndividualGoalService {

    private final MemberRepository memberRepository;
    private final IndividualGoalRepository individualGoalRepository;
    private final TodoRepository todoRepository;

    //개인 목표 생성
    @Override
    @Transactional
    public ResponseDTO<IndividualGoalDTO> createIndividualGoal(String title, Long memberId){
        checkTitleLength(title);

        Member member = memberRepository.findByMemberId(memberId);
        IndividualGoal individualGoal = IndividualGoal.builder()
                .title(title)
                .member(member)
                .build();

        individualGoalRepository.save(individualGoal);
        IndividualGoalDTO individualGoalDTO = new IndividualGoalDTO(individualGoal);
        return new ResponseDTO<>(individualGoalDTO, Responses.CREATED);
    }

    //개인 목표 리스트 조회
    @Override
    @Transactional
    public ResponseDTO<?> getIndividualGoals(Long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        List<IndividualGoal> individualGoals = individualGoalRepository.findAllByMember(member);
        List<IndividualGoalDTO> individualGoalDTOS = new ArrayList<>();

        for (IndividualGoal individualGoal : individualGoals) {
            individualGoalDTOS.add(new IndividualGoalDTO(individualGoal));
        }

        Map<String, List<IndividualGoalDTO>> goalList = new HashMap<>();
        goalList.put("individualGoals", individualGoalDTOS);

        return new ResponseDTO<>(goalList, Responses.OK);
    }

    //개인 목표 & 할일 리스트 조회
    @Override
    public ResponseDTO<GoalTodosResponseDTO<IndividualGoalTodoDTO>> getIndividualGoalTodos(Long memberId, int page, Integer limit) {
        Member member = memberRepository.findByMemberId(memberId);
        List<IndividualGoal> individualGoals;

        if (limit == 0) {
            // limit가 0이면 모든 데이터를 가져옴
            individualGoals = individualGoalRepository.findAllByMember(member);
        } else {
            PageRequest pageRequest = PageRequest.of(page - 1, limit);
            individualGoals = individualGoalRepository.findAllByMember(member, pageRequest);
        }

        List<IndividualGoalTodoDTO> individualGoalTodoDTOS = new ArrayList<>();

        for (IndividualGoal individualGoal : individualGoals) {
            IndividualGoalTodoDTO individualGoalTodoDTO = IndividualGoalTodoDTO.builder()
                    .title(individualGoal.getTitle())
                    .id(individualGoal.getId())
                    .memberId(memberId)
                    .updatedAt(individualGoal.getUpdatedAt())
                    .createdAt(individualGoal.getCreatedAt())
                    .build();

            // 할 일 리스트
            List<IndividualTodo> individualTodos = todoRepository.findAllByGoal(individualGoal);

            // 진행률 계산
            long totalTodos = individualTodos.size();
            long completedTodos = individualTodos.stream().filter(IndividualTodo::getIsDone).count();
            double progress = totalTodos > 0 ? (double) completedTodos / totalTodos * 100 : 0.0;
            progress = Math.round(progress * 10.0) / 10.0; // 소수점 한 자리까지 반올림

            // 진행률 설정
            individualGoalTodoDTO.setProgress(progress);

            List<IndividualGoalTodoDTO.IndividualTodoDto> todoDtos = individualTodos.stream()
                    .map(todo -> IndividualGoalTodoDTO.IndividualTodoDto.builder()
                            .id(todo.getId())
                            .noteId(todo.getNote() != null ? todo.getNote().getId() : null)
                            .title(todo.getTitle())
                            .done(todo.getIsDone())
                            .build())
                    .collect(Collectors.toList());

            // 할 일 리스트 설정
            individualGoalTodoDTO.setTodos(todoDtos);
            // DTO에 추가
            individualGoalTodoDTOS.add(individualGoalTodoDTO);
        }

        Long totalCount = individualGoalRepository.countByMember(member);

        return new ResponseDTO<>(new GoalTodosResponseDTO<>(page, totalCount, individualGoalTodoDTOS), Responses.OK);
    }



    //개인 목표 수정
    @Override
    @Transactional
    public ResponseDTO<IndividualGoalDTO> updateIndividualGoal(Long goalId, String title){
        checkTitleLength(title);

        IndividualGoal individualGoal = individualGoalRepository.findById(goalId)
                .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

        individualGoal.setTitle(title);
        individualGoalRepository.save(individualGoal);
        IndividualGoalDTO individualGoalDTO = new IndividualGoalDTO(individualGoal);
        return new ResponseDTO<>(individualGoalDTO, Responses.OK);
    }

    //개인 목표 삭제
    @Override
    @Transactional
    public ResponseDTO<?> deleteIndividualGoal(Long goalId){
        IndividualGoal individualGoal = individualGoalRepository.findById(goalId)
                .orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

//        individualGoal.setIsDeleted(true);
        individualGoal.deleteGoal();
        individualGoalRepository.save(individualGoal);

        return new ResponseDTO<>(null, Responses.NO_CONTENT);
    }


    public void checkTitleLength(String title){
        if(title.length() > 30){
            throw new CustomException(Exceptions.TITLE_TOO_LONG);
        }
    }

    //진행률 계산 메소드
    public double calProgress(List<IndividualTodo> todos){
        long totalTodos = todos.size();
        long completedTodos = todos.stream().filter(IndividualTodo::getIsDone).count();
        double progress = totalTodos > 0 ? (double) completedTodos / totalTodos * 100 : 0.0;
        progress = Math.round(progress * 10.0) / 10.0; // 소수점 한 자리까지 반올림
        return progress;
    }
}
