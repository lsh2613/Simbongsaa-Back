package com.simbongsa.feed.entity;

import com.simbongsa.comment.entity.Comment;
import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.global.common.entity.Image;
import com.simbongsa.group.entity.Group;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FEED")
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<Image> image = new ArrayList<>();

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<Comment> comment = new ArrayList<>();
}
