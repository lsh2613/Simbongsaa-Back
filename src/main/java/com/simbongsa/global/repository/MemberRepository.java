package com.simbongsa.global.repository;

import com.simbongsa.global.constant.OauthProvider;
import com.simbongsa.global.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId);
}
