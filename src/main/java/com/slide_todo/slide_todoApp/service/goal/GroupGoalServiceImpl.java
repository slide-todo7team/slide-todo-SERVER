package com.slide_todo.slide_todoApp.service.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.dto.goal.GoalTodosResponseDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalTodoDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupProgressDTO;
import com.slide_todo.slide_todoApp.dto.goal.SingleGoalDTO;
import com.slide_todo.slide_todoApp.repository.goal.GroupGoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupMemberRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupGoalServiceImpl implements GroupGoalService {

    private final TodoRepository todoRepository;
    private final IndividualGoalServiceImpl individualGoalServiceImpl;
    private final GroupRepository groupRepository;
    private final GroupGoalRepository groupGoalRepository;
    private final GroupMemberRepository groupMemberRepository;

    //그룹 목표 생성
    @Override
    @Transactional
    public ResponseDTO<GroupGoalDTO> createGroupGoal(Long memberId, Long groupId,String title){
        GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(memberId, groupId);
        individualGoalServiceImpl.checkTitleLength(title);

        Group group = groupRepository.findById(groupId).get();

        GroupGoal groupGoal = GroupGoal.builder()
                .title(title)
                .group(group)
                .groupMember(groupMember)
                .build();

        groupGoalRepository.save(groupGoal);
        GroupGoalDTO groupGoalDTO = new GroupGoalDTO(groupGoal);
        return new ResponseDTO<>(groupGoalDTO, Responses.CREATED);
    }

    //그룹 목표 리스트 조회
    @Override
    public ResponseDTO<?> getGroupGoals(Long groupId){
        Group group = groupRepository.findById(groupId).get();
        List<GroupGoal> groupGoals = groupGoalRepository.findAllByGroup(group);

        List<GroupGoalDTO> groupGoalDTOS = new ArrayList<>();

        for(GroupGoal groupGoal: groupGoals){
            groupGoalDTOS.add(new GroupGoalDTO(groupGoal));
        }

        Map<String, List<GroupGoalDTO>> goalList = new HashMap<>();
        goalList.put("groupGoals", groupGoalDTOS);

        return new ResponseDTO<>(goalList,Responses.OK);
    }


    //그룹 목표 & 할일 리스트 조회
    @Override
    public ResponseDTO<GoalTodosResponseDTO<GroupGoalTodoDTO>> getGroupGoalTodos(Long groupId,int page,Integer limit){
        List<GroupGoal> groupGoals;
        Group group = groupRepository.findById(groupId).get();

        if (limit == 0) {
            groupGoals = groupGoalRepository.findAllByGroup(group);
        } else {
            PageRequest pageRequest = PageRequest.of(page-1, limit);
            groupGoals = groupGoalRepository.findAllByGroup(group,pageRequest);
        }

        List<GroupGoalTodoDTO> groupGoalTodoDTOS = new ArrayList<>();

        List<GroupMember> groupMembers = groupMemberRepository.findAllByGroupId(groupId);

        for(GroupGoal groupGoal : groupGoals){
            List<GroupTodo> groupTodos = todoRepository.findAllByGoal(groupGoal.getId());

            List<GroupProgressDTO.GroupGoalMemDTO> groupGoalMemDTOS = groupMembers.stream()
                    .map(member -> {
                        Integer contributionPercent = calContributionPercent(groupTodos, member.getId());

                        return GroupProgressDTO.GroupGoalMemDTO.builder()
                                .nickname(member.getMember().getNickname())
                                .contributionPercent(contributionPercent)
                                .color(member.getColor().getHexCode())
                                .build();
                    })
                    .toList();

            GroupProgressDTO groupProgressDTO = GroupProgressDTO.builder()
                    .completedPercent(groupGoal.getProgressRate())
                    .members(groupGoalMemDTOS)
                    .build();

            List<GroupGoalTodoDTO.GroupTodoDTO> groupTodoDTOS = groupTodos.stream()
                    .map(todo -> GroupGoalTodoDTO.GroupTodoDTO.builder()
                            .noteId(todo.getNote()!= null ? todo.getNote().getId() : null)
                            .isDone(todo.getIsDone())
                            .title(todo.getTitle())
                            .id(todo.getId())
                            .memberInCharge(todo.getMemberInCharge() != null ?
                                    todo.getMemberInCharge().getMember().getNickname() : null)
                            .build())
                    .toList();

            GroupGoalTodoDTO groupGoalTodoDTO = GroupGoalTodoDTO.builder()
                    .title(groupGoal.getTitle())
                    .id(groupGoal.getId())
                    .updatedAt(groupGoal.getUpdatedAt())
                    .createdAt(groupGoal.getCreatedAt())
                    .todos(groupTodoDTOS)
                    .progress(groupProgressDTO)
                    .build();

            groupGoalTodoDTOS.add(groupGoalTodoDTO);
        }

        Long totalCount = groupGoalRepository.countByGroup(group);
        return new ResponseDTO<>(new GoalTodosResponseDTO<>(page,totalCount,groupGoalTodoDTOS),Responses.OK);
    }

    //그룹 목표 수정
    @Override
    @Transactional
    public ResponseDTO<GroupGoalDTO> updateGroupGoal(Long groupId, Long goalId, String title){
        individualGoalServiceImpl.checkTitleLength(title);
        Group group = groupRepository.findById(groupId).get();

        GroupGoal groupGoal = groupGoalRepository.findByGroupAndId(group,goalId);

        groupGoal.setTitle(title);
        GroupGoal saveGoal = groupGoalRepository.save(groupGoal);
        return new ResponseDTO<>(new GroupGoalDTO(saveGoal),Responses.OK);
    }

    //그룹 목표 삭제
    @Override
    @Transactional
    public ResponseDTO<?> deleteGroupGoal(Long groupId, Long goalId){
        Group group = groupRepository.findById(groupId).get();
        GroupGoal groupGoal = groupGoalRepository.findByGroupAndId(group,goalId);

        groupGoal.deleteGoal();
        groupGoalRepository.save(groupGoal);

        return new ResponseDTO<>(null,Responses.NO_CONTENT);
    }

    @Override
    public ResponseDTO<SingleGoalDTO> getSingleGroupGoal(Long goalId) {
        GroupGoal groupGoal = groupGoalRepository.findById(goalId).orElseThrow(() -> new CustomException(Exceptions.GOAL_NOT_FOUND));

        GroupMember groupMember = groupGoal.getGroupMember();
        Long memberId = (groupMember != null && groupMember.getMember() != null)
                ? groupMember.getMember().getId()
                : null;

        if (memberId == null) {
            throw new CustomException(Exceptions.MEMBER_NOT_FOUND); 
        }

        SingleGoalDTO singleGoalDTO = SingleGoalDTO.builder()
                .id(groupGoal.getId())
                .title(groupGoal.getTitle())
                .memberId(groupGoal.getGroupMember().getMember().getId())
                .createdAt(groupGoal.getCreatedAt())
                .updatedAt(groupGoal.getUpdatedAt())
                .progress(groupGoal.getProgressRate())
                .build();
        return new ResponseDTO<>(singleGoalDTO,Responses.OK);
    }

    public Integer calContributionPercent(List<GroupTodo> groupTodos, Long memberId){
        Integer totalDoneCount = groupTodos.size();
        Integer contributionCount = (int) groupTodos.stream()
//                .filter(todo -> todo.getIsDone() && Objects.equals(todo.getMemberInCharge().getMember().getId(), memberId))
                .filter(todo -> {
                    if(todo.getMemberInCharge() == null){
                        return false;
                    }
                    return todo.getIsDone() && todo.getMemberInCharge().getMember().getId().equals(memberId);
                })
                .count();

        if(totalDoneCount == 0){
            return 0;
        }

        return (contributionCount * 100) / totalDoneCount;

    }
}
