package com.slide_todo.slide_todoApp.service.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.dto.goal.GoalTodosResponseDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalTodoDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupProgressDTO;
import com.slide_todo.slide_todoApp.repository.goal.GroupGoalRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupMemberRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupGoalServiceImpl implements GroupGoalService {

    private final TodoRepository todoRepository;
    IndividualGoalService individualGoalService;
    GroupRepository groupRepository;
    GroupGoalRepository groupGoalRepository;
    GroupMemberRepository groupMemberRepository;

    public GroupGoalServiceImpl(IndividualGoalService individualGoalService, GroupRepository groupRepository, GroupGoalRepository groupGoalRepository, GroupMemberRepository groupMemberRepository, TodoRepository todoRepository) {
        this.individualGoalService = individualGoalService;
        this.groupRepository = groupRepository;
        this.groupGoalRepository = groupGoalRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.todoRepository = todoRepository;
    }

    //그룹 목표 생성
    public ResponseDTO<GroupGoalDTO> createGroupGoal(Long groupId,String title){
        individualGoalService.checkTitleLength(title);

        Group group = groupRepository.findById(groupId).get();

        GroupGoal groupGoal = GroupGoal.builder()
                .title(title)
                .group(group)
                .build();

        groupGoalRepository.save(groupGoal);
        GroupGoalDTO groupGoalDTO = new GroupGoalDTO(groupGoal);
        return new ResponseDTO<>(groupGoalDTO, Responses.CREATED);
    }

    //그룹 목표 리스트 조회
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
    public ResponseDTO<GoalTodosResponseDTO<GroupGoalTodoDTO>> getGroupGoalTodos(Long groupId,Long cursor,Integer limit){
        List<GroupGoal> groupGoals;
        Group group = groupRepository.findById(groupId).get();
        if (limit == 0) {
            groupGoals = groupGoalRepository.findAllByGroup(group);
        } else {
            groupGoals = groupGoalRepository.findAllByGroupAndIdGreaterThan(group, cursor, PageRequest.of(0,limit));
        }

        List<GroupGoalTodoDTO> groupGoalTodoDTOS = new ArrayList<>();

        List<GroupMember> groupMembers = groupMemberRepository.findAllByGroupId(groupId);

        for(GroupGoal groupGoal : groupGoals){
            List<GroupTodo> groupTodos = todoRepository.findAllByGoal(groupGoal);

            List<GroupProgressDTO.GroupGoalMemDTO> groupGoalMemDTOS = groupMembers.stream()
                    .map(member -> {
                        Integer contributionPercent = calContributionPercent(groupTodos, member.getId());

                        return GroupProgressDTO.GroupGoalMemDTO.builder()
                                .name(member.getMember().getNickname())
                                .contributionPercent(contributionPercent)
                                .color(member.getColor().getHexCode())
                                .build();
                    })
                    .toList();

            GroupProgressDTO groupProgressDTO = GroupProgressDTO.builder()
                    .completedPercent(groupGoal.getProgressRate())
                    .memebers(groupGoalMemDTOS)
                    .build();

            List<GroupGoalTodoDTO.GroupTodoDTO> groupTodoDTOS = groupTodos.stream()
                    .map(todo -> GroupGoalTodoDTO.GroupTodoDTO.builder()
                            .noteId(todo.getNote()!= null ? todo.getNote().getId() : null)
                            .done(todo.getIsDone())
                            .title(todo.getTitle())
                            .id(todo.getId())
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

        Long nextCursor = groupGoals.isEmpty() ? null : groupGoals.get(groupGoals.size() - 1).getId();
        Long totalCount = groupGoalRepository.countByGroup(group);
        return new ResponseDTO<>(new GoalTodosResponseDTO<>(nextCursor,totalCount,groupGoalTodoDTOS),Responses.OK);
    }

    //그룹 목표 수정
    public ResponseDTO<GroupGoalDTO> updateGroupGoal(Long groupId, Long goalId, String title){
        individualGoalService.checkTitleLength(title);
        Group group = groupRepository.findById(groupId).get();

        GroupGoal groupGoal = groupGoalRepository.findByGroupAndId(group,goalId);

        groupGoal.setTitle(title);
        GroupGoal saveGoal = groupGoalRepository.save(groupGoal);
        return new ResponseDTO<>(new GroupGoalDTO(saveGoal),Responses.OK);
    }

    //그룹 목표 삭제
    public ResponseDTO<?> deleteGroupGoal(Long groupId, Long goalId){
        Group group = groupRepository.findById(groupId).get();
        GroupGoal groupGoal = groupGoalRepository.findByGroupAndId(group,goalId);

        groupGoal.setIsDeleted(true);
        groupGoalRepository.save(groupGoal);

        return new ResponseDTO<>(null,Responses.NO_CONTENT);
    }


    public Integer calContributionPercent(List<GroupTodo> groupTodos, Long memberId){
        Integer totalDoneCount = groupTodos.size();
        Integer contributionCount = (int) groupTodos.stream()
                .filter(todo -> todo.getIsDone() && Objects.equals(todo.getGroupMember().getMember().getId(), memberId))
                .count();

        if(totalDoneCount == 0){
            return 0;
        }

        return (contributionCount * 100) / totalDoneCount;

    }
}
