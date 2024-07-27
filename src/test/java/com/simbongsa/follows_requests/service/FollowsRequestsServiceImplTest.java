package com.simbongsa.follows_requests.service;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.dto.res.FollowsRequestsPageRes;
import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.follows_requests.repository.FollowsRequestRepository;
import com.simbongsa.global.common.constant.FollowsRequestsDecide;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class FollowsRequestsServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FollowsRequestsService followsRequestsService;
    @Autowired
    private FollowsRepository followsRepository;
    @Autowired
    private FollowsRequestRepository followsRequestRepository;

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

    Long saveMember(String nickname, String socialId, MemberStatus memberStatus) {
        Member saveMember = memberRepository.save(new Member(
                socialId,
                OauthProvider.GOOGLE,
                "test@gmail.com",
                nickname,
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
    void 팔로우() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PUBLIC);

        //when
        followsRequestsService.follow(followingMemberId, followedMemberId);

        //then
        List<Follows> allFollows = followsRepository.findAll();

        Follows follows = allFollows.get(0);

        assertThat(allFollows.size()).isEqualTo(1);
        assertThat(follows.getFollowingMember().getId()).isEqualTo(followingMemberId);
        assertThat(follows.getFollowedMember().getId()).isEqualTo(followedMemberId);
    }

    @Test
    void 팔로우_요청() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PRIVATE);

        //when
        followsRequestsService.follow(followingMemberId, followedMemberId);

        //then
        List<FollowsRequests> allFollowsRequests = followsRequestRepository.findAll();

        FollowsRequests followsRequests = allFollowsRequests.get(0);

        assertThat(allFollowsRequests.size()).isEqualTo(1);
        assertThat(followsRequests.getFollowingMember().getId()).isEqualTo(followingMemberId);
        assertThat(followsRequests.getFollowedMember().getId()).isEqualTo(followedMemberId);
    }

    @Test
    void 팔로우_요청_수락() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PRIVATE);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        Long followsRequestsId = followsRequestRepository.findAll().get(0).getId();

        //when
        followsRequestsService.decideRequests(followedMemberId, followsRequestsId, FollowsRequestsDecide.ACCEPT);

        //then
        List<FollowsRequests> allFollowsRequests = followsRequestRepository.findAll();

        List<Follows> allFollows = followsRepository.findAll();
        Follows follows = allFollows.get(0);

        Member followingMember = follows.getFollowingMember();
        Member followedMember = follows.getFollowedMember();

        assertThat(allFollowsRequests.size()).isEqualTo(0);
        assertThat(allFollows.size()).isEqualTo(1);
        assertThat(followingMember.getId()).isEqualTo(followingMemberId);
        assertThat(followedMember.getId()).isEqualTo(followedMemberId);
    }

    @Test
    void 팔로우_요청_거절() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PRIVATE);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        Long followsRequestsId = followsRequestRepository.findAll().get(0).getId();

        //when
        followsRequestsService.decideRequests(followedMemberId, followsRequestsId, FollowsRequestsDecide.REJECT);

        //then
        List<FollowsRequests> allFollowsRequests = followsRequestRepository.findAll();

        List<Follows> allFollows = followsRepository.findAll();

        assertThat(allFollowsRequests.size()).isEqualTo(0);
        assertThat(allFollows.size()).isEqualTo(0);
    }

    @Test
    void 내가_받은_팔로우_요청_조회() {
        //given
        Long followingMemberId_01 = saveMember("following_01", "socialId1", MemberStatus.PUBLIC);
        Long followingMemberId_02 = saveMember("following_02", "socialId2", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("followed", "socialId3", MemberStatus.PRIVATE);

        followsRequestsService.follow(followingMemberId_01, followedMemberId);
        followsRequestsService.follow(followingMemberId_02, followedMemberId);

        //when
        FollowsRequestsPageRes followsRequestsPage_01 = followsRequestsService.getReceivedFollowsRequestsPage(
                followedMemberId,
                null,
                PageRequest.of(0, 1)
        );
        FollowsRequestsPageRes followsRequestsPage_02 = followsRequestsService.getReceivedFollowsRequestsPage(
                followedMemberId,
                followsRequestsPage_01.lastFollowsRequestId(),
                PageRequest.of(1, 1)
        );

        //then
        assertThat(followsRequestsPage_01.followsRequestsResList().size()).isEqualTo(1);
        assertThat(followsRequestsPage_01.followsRequestsResList().get(0).nickname()).isEqualTo("following_02");
        assertThat(followsRequestsPage_01.hasNext()).isTrue();
        assertThat(followsRequestsPage_02.followsRequestsResList().size()).isEqualTo(1);
        assertThat(followsRequestsPage_02.followsRequestsResList().get(0).nickname()).isEqualTo("following_01");
        assertThat(followsRequestsPage_02.hasNext()).isFalse();
    }

    @Test
    void 내가_신청한_팔로우_요청_조회() {
        //given
        Long followingMemberId = saveMember("following", "socialId1", MemberStatus.PUBLIC);
        Long followedMemberId_01 = saveMember("followed1", "socialId2", MemberStatus.PRIVATE);
        Long followedMemberId_02 = saveMember("followed2", "socialId3", MemberStatus.PRIVATE);

        followsRequestsService.follow(followingMemberId, followedMemberId_01);
        followsRequestsService.follow(followingMemberId, followedMemberId_02);

        //when
        FollowsRequestsPageRes followsRequestsPage_01 = followsRequestsService.getSentFollowsRequestsPage(
                followingMemberId,
                null,
                PageRequest.of(0, 1)
        );
        FollowsRequestsPageRes followsRequestsPage_02 = followsRequestsService.getSentFollowsRequestsPage(
                followingMemberId,
                followsRequestsPage_01.lastFollowsRequestId(),
                PageRequest.of(1, 1)
        );

        //then
        assertThat(followsRequestsPage_01.followsRequestsResList().size()).isEqualTo(1);
        assertThat(followsRequestsPage_01.followsRequestsResList().get(0).nickname()).isEqualTo("followed2");
        assertThat(followsRequestsPage_01.hasNext()).isTrue();
        assertThat(followsRequestsPage_02.followsRequestsResList().size()).isEqualTo(1);
        assertThat(followsRequestsPage_02.followsRequestsResList().get(0).nickname()).isEqualTo("followed1");
        assertThat(followsRequestsPage_02.hasNext()).isFalse();
    }

    @Test
    void 팔로우_요청_삭제() {
        //given
        Long followingMemberId = saveMember("test1", "socialId1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", "socialId2", MemberStatus.PRIVATE);

        followsRequestsService.follow(followingMemberId, followedMemberId);
        FollowsRequests followsRequests = followsRequestRepository.findAll().get(0);

        //when
        followsRequestsService.delete(followingMemberId, followsRequests.getId());

        //then
        List<FollowsRequests> all = followsRequestRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }
}
