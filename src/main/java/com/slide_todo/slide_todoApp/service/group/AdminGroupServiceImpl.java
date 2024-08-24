package com.slide_todo.slide_todoApp.service.group;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.dto.group.GroupInfoDTO;
import com.slide_todo.slide_todoApp.dto.group.GroupMemberDTO;
import com.slide_todo.slide_todoApp.dto.group.admin.GroupIdDTO;
import com.slide_todo.slide_todoApp.dto.group.admin.GroupInfoListDTO;
import com.slide_todo.slide_todoApp.dto.group.admin.GroupListDTO;
import com.slide_todo.slide_todoApp.repository.group.GroupMemberRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminGroupServiceImpl implements AdminGroupService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Override
    public ResponseDTO<GroupListDTO> getAllGroups(String nickname, String title, int page, int limit) {

        Specification<Group> groupSearchSpec = (root, query, criteriaBuilder) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            //그룹 이름으로 필터링하기
            if(title != null && !title.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("title"),title));
            }

            //그룹장 닉네임으로 필터링하기
            if(nickname != null && !nickname.isEmpty()){
                Member member = memberRepository.findByNickname(nickname);
                if(member != null){
                    Join<Group, GroupMember> groupMembers = root.join("groupMembers");
                    predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(groupMembers.get("member"),member),
                            criteriaBuilder.isTrue(groupMembers.get("isLeader"))
                    ));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };

        List<Group> groups;

        if(limit == 0){
            groups = groupRepository.findAll(groupSearchSpec);
        }
        else{
            PageRequest pageRequest = PageRequest.of(page - 1, limit);
            Page<Group> groupPage = groupRepository.findAll(groupSearchSpec, pageRequest);
            groups = groupPage.getContent(); // Page에서 리스트 가져오기
        }
        long totalCount = groupRepository.count();

        List<GroupInfoDTO> groupInfoDTOS = new ArrayList<>();

        for (Group group : groups) {
            // 그룹 멤버 리스트 가져오기
            List<GroupMember> groupMembers = group.getGroupMembers();

            // GroupInfoDTO 생성
            GroupInfoDTO groupInfoDTO = new GroupInfoDTO(group);

            // 순위를 계산하고 members 설정
            groupInfoDTO.calculateContributionRank(groupMembers);

            // GroupInfoDTO 추가
            groupInfoDTOS.add(groupInfoDTO);
        }

        GroupListDTO groupListDTO = GroupListDTO.builder()
                .totalCount(totalCount)
                .searchCount((long) groupInfoDTOS.size())
                .current_page(page)
                .groups(groupInfoDTOS)
                .build();

        return new ResponseDTO<>(groupListDTO, Responses.OK);
    }



    @Override
    @Transactional
    public ResponseDTO<?> deleteGroups(GroupIdDTO groupIdDTO) {
        List<Long> ids = groupIdDTO.getGroupIds();

        for(Long id : ids){
            Group group = groupRepository.findById(id).orElseThrow(() -> new CustomException(Exceptions.GROUP_NOT_FOUND));
            group.deleteGroup();
        }

        return new ResponseDTO<>("그룹 삭제 완료",Responses.NO_CONTENT);
    }

    @Override
    public ResponseDTO<GroupInfoListDTO> getGroupInfo(Long groupId) {
        Group group = groupRepository.findGroupWithGroupMembers(groupId);

        List<GroupInfoListDTO.GroupMemberDTO> groupMemberDTOS = new ArrayList<>();
        for(GroupMember groupMember : group.getGroupMembers()){
            groupMemberDTOS.add(new GroupInfoListDTO.GroupMemberDTO(groupMember));
        }

        List<GroupInfoListDTO.GoalDTO> groupGoalDTOS = new ArrayList<>();
        for(GroupGoal groupGoal : group.getGroupGoals()){
            groupGoalDTOS.add(new GroupInfoListDTO.GoalDTO(groupGoal));
        }


        GroupInfoListDTO groupInfoListDTO = GroupInfoListDTO.builder()
                .id(group.getId())
                .title(group.getTitle())
                .createUser(group.getCreatedGroupMember().getMember().getNickname())
                .secretCode(group.getSecretCode())
                .members(groupMemberDTOS)
                .goals(groupGoalDTOS)
                .build();

        return new ResponseDTO<>(groupInfoListDTO, Responses.OK);

    }

    @Override
    @Transactional
    public ResponseDTO<?> deleteMemeber(Long groupId, Long memberId) {
        GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(memberId,groupId);
        groupMember.deleteGroupMember();

        return new ResponseDTO<>("멤버 탈퇴 완료",Responses.OK);
    }

    @Override
    @Transactional
    public ResponseDTO<GroupInfoListDTO> updateGroupInfo(Long groupId, String title, String secretCode) {
       Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(Exceptions.GROUP_NOT_FOUND));

       if(title!=null && !title.isEmpty()){
           group.updateTitle(title);
       }
       if(secretCode!=null && !secretCode.isEmpty()){
           group.setSecretCode(secretCode);
       }

       Group newGroup = groupRepository.save(group);
       return getGroupInfo(newGroup.getId());
    }

    @Override
    @Transactional
    public ResponseDTO<?> changeLeader(Long groupId, Long memberId) {
        Group group = groupRepository.findGroupWithGroupMembers(groupId);

        // 현재 리더 찾기
        GroupMember currentLeader = group.getGroupMembers().stream()
                .filter(member -> member.getIsLeader().equals(true))
                .findFirst()
                .orElseThrow(()->new CustomException(Exceptions.MEMBER_NOT_FOUND));


        GroupMember newLeader = group.getGroupMembers().stream()
                .filter(member -> member.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow(()->new CustomException(Exceptions.MEMBER_NOT_FOUND));


        currentLeader.setIsLeader(false);
        newLeader.setIsLeader(true);

        group.setCreatedGroupMember(newLeader);

        groupMemberRepository.save(currentLeader);
        groupMemberRepository.save(newLeader);
        groupRepository.save(group);

        return new ResponseDTO<>("리더 변경 완료",Responses.OK);
    }




}
