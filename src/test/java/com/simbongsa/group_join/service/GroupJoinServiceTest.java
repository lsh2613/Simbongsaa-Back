package com.simbongsa.group_join.service;

import com.simbongsa.global.common.constant.*;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.group.dto.req.GroupCreateReq;
import com.simbongsa.group.entity.Group;
import com.simbongsa.group.repository.GroupRepository;
import com.simbongsa.group.service.GroupService;
import com.simbongsa.group_join.dto.req.GroupJoinApplyReq;
import com.simbongsa.group_join.dto.req.GroupJoinDecideReq;
import com.simbongsa.group_join.dto.res.GroupJoinMyRes;
import com.simbongsa.group_join.dto.res.GroupJoinRes;
import com.simbongsa.group_join.entity.GroupJoin;
import com.simbongsa.group_join.repository.GroupJoinRepository;
import com.simbongsa.group_user.entity.GroupUser;
import com.simbongsa.group_user.repository.GroupUserRepository;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class GroupJoinServiceTest {
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupJoinService groupJoinService;
    @Autowired
    private GroupJoinRepository groupJoinRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupUserRepository groupUserRepository;

    public Long memberId_01;
    public Long memberId_02;
    public Long memberId_03;
    public Long groupId_01;
    public Long groupId_02;

    @BeforeEach
    void setup() {
        Member member01 = new Member(
                1L,
                "3231321",
                OauthProvider.GOOGLE,
                "test@gmail.com",
                "새싹개발자",
                22,
                null,
                Role.USER,
                "안녕하세요 개발자입니다.",
                0
        );

        Member member02 = new Member(
                2L,
                "123321123",
                OauthProvider.KAKAO,
                "test@kakao.com",
                "잔디개발자",
                30,
                null,
                Role.USER,
                "안녕하세요 백수입니다.",
                0
        );

        Member member03 = new Member(
                3L,
                "1231312",
                OauthProvider.APPLE,
                "test@apple.com",
                "네잎개발자",
                20,
                null,
                Role.USER,
                "안녕하세요 새내기입니다.",
                0
        );
        Member saveMember01 = memberRepository.save(member01);
        Member saveMember02 = memberRepository.save(member02);
        Member saveMember03 = memberRepository.save(member03);
        this.memberId_01 = saveMember01.getId();
        this.memberId_02 = saveMember02.getId();
        this.memberId_03 = saveMember03.getId();

        GroupCreateReq groupCreateReq_01 = GroupCreateReq.builder()
                .name("봉사별")
                .introduction("안녕하세요 봉사별 그룹입니다. 저희는 주로 연탄봉사를 다니면서 힘든 분들에게 따듯한 손길을 건내주고 있어요")
                .maxPeople(20)
                .groupStatus(GroupStatus.PUBLIC)
                .build();
        GroupCreateReq groupCreateReq_02 = GroupCreateReq.builder()
                .name("이음별")
                .introduction("안녕하세요 이음별 그룹입니다. 저희는 주로 어르신들을 보살피는 봉사를 하고 있어요")
                .maxPeople(60)
                .groupStatus(GroupStatus.PRIVATE)
                .build();
        this.groupId_01 = groupService.createGroup(memberId_01, groupCreateReq_01);
        this.groupId_02 = groupService.createGroup(memberId_01, groupCreateReq_02);
    }

    @Test
    void 참가_성공_PUBLIC_GROUP() {
        //given
        GroupJoinApplyReq groupJoinApplyReq = new GroupJoinApplyReq(groupId_01, JoinType.JOIN);

        //when
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq);

        //then
        List<GroupUser> all = groupUserRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getGroup().getGroupLeader().getId()).isEqualTo(memberId_01);
        assertThat(all.get(0).getGroup().getName()).isEqualTo("봉사별");
        assertThat(all.get(0).getMember().getId()).isEqualTo(memberId_02);
    }

    @Test
    void 참가_신청_성공_PRIVATE_GROUP() {
        //given
        GroupJoinApplyReq groupJoinApplyReq = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);

        //when
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq);

        //then
        List<GroupJoin> allGroupJoins = groupJoinRepository.findAll();
        List<GroupUser> allGroupUsers = groupUserRepository.findAll();
        assertThat(allGroupJoins.size()).isEqualTo(1);
        assertThat(allGroupJoins.get(0).getGroup().getId()).isEqualTo(groupId_02);
        assertThat(allGroupJoins.get(0).getGroup().getName()).isEqualTo("이음별");
        assertThat(allGroupJoins.get(0).getMember().getId()).isEqualTo(memberId_02);
        assertThat(allGroupUsers.size()).isEqualTo(0);
    }

    @Test
    void 참가_취소_성공() {
        //given
        GroupJoinApplyReq groupJoinApplyReq_01 = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq_01);

        //when
        GroupJoinApplyReq groupJoinApplyReq_02 = new GroupJoinApplyReq(groupId_02, JoinType.CANCEL);
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq_02);

        //then
        List<GroupJoin> all = groupJoinRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void 참가_신청_실패_풀방() {
        //given
        Group byId = groupRepository.findById(groupId_01).orElse(null);
        byId.setMaxPeople(1);

        GroupJoinApplyReq groupJoinApplyReq = new GroupJoinApplyReq(groupId_01, JoinType.JOIN);

        //when, then
        assertThrows(GeneralHandler.class, () -> groupJoinService.applyJoin(memberId_02, groupJoinApplyReq));
    }

    @Test
    void 참가_신청_실패_중복() {
        //given
        GroupJoinApplyReq groupJoinApplyReq_01 = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);
        GroupJoinApplyReq groupJoinApplyReq_02 = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);

        //when
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq_01);

        //then
        assertThrows(GeneralHandler.class, () -> groupJoinService.applyJoin(memberId_02, groupJoinApplyReq_02));
    }

    @Test
    void 참가_결정_성공() {
        //given
        GroupJoinApplyReq groupJoinApplyReq = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq);

        List<GroupJoinRes> groupJoinList = groupJoinService.getGroupJoinList(memberId_01, groupId_02);
        Long groupJoinId = groupJoinList.get(0).groupJoinId();

        GroupJoinDecideReq groupJoinDecideReq = new GroupJoinDecideReq(groupJoinId, JoinDecide.ACCEPT);

        //when
        groupJoinService.decideJoin(memberId_01, groupJoinDecideReq);

        //then
        Optional<GroupJoin> groupJoinById = groupJoinRepository.findById(groupJoinId);
        Optional<Group> groupById = groupRepository.findById(groupId_02);
        Optional<GroupUser> groupUserByGroupIdAndMemberId = groupUserRepository.findByGroup_IdAndMember_Id(groupById.get().getId(), memberId_02);

        assertThat(groupJoinById).isEmpty();
        assertThat(groupById.get().getCurrentPeople()).isEqualTo(2);
        assertThat(groupUserByGroupIdAndMemberId.get().getGroup().getName()).isEqualTo("이음별");
        assertThat(groupUserByGroupIdAndMemberId.get().getMember().getNickname()).isEqualTo("잔디개발자");
    }

    @Test
    void 참가_결정_실패_권한없음() {
        GroupJoinApplyReq groupJoinApplyReq = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq);

        List<GroupJoinRes> groupJoinList = groupJoinService.getGroupJoinList(memberId_01, groupId_02);
        Long groupJoinId = groupJoinList.get(0).groupJoinId();

        GroupJoinDecideReq groupJoinDecideReq = new GroupJoinDecideReq(groupJoinId, JoinDecide.ACCEPT);

        //when, then
        assertThrows(GeneralHandler.class, () -> groupJoinService.decideJoin(memberId_02, groupJoinDecideReq));

    }

    @Test
    void 지원_현황_성공() {
        //given
        GroupJoinApplyReq groupJoinApplyReq = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);

        //when
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq);
        groupJoinService.applyJoin(memberId_03, groupJoinApplyReq);

        //then
        List<GroupJoinRes> groupJoinList = groupJoinService.getGroupJoinList(memberId_01, groupId_02);
        assertThat(groupJoinList.size()).isEqualTo(2);
    }

    @Test
    void 지원자_현황_성공() {
        //given
        GroupJoinApplyReq groupJoinApplyReq = new GroupJoinApplyReq(groupId_02, JoinType.JOIN);

        //when
        groupJoinService.applyJoin(memberId_02, groupJoinApplyReq);

        //then
        List<GroupJoinMyRes> myGroupJoinList = groupJoinService.getMyGroupJoinList(memberId_02);
        assertThat(myGroupJoinList.size()).isEqualTo(1);
    }
}