package com.simbongsa.feed.service;

import com.simbongsa.feed.dto.req.FeedCreateReq;
import com.simbongsa.feed.dto.req.FeedUpdateReq;
import com.simbongsa.feed.dto.res.FeedPageRes;
import com.simbongsa.feed.dto.res.FeedRes;
import com.simbongsa.feed.dto.res.MyFeedRes;
import com.simbongsa.feed.entity.Feed;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.constant.OauthProvider;
import com.simbongsa.global.common.constant.Role;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class FeedServiceTest {
    @Autowired
    private EntityFacade entityFacade;
    @Autowired
    private FeedService feedService;
    @Autowired
    private MemberRepository memberRepository;
    public Long memberId_01;
    public Long memberId_02;

    @BeforeEach
    void memberSetup() {
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
                0,
                null
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
                0,
                null
        );
        Member saveMember_01 = memberRepository.save(member01);
        Member saveMember_02 = memberRepository.save(member02);
        this.memberId_01 = saveMember_01.getId();
        this.memberId_02 = saveMember_02.getId();
    }

    Long createFeed(Long memberId, String title, String body) {
        FeedCreateReq feedCreateReq = new FeedCreateReq(title, body, List.of("인증샷1.jpg", "인증샷2.jpg"));
        return feedService.createFeed(memberId, feedCreateReq);
    }
    @Test
    void 피드_생성_성공() {
        //given
        FeedCreateReq feedCreateReq = new FeedCreateReq("이음별 봉사 후기~", "안녕하세요 이음별 봉사활동 다녀온 후기입니다.", List.of("인증샷1.jpg", "인증샷2.jpg"));

        //when
        Long feedId = feedService.createFeed(memberId_01, feedCreateReq);

        //then
        Feed feed = entityFacade.getFeed(feedId);
        assertThat(feed.getId()).isNotNull();
        assertThat(feed.getTitle()).isEqualTo("이음별 봉사 후기~");
        assertThat(feed.getBody()).isEqualTo("안녕하세요 이음별 봉사활동 다녀온 후기입니다.");
        assertThat(feed.getImages().get(0).getFeed().getId()).isEqualTo(feedId);
        assertThat(feed.getImages().get(0).getUrl()).isEqualTo("인증샷1.jpg");
    }

    @Test
    void 피드_상세조회_성공() {
        //given
        Long feedId = createFeed(memberId_01, "이음별 봉사 후기~", "안녕하세요 이음별 봉사활동 다녀온 후기입니다.");

        //when
        FeedRes feed_01 = feedService.getFeed(memberId_01, feedId);
        FeedRes feed_02 = feedService.getFeed(memberId_01, feedId);

        //then
        assertThat(feed_01.id()).isNotNull();
        assertThat(feed_01.title()).isEqualTo("이음별 봉사 후기~");
        assertThat(feed_01.body()).isEqualTo("안녕하세요 이음별 봉사활동 다녀온 후기입니다.");
        assertThat(feed_01.views()).isEqualTo(0);
        assertThat(feed_02.views()).isEqualTo(1);
        assertThat(feed_01.likes()).isEqualTo(0);
    }

    @Test
    void 피드_리스트_조회_성공() throws InterruptedException {
        //given
        Long feed_01 = createFeed(memberId_02, "별", "북두칠성");
        Thread.sleep(1000);
        Long feed_02 = createFeed(memberId_02, "달", "달 탐사");

        //when
        FeedPageRes feeds_01 = feedService.getFeeds(memberId_02, null, PageRequest.of(0, 1));
        FeedPageRes feeds_02 = feedService.getFeeds(memberId_02, feeds_01.lastFeedId(), PageRequest.of(1, 1));

        //then
        assertThat(feeds_01.feedResList().size()).isEqualTo(1);
        assertThat(feeds_01.feedResList().get(0).title()).isEqualTo("달");
        assertThat(feeds_01.lastFeedId()).isEqualTo(feed_02);
        assertThat(feeds_01.hasNext()).isTrue();
        assertThat(feeds_02.feedResList().size()).isEqualTo(1);
        assertThat(feeds_02.feedResList().get(0).title()).isEqualTo("별");
        assertThat(feeds_02.lastFeedId()).isEqualTo(feed_01);
        assertThat(feeds_02.hasNext()).isFalse();

    }

    @Test
    void 피드_업데이트_성공() {
        //given
        Long feedId = createFeed(memberId_02, "별", "북두칠성");
        FeedUpdateReq feedUpdateReq = new FeedUpdateReq("달", "달을 잇다", List.of("인증샷2.jpg", "인증샷3.jpg", "인증샷4.jpg"));

        //when
        feedService.updateFeed(memberId_02, feedId, feedUpdateReq);

        //then
        Feed feed = entityFacade.getFeed(feedId);
        assertThat(feed.getTitle()).isEqualTo("달");
        assertThat(feed.getBody()).isEqualTo("달을 잇다");
        assertThat(feed.getImages().size()).isEqualTo(3);
        assertThat(feed.getImages().get(0).getUrl()).isEqualTo("인증샷2.jpg");
        assertThat(feed.getImages().get(1).getUrl()).isEqualTo("인증샷3.jpg");
        assertThat(feed.getImages().get(2).getUrl()).isEqualTo("인증샷4.jpg");
    }

    @Test
    void 피드_업데이트_실패_권한없음() {
        //given
        Long feedId = createFeed(memberId_02, "별", "북두칠성");
        FeedUpdateReq feedUpdateReq = new FeedUpdateReq("달", "달을 잇다", List.of("인증샷2.jpg", "인증샷3.jpg", "인증샷4.jpg"));

        //when, then
        assertThrows(GeneralHandler.class, () -> feedService.updateFeed(memberId_01, feedId, feedUpdateReq));
    }

    @Test
    void 피드_삭제_성공() {
        //given
        Long feedId = createFeed(memberId_01, "별", "별을 잇다");

        //when
        feedService.deleteFeed(memberId_01, feedId);

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getFeed(feedId));
    }

    @Test
    void 피드_삭제_실패_권한없음() {
        //given
        Long feedId = createFeed(memberId_01, "별", "별을 잇다");

        //when, then
        assertThrows(GeneralHandler.class, () -> feedService.deleteFeed(memberId_02, feedId));

    }

    @Test
    void 내피드_상세조회_성공() {
        //given
        Long feedId = createFeed(memberId_01, "별", "별을 잇다");

        //when
        MyFeedRes myFeed = feedService.getMyFeed(memberId_01, feedId);

        //then
        assertThat(myFeed.title()).isEqualTo("별");
        assertThat(myFeed.body()).isEqualTo("별을 잇다");
        assertThat(myFeed.isHost()).isTrue();
    }

    @Test
    void 내피드_리스트_조회_성공() throws InterruptedException {
        //given
        Long feed_01 = createFeed(memberId_02, "별", "북두칠성");
        Thread.sleep(1000);
        Long feed_02 = createFeed(memberId_02, "달", "달 탐사");

        //when
        FeedPageRes feeds_01 = feedService.getMyFeeds(memberId_02, null, PageRequest.of(0, 1));
        FeedPageRes feeds_02 = feedService.getMyFeeds(memberId_02, feeds_01.lastFeedId(), PageRequest.of(1, 1));

        //then
        assertThat(feeds_01.feedResList().size()).isEqualTo(1);
        assertThat(feeds_01.feedResList().get(0).title()).isEqualTo("달");
        assertThat(feeds_01.lastFeedId()).isEqualTo(feed_02);
        assertThat(feeds_01.hasNext()).isTrue();
        assertThat(feeds_02.feedResList().size()).isEqualTo(1);
        assertThat(feeds_02.feedResList().get(0).title()).isEqualTo("별");
        assertThat(feeds_02.lastFeedId()).isEqualTo(feed_01);
        assertThat(feeds_02.hasNext()).isFalse();
    }

    @Test
    void 피드_좋아요_성공() {
        //given
        Long feedId = createFeed(memberId_01, "별", "별을 잇다");

        //when
        feedService.likeFeed(feedId);

        //then
        FeedRes feed = feedService.getFeed(memberId_02, feedId);
        assertThat(feed.likes()).isEqualTo(1);
    }
}