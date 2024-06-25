package com.simbongsa.group_ranking.repository;

import com.simbongsa.group_ranking.entity.GroupRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRankingRepository extends JpaRepository<GroupRanking, Long> {
}
