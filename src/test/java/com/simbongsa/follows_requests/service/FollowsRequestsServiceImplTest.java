package com.simbongsa.follows_requests.service;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.dto.req.FollowsRequestsDecideReq;
import com.simbongsa.follows_requests.entity.FollowsRequests;
import com.simbongsa.follows_requests.repository.FollowsRequestRepository;
import com.simbongsa.global.common.constant.FollowsRequestsDecide;
import com.simbongsa.global.common.constant.MemberStatus;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    void 팔로우_취소() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PUBLIC);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        //when
        followsRequestsService.follow(followingMemberId, followedMemberId);

        //then
        List<Follows> allFollows = followsRepository.findAll();
        List<FollowsRequests> allFollowsRequests = followsRequestRepository.findAll();

        assertThat(allFollows.size()).isEqualTo(0);
        assertThat(allFollowsRequests.size()).isEqualTo(0);
    }

    @Test
    void 팔로우_요청_수락() {
        //given
        Long followingMemberId = saveMember("test1", MemberStatus.PUBLIC);
        Long followedMemberId = saveMember("test2", MemberStatus.PRIVATE);
        followsRequestsService.follow(followingMemberId, followedMemberId);

        Long followsRequestsId = followsRequestRepository.findAll().get(0).getId();
        FollowsRequestsDecideReq followsRequestsDecideReq = new FollowsRequestsDecideReq(followsRequestsId, FollowsRequestsDecide.ACCEPT);

        //when
        followsRequestsService.decideRequests(followedMemberId, followsRequestsDecideReq);

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
        FollowsRequestsDecideReq followsRequestsDecideReq = new FollowsRequestsDecideReq(followsRequestsId, FollowsRequestsDecide.REJECT);

        //when
        followsRequestsService.decideRequests(followedMemberId, followsRequestsDecideReq);

        //then
        List<FollowsRequests> allFollowsRequests = followsRequestRepository.findAll();

        List<Follows> allFollows = followsRepository.findAll();

        assertThat(allFollowsRequests.size()).isEqualTo(0);
        assertThat(allFollows.size()).isEqualTo(0);
    }

}
