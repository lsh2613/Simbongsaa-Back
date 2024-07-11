package com.simbongsa.group_user.repository;

import com.simbongsa.group_user.entity.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    Optional<GroupUser> findByGroup_IdAndMember_Id(Long groupId, Long memberId);
}
