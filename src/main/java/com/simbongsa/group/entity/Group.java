package com.simbongsa.group.entity;

import com.simbongsa.global.common.constant.GroupStatus;
import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "\"GROUP\"")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String introduction;

    @Column(nullable = false)
    private Integer max_people;

    @Column(nullable = false)
    private Integer current_people;

    @OneToOne
    @JoinColumn(name = "group_leader_id")
    private Member group_leader;

    private boolean exposed_status;
}
