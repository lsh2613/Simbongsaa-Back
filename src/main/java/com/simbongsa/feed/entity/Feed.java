package com.simbongsa.feed.entity;

import com.simbongsa.common.entity.BaseEntity;
import com.simbongsa.group.entity.Group;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn
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
