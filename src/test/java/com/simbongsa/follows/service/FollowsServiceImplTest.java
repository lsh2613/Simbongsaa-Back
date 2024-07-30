package com.simbongsa.follows.service;

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

}
