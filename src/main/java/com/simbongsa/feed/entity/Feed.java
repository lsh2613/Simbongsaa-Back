package com.simbongsa.feed.entity;

import com.simbongsa.comment.entity.Comment;
import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.global.image.entity.Image;
import com.simbongsa.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FEED")
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private Integer views;
    @Column(nullable = false)
    private Integer likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();

    public void addImage(Image image) {
        if (images == null) images = new ArrayList<>();
        images.add(image);
        image.setFeed(this);
    }

    public void addImages(List<Image> images) {
        if (images == null) images = new ArrayList<>();
        images.forEach(this::addImage);
    }
}
