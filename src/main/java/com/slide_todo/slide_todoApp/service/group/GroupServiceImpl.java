package com.slide_todo.slide_todoApp.service.group;


import com.slide_todo.slide_todoApp.domain.group.ColorEnum;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.dto.group.*;
import com.slide_todo.slide_todoApp.repository.group.GroupMemberRepository;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    //그룹 생성하기
    @Override
    @Transactional
    public ResponseDTO<GroupResponseDTO> createGroup(String title, Long memberId){

        //그룹 이름 중복 확인
        if(groupRepository.findByTitle(title).isPresent()){
            throw new CustomException(Exceptions.GROUP_ALREADY_EXISTS);
        }

        Integer secretCode = generateRandomNumber();
        GroupCreateDTO groupCreateDTO = GroupCreateDTO.builder()
                .title(title)
                .secretCode(String.valueOf(secretCode))
                .build();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(Exceptions.MEMBER_NOT_FOUND));

        Group group = new Group(groupCreateDTO,member);
        groupRepository.save(group);

        GroupResponseDTO responseDTO = new GroupResponseDTO(group); //그룹 테이블에 정보 저장

//        saveGroupMemInfo(group,member,true);
        return new ResponseDTO<>(responseDTO,Responses.CREATED);
    }

    //그룹 참여하기
    @Override
    @Transactional
    public ResponseDTO<GroupResponseDTO> joinGroup(String secretCode, Long memberId){

        //초대코드 존재 확인
        Group group = groupRepository.findBySecretCode(secretCode)
                .orElseThrow(() -> new CustomException(Exceptions.GROUP_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(Exceptions.MEMBER_NOT_FOUND));

        List<GroupMember> groupMembers = group.getGroupMembers();
        for (GroupMember gm : groupMembers) {
            if (gm.getMember().equals(member)) {
                throw new CustomException(Exceptions.ALREADY_JOINED_GROUP);
            }
        }

        saveGroupMemInfo(group,member,false);
        GroupResponseDTO responseDTO = new GroupResponseDTO(group);

        return new ResponseDTO<>(responseDTO,Responses.OK);

    }

    //그룹 리스트 조회
    @Override
    public ResponseDTO<List<GroupDTO>> getGroupList(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(Exceptions.MEMBER_NOT_FOUND));

        List<GroupMember> groupMembers = groupMemberRepository.findByMember(member);

        List<GroupDTO> groupDTOS =  groupMembers.stream()
                .map(gm -> new GroupDTO(gm.getGroup().getId(), gm.getGroup().getTitle()))
                .collect(Collectors.toList());

        return new ResponseDTO<>(groupDTOS,Responses.OK);
    }

    //그룹 상세 조회
    @Override
    public ResponseDTO<GroupInfoDTO> getGroupInfo(Long groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(Exceptions.GROUP_NOT_FOUND));
        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);

        GroupInfoDTO groupInfoDTO = new GroupInfoDTO(group);

        groupInfoDTO.calculateContributionRank(groupMembers);
        return new ResponseDTO<>(groupInfoDTO, Responses.OK);
    }

    //그룹 삭제하기
    @Override
    @Transactional
    public ResponseDTO<?> deleteGroup(Long groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(Exceptions.GROUP_NOT_FOUND));
        group.deleteGroup();
        return new ResponseDTO<>("그룹 삭제 성공", Responses.NO_CONTENT);
    }

    //그룹 탈퇴
    @Override
    @Transactional
    public ResponseDTO<?> leaveGroup(Long groupId, Long memberId){

        GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(memberId,groupId);
        if(groupMember.getIsLeader().equals(true)){
            throw new CustomException(Exceptions.GROUP_MEMBER_ONLY);
        }
        groupMember.deleteGroupMember();
        return new ResponseDTO<>("그룹 탈퇴 성공", Responses.OK);
    }

    //새로운 초대코드 발급
    @Override
    public ResponseDTO<GroupCodeDTO> getNewSecretCode(Long groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(Exceptions.GROUP_NOT_FOUND));
        int newCode = generateRandomNumber();

        GroupCodeDTO groupCodeDTO = GroupCodeDTO.builder()
                .secretCode(String.valueOf(newCode))
                .build();

        return new ResponseDTO<>(groupCodeDTO,Responses.OK);
    }

    //새로운 초대코드 저장
    @Override
    @Transactional
    public ResponseDTO<GroupInfoDTO> saveNewSecretCode(Long groupId, String secretCode) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(Exceptions.GROUP_NOT_FOUND));

        group.setSecretCode(secretCode);
        groupRepository.save(group);

        return getGroupInfo(groupId);
    }

    @Override
    @Transactional
    public ResponseDTO<GroupInfoDTO> updateGroup(Long groupId, String title, String secretCode) {
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
    public ResponseDTO<?> deleteMember(Long groupId, Long memberId, Long leaderId) {
        //요청한 사람이 리더인지 확인
        GroupMember groupMember = groupMemberRepository.findByMemberIdAndGroupId(leaderId,groupId);
        if(groupMember.getIsLeader().equals(false)){
            throw new CustomException(Exceptions.GROUP_LEADER_ONLY);
        }

        GroupMember member = groupMemberRepository.findByMemberIdAndGroupId(memberId,groupId);
        member.deleteGroupMember();

        return new ResponseDTO<>("멤버 탈퇴 완료",Responses.OK);
    }

    @Override
    @Transactional
    public ResponseDTO<?> changeLeader(Long groupId, Long memberId, Long leaderId) {
        //요청한 사람이 리더인지 확인
        GroupMember currentLeader = groupMemberRepository.findByMemberIdAndGroupId(leaderId,groupId);
        if(currentLeader.getIsLeader().equals(false)){
            throw new CustomException(Exceptions.GROUP_LEADER_ONLY);
        }

        Group group = groupRepository.findGroupWithGroupMembers(groupId);
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

    //6자리 랜덤 숫자 생성
    public int generateRandomNumber() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    //그룹 멤버 저장
    @Transactional
    public void saveGroupMemInfo(Group group, Member member, Boolean isLeader) {

        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);
        Set<String> existColors = groupMembers.stream()
                .map(groupMember -> groupMember.getColor().getHexCode())
                .collect(Collectors.toSet());

        //사용 가능한 색 필터링
        List<ColorEnum> availabeColors = Arrays.stream(ColorEnum.values())
                .filter(color -> !existColors.contains(color.getHexCode()))
                .collect(Collectors.toList());

        Random random = new Random();
        ColorEnum randomColor = availabeColors.get(random.nextInt(availabeColors.size()));

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setMember(member);
        groupMember.setIsLeader(isLeader);
        groupMember.setTodoCount(0);
        groupMember.setColor(randomColor);
        groupMemberRepository.save(groupMember);
    }
}
