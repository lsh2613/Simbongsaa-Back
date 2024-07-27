package com.simbongsa.follows.repository;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, Long> {

    Optional<Follows> findByFollowingMemberAndFollowedMember(Member followingMember, Member followedMember);
}
