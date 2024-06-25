package com.simbongsa.group_join.repository;

import com.simbongsa.group_join.entity.GroupJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupJoinRepository extends JpaRepository<GroupJoin, Long> {
}
