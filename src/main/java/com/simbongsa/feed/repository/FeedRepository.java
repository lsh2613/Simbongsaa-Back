package com.simbongsa.feed.repository;

import com.simbongsa.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Feed f SET f.views = f.views + 1 WHERE f.id = :feedId")
    void increaseViews(@Param("feedId") Long feedId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Feed f SET f.likes = f.likes + 1 WHERE f.id = :feedId")
    void increaseLikes(@Param("feedId") Long feedId);
}
