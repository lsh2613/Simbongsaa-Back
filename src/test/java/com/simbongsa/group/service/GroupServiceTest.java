package com.simbongsa.group.service;

import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.constant.GroupStatus;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.group.dto.req.GroupCreateReq;
import com.simbongsa.group.dto.req.GroupSearchReq;
import com.simbongsa.group.dto.req.GroupUpdateReq;
import com.simbongsa.group.dto.res.GroupRes;
import com.simbongsa.group.dto.res.GroupSearchRes;
import com.simbongsa.group.entity.Group;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class GroupServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private EntityFacade entityFacade;

    public Long memberId;

    @BeforeEach
    void memberSetup() {
        Member member = new Member(
                1L,
                "3231321",
                OauthProvider.GOOGLE,
                "test@gmail.com",
                "새싹개발자",
                22,
                null,
                Role.USER,
                "안녕하세요 개발자입니다.",
                0,
                null
        );
        Member saveMember = memberRepository.save(member);
        this.memberId = saveMember.getId();
    }

    Long createGroup() {
        GroupCreateReq groupCreateReq = GroupCreateReq.builder()
                .name("봉사별")
                .introduction("안녕하세요 봉사별 그룹입니다. 저희는 주로 연탄봉사를 다니면서 힘든 분들에게 따듯한 손길을 건내주고 있어요")
                .maxPeople(20)
                .groupStatus(GroupStatus.PUBLIC)
                .build();

        return groupService.createGroup(memberId, groupCreateReq);
    }

    @Test
    void 그룹_생성_성공() {
        //given
        GroupCreateReq groupCreateReq = GroupCreateReq.builder()
                .name("봉사별")
                .introduction("안녕하세요 봉사별 그룹입니다. 저희는 주로 연탄봉사를 다니면서 힘든 분들에게 따듯한 손길을 건내주고 있어요")
                .maxPeople(20)
                .groupStatus(GroupStatus.PUBLIC)
                .build();

        //when
        Long groupId = groupService.createGroup(memberId, groupCreateReq);
        Group group = entityFacade.getGroup(groupId);
        Member member = entityFacade.getMember(memberId);

        //then
        assertThat(group.getName()).isEqualTo("봉사별");
        assertThat(group.getIntroduction()).isEqualTo("안녕하세요 봉사별 그룹입니다. 저희는 주로 연탄봉사를 다니면서 힘든 분들에게 따듯한 손길을 건내주고 있어요");
        assertThat(group.getMaxPeople()).isEqualTo(20);
        assertThat(group.getGroupStatus()).isEqualTo(GroupStatus.PUBLIC);
        assertThat(group.getGroupLeader()).isEqualTo(member);
    }

    @Test
    void 그룹_생성_실패_유효성검증_실패() {
        //given
        GroupCreateReq groupCreateReq = GroupCreateReq.builder()
                .name("")
                .introduction(null)
                .maxPeople(null)
                .groupStatus(null)
                .build();

        //when, then
        assertThrows(DataIntegrityViolationException.class, () -> groupService.createGroup(memberId, groupCreateReq));
    }

    @Test
    void 그룹_상세조회_성공() {
        //given
        Long groupId = createGroup();

        //when
        GroupRes groupRes = groupService.getGroup(memberId, groupId);

        //then
        assertThat(groupRes.name()).isEqualTo("봉사별");
        assertThat(groupRes.introduction()).isEqualTo("안녕하세요 봉사별 그룹입니다. 저희는 주로 연탄봉사를 다니면서 힘든 분들에게 따듯한 손길을 건내주고 있어요");
        assertThat(groupRes.maxPeople()).isEqualTo(20);
        assertThat(groupRes.currentPeople()).isEqualTo(1);
        assertThat(groupRes.groupStatus()).isEqualTo(GroupStatus.PUBLIC);
        assertThat(groupRes.isHost()).isTrue();
    }

    @Test
    void 그룹_리스트조회_성공() {
        //given
        Long groupId = createGroup();
        GroupSearchReq groupSearchReq_01 = new GroupSearchReq("별");
        GroupSearchReq groupSearchReq_02 = new GroupSearchReq("이음");
        
        //when
        GroupSearchRes groupList_01 = groupService.getGroupList(memberId, PageRequest.of(0, 5), groupSearchReq_01);
        GroupSearchRes groupList_02 = groupService.getGroupList(memberId, PageRequest.of(0, 5), groupSearchReq_02);

        //then
        assertThat(groupList_01.groupList().size()).isEqualTo(1);
        assertThat(groupList_01.lastPage()).isEqualTo(0);
        assertThat(groupList_01.hasNext()).isFalse();

        assertThat(groupList_02.groupList().size()).isEqualTo(0);
        assertThat(groupList_02.lastPage()).isEqualTo(0);
        assertThat(groupList_02.hasNext()).isFalse();
    }

    @Test
    void 그룹_수정_성공() {
        //given
        Long groupId = createGroup();
        GroupUpdateReq groupUpdateReq = new GroupUpdateReq("이음별", "안녕하세요 이음별 그룹입니다.", 60, GroupStatus.PRIVATE);

        //when
        groupService.updateGroup(memberId, groupId, groupUpdateReq);
        Group group = entityFacade.getGroup(groupId);

        //then
        assertThat(group.getName()).isEqualTo("이음별");
        assertThat(group.getIntroduction()).isEqualTo("안녕하세요 이음별 그룹입니다.");
        assertThat(group.getMaxPeople()).isEqualTo(60);
        assertThat(group.getGroupStatus()).isEqualTo(GroupStatus.PRIVATE);
    }

    @Test
    void 그룹_수정_실패_방장아님() {
        //given
        Long groupId = createGroup();
        GroupUpdateReq groupUpdateReq = new GroupUpdateReq("이음별", null, null, null);

        //when, then
        assertThrows(GeneralHandler.class, () -> groupService.updateGroup(memberId + 10L, groupId, groupUpdateReq));
    }
    
    @Test
    void 그룹_삭제_성공() {
        //given
        Long groupId = createGroup();

        //when
        groupService.deleteGroup(memberId, groupId);

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getGroup(groupId));
    }

    @Test
    void 그룹_삭제_실패_방장아님() {
        //given
        Long groupId = createGroup();

        //when, then
        assertThrows(GeneralHandler.class, () -> groupService.deleteGroup(memberId+10L, groupId));
    }

}