package com.simbongsa.common.entity;

import com.amazonaws.services.ec2.model.Volume;
import com.simbongsa.badge.entity.Badge;
import com.simbongsa.feed.entity.Feed;
import com.simbongsa.group.entity.Group;
import com.simbongsa.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "IMAGE")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @OneToOne
    @JoinColumn
    private Member member;

    @OneToOne
    @JoinColumn
    private Group group;

    @OneToOne
    @JoinColumn
    private Badge badge;

    @OneToOne
    @JoinColumn
    private Feed feed;

    // Posts

    // Volunteer
}
