package com.simbongsa.follows.repository;

import com.simbongsa.follows.entity.Follows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, Long> {
}
