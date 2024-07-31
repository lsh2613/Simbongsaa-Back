package com.simbongsa.follows.service;

import com.simbongsa.follows.dto.res.FollowsPageRes;
import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.service.FollowsRequestsService;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class FollowsServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(FollowsServiceImplTest.class);
    @Autowired
    private FollowsService followsService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FollowsRequestsService followsRequestsService;
    @Autowired
    private FollowsRepository followsRepository;

    Long saveMember(String socialId, MemberStatus memberStatus) {
        Member saveMember = memberRepository.save(new Member(
                socialId,
                OauthProvider.GOOGLE,
                "test@gmail.com",
                "test",
                20,
                null,
                Role.USER,
                "안녕하세요 개발자입니다.",
                0,
                memberStatus
        ));
        return saveMember.getId();
    }

    @Test
    void 내가_신청한_팔로우_삭제_성공() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PUBLIC);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        List<Follows> all = followsRepository.findAll();
        Follows follows = all.get(0);

        //when
        followsService.deleteMyFollows(followingMemberId, follows.getId());

        //then
        assertThat(followsRepository.findAll()).hasSize(0);
    }

    @Test
    void 내가_신청한_팔로우_삭제_실패() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PUBLIC);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        List<Follows> all = followsRepository.findAll();
        Follows follows = all.get(0);

        //when - then
        assertThrows(GeneralHandler.class, () -> followsService.deleteMyFollows(followedMemberId, follows.getId()));
    }

    @Test
    void 상대방_팔로우_삭제_성공() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PUBLIC);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        List<Follows> all = followsRepository.findAll();
        Follows follows = all.get(0);

        //when
        followsService.deleteOppositeFollows(followedMemberId, follows.getId());

        //then
        assertThat(followsRepository.findAll()).hasSize(0);
    }

    @Test
    void 상대방_팔로우_삭제_실패() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PUBLIC);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        List<Follows> all = followsRepository.findAll();
        Follows follows = all.get(0);

        //when - then
        assertThrows(GeneralHandler.class, () -> followsService.deleteOppositeFollows(followingMemberId, follows.getId()));
    }

    @Test
    void 나의_팔로잉_조회(){
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId_01 = saveMember("test2", MemberStatus.PUBLIC);
        Long followedMemberId_02 = saveMember("test3", MemberStatus.PUBLIC);

        followsRequestsService.follow(followingMemberId, followedMemberId_01);
        followsRequestsService.follow(followingMemberId, followedMemberId_02);

        //when
        FollowsPageRes myFollowsPage_01 = followsService.getMyFollowingPage(
                followingMemberId,
                null,
                PageRequest.of(0, 1)
        );
        FollowsPageRes myFollowsPage_02 = followsService.getMyFollowingPage(
                followingMemberId,
                myFollowsPage_01.lastFollowsRequestId(),
                PageRequest.of(1, 1)
        );

        //then
        assertThat(myFollowsPage_01.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_01.followsResList().get(0).memberId()).isEqualTo(followedMemberId_02);
        assertThat(myFollowsPage_01.hasNext()).isTrue();
        assertThat(myFollowsPage_02.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_02.followsResList().get(0).memberId()).isEqualTo(followedMemberId_01);
        assertThat(myFollowsPage_02.hasNext()).isFalse();
    }

    @Test
    void 타유저의_팔로잉_조회_성공(){
        //given
        Long loginId = saveMember("test1", MemberStatus.PUBLIC);
        Long followingMemberId = saveMember("test2", MemberStatus.PUBLIC);
        Long followedMemberId_01 = saveMember("test3", MemberStatus.PUBLIC);
        Long followedMemberId_02 = saveMember("test4", MemberStatus.PUBLIC);

        followsRequestsService.follow(loginId, followingMemberId);
        followsRequestsService.follow(followingMemberId, followedMemberId_01);
        followsRequestsService.follow(followingMemberId, followedMemberId_02);

        Member followingMember = memberRepository.findById(followingMemberId).get();
        followingMember.setMemberStatus(MemberStatus.PRIVATE);
        memberRepository.save(followingMember);

        //when
        FollowsPageRes memberFollowsPage_01 = followsService.getMemberFollowingPage(
                loginId,
                followingMemberId,
                null,
                PageRequest.of(0, 1)
        );
        FollowsPageRes memberFollowsPage_02 = followsService.getMemberFollowingPage(
                loginId,
                followingMemberId,
                memberFollowsPage_01.lastFollowsRequestId(),
                PageRequest.of(1, 1)
        );

        //then
        assertThat(memberFollowsPage_01.followsResList().size()).isEqualTo(1);
        assertThat(memberFollowsPage_01.followsResList().get(0).memberId()).isEqualTo(followedMemberId_02);
        assertThat(memberFollowsPage_01.hasNext()).isTrue();
        assertThat(memberFollowsPage_02.followsResList().size()).isEqualTo(1);
        assertThat(memberFollowsPage_02.followsResList().get(0).memberId()).isEqualTo(followedMemberId_01);
        assertThat(memberFollowsPage_02.hasNext()).isFalse();
    }

    @Test
    void 타유저의_팔로잉_조회_실패(){
        //given
        Long loginId = saveMember("test1", MemberStatus.PUBLIC);
        Long followingMemberId = saveMember("test2", MemberStatus.PRIVATE);
        Long followedMemberId_01 = saveMember("test3", MemberStatus.PUBLIC);
        Long followedMemberId_02 = saveMember("test4", MemberStatus.PUBLIC);

        followsRequestsService.follow(followingMemberId, followedMemberId_01);
        followsRequestsService.follow(followingMemberId, followedMemberId_02);

        // when-then
        assertThrows(GeneralHandler.class, () -> followsService.getMemberFollowingPage(
                loginId,
                followingMemberId,
                null,
                PageRequest.of(0, 1)
        ));
    }

    @Test
    void 나의_팔로워_조회(){
        //given
        Long followingMemberId_01 = saveMember("test1", MemberStatus.PUBLIC);
        Long followingMemberId_02 = saveMember("test2", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test3", MemberStatus.PUBLIC);

        followsRequestsService.follow(followingMemberId_01, followedMemberId);
        followsRequestsService.follow(followingMemberId_02, followedMemberId);

        //when
        FollowsPageRes myFollowsPage_01 = followsService.getMyFollowerPage(
                followedMemberId,
                null,
                PageRequest.of(0, 1)
        );
        FollowsPageRes myFollowsPage_02 = followsService.getMyFollowerPage(
                followedMemberId,
                myFollowsPage_01.lastFollowsRequestId(),
                PageRequest.of(1, 1)
        );

        //then
        assertThat(myFollowsPage_01.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_01.followsResList().get(0).memberId()).isEqualTo(followingMemberId_02);
        assertThat(myFollowsPage_01.hasNext()).isTrue();
        assertThat(myFollowsPage_02.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_02.followsResList().get(0).memberId()).isEqualTo(followingMemberId_01);
        assertThat(myFollowsPage_02.hasNext()).isFalse();
    }

    @Test
    void 타유저의_팔로워_조회_성공_with_public(){
        //given
        Long loginId = saveMember("test1", MemberStatus.PUBLIC);
        Long followingMemberId = saveMember("test2", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test3", MemberStatus.PUBLIC);

        followsRequestsService.follow(loginId, followedMemberId);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        //when
        FollowsPageRes myFollowsPage_01 = followsService.getMemberFollowerPage(
                loginId,
                followedMemberId,
                null,
                PageRequest.of(0, 1)
        );
        FollowsPageRes myFollowsPage_02 = followsService.getMemberFollowerPage(
                loginId,
                followedMemberId,
                myFollowsPage_01.lastFollowsRequestId(),
                PageRequest.of(1, 1)
        );

        //then
        assertThat(myFollowsPage_01.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_01.followsResList().get(0).memberId()).isEqualTo(followingMemberId);
        assertThat(myFollowsPage_01.hasNext()).isTrue();
        assertThat(myFollowsPage_02.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_02.followsResList().get(0).memberId()).isEqualTo(loginId);
        assertThat(myFollowsPage_02.hasNext()).isFalse();
    }

    @Test
    void 타유저의_팔로워_조회_성공_with_private(){
        //given
        Long loginId = saveMember("test1", MemberStatus.PUBLIC);
        Long followingMemberId = saveMember("test2", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test3", MemberStatus.PUBLIC);

        followsRequestsService.follow(loginId, followedMemberId);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        Member followedMember = memberRepository.findById(followedMemberId).get();
        followedMember.setMemberStatus(MemberStatus.PRIVATE);
        memberRepository.save(followedMember);

        //when
        FollowsPageRes myFollowsPage_01 = followsService.getMemberFollowerPage(
                loginId,
                followedMemberId,
                null,
                PageRequest.of(0, 1)
        );
        FollowsPageRes myFollowsPage_02 = followsService.getMemberFollowerPage(
                loginId,
                followedMemberId,
                myFollowsPage_01.lastFollowsRequestId(),
                PageRequest.of(1, 1)
        );

        //then
        assertThat(myFollowsPage_01.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_01.followsResList().get(0).memberId()).isEqualTo(followingMemberId);
        assertThat(myFollowsPage_01.hasNext()).isTrue();
        assertThat(myFollowsPage_02.followsResList().size()).isEqualTo(1);
        assertThat(myFollowsPage_02.followsResList().get(0).memberId()).isEqualTo(loginId);
        assertThat(myFollowsPage_02.hasNext()).isFalse();
    }

    @Test
    void 타유저의_팔로워_조회_실패(){
        //given
        Long loginId = saveMember("test1", MemberStatus.PUBLIC);
        Long followingMemberId_01 = saveMember("test2", MemberStatus.PUBLIC);
        Long followingMemberId_02 = saveMember("test3", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test4", MemberStatus.PRIVATE);

        followsRequestsService.follow(followingMemberId_01, followedMemberId);
        followsRequestsService.follow(followingMemberId_02, followedMemberId);

        // when-then
        assertThrows(GeneralHandler.class, () -> followsService.getMemberFollowerPage(
                loginId,
                followedMemberId,
                null,
                PageRequest.of(0, 1)
        ));
    }
}
