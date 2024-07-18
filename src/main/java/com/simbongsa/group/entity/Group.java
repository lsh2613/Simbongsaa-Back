package com.simbongsa.group.entity;

import com.simbongsa.global.common.constant.GroupStatus;
import com.simbongsa.global.common.entity.BaseEntity;
import com.simbongsa.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "GROUPS")
public class Group extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String introduction;

    @Column(nullable = false)
    private Integer maxPeople;

    @Column(nullable = false)
    private Integer currentPeople;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupStatus groupStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_leader_id")
    private Member groupLeader;

    public void increaseCurrentPeople() {
        this.currentPeople++;
    }
    public void decreaseCurrentPeople() { this.currentPeople--; }
}
