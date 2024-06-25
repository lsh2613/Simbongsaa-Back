package com.simbongsa.member_ranking.repository;

import com.simbongsa.member_ranking.entity.MemberRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRankingRepository extends JpaRepository<MemberRanking, Long> {
}
