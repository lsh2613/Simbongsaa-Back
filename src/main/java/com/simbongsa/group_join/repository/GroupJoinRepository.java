package com.simbongsa.group_join.repository;

import com.simbongsa.group_join.entity.GroupJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupJoinRepository extends JpaRepository<GroupJoin, Long> {
    Optional<GroupJoin> findByGroup_IdAndMember_Id(Long groupId, Long memberId);
    List<GroupJoin> findByMember_Id(Long memberId);
    List<GroupJoin> findByGroup_Id(Long groupId);
}
