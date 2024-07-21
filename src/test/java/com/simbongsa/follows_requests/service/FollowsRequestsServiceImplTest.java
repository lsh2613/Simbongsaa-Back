package com.simbongsa.follows_requests.service;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.follows_requests.dto.req.FollowsRequestsDecideReq;
import com.simbongsa.follows_requests.dto.res.FollowsRequestsRes;
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

    @Test
    void 내가_받은_팔로우_요청_조회() {
        //given
        Long followedMemberId = saveMember("test", "socialId", MemberStatus.PRIVATE);
        Long followingMemberId1 = saveMember("test1", "socialId1", MemberStatus.PUBLIC);
        Long followingMemberId2 = saveMember("test2", "socialId2", MemberStatus.PUBLIC);

        followsRequestsService.follow(followingMemberId1, followedMemberId);
        followsRequestsService.follow(followingMemberId2, followedMemberId);

        List<FollowsRequests> followsRequestsList = followsRequestRepository.findAllByFollowedMemberId(followedMemberId);
        FollowsRequests followsRequests1 = followsRequestsList.get(0);
        FollowsRequests followsRequests2 = followsRequestsList.get(1);

        //when
        List<FollowsRequestsRes> followsRequestsResList = followsRequestsService.getFollowsRequestsList(followedMemberId);

        //then
        FollowsRequestsRes followsRequestsRes1 = followsRequestsResList.get(0);
        FollowsRequestsRes followsRequestsRes2 = followsRequestsResList.get(1);

        assertThat(followsRequestsResList.size()).isEqualTo(2);
        assertThat(followsRequestsRes1.followsRequestId()).isEqualTo(followsRequests1.getId());
        assertThat(followsRequestsRes2.followsRequestId()).isEqualTo(followsRequests2.getId());
        assertThat(followsRequestsRes1.memberId()).isEqualTo(followingMemberId1);
        assertThat(followsRequestsRes2.memberId()).isEqualTo(followingMemberId2);
    }

    @Test
    void 내가_신청한_팔로우_요청_조회() {
        //given
        Long followingMemberId = saveMember("test", "socialId", MemberStatus.PUBLIC);
        Long followedMemberId1 = saveMember("test1", "socialId1", MemberStatus.PRIVATE);
        Long followedMemberId2 = saveMember("test2", "socialId2", MemberStatus.PRIVATE);

        followsRequestsService.follow(followingMemberId, followedMemberId1);
        followsRequestsService.follow(followingMemberId, followedMemberId2);

        List<FollowsRequests> followsRequestsList = followsRequestRepository.findAllByFollowingMemberId(followingMemberId);
        FollowsRequests followsRequests1 = followsRequestsList.get(0);
        FollowsRequests followsRequests2 = followsRequestsList.get(1);

        //when
        List<FollowsRequestsRes> myFollowsRequestsList = followsRequestsService.getMyFollowsRequestsList(followingMemberId);

        //then
        FollowsRequestsRes followsRequestsRes1 = myFollowsRequestsList.get(0);
        FollowsRequestsRes followsRequestsRes2 = myFollowsRequestsList.get(1);

        assertThat(myFollowsRequestsList.size()).isEqualTo(2);
        assertThat(followsRequestsRes1.followsRequestId()).isEqualTo(followsRequests1.getId());
        assertThat(followsRequestsRes2.followsRequestId()).isEqualTo(followsRequests2.getId());
        assertThat(followsRequestsRes1.memberId()).isEqualTo(followedMemberId1);
        assertThat(followsRequestsRes2.memberId()).isEqualTo(followedMemberId2);
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
