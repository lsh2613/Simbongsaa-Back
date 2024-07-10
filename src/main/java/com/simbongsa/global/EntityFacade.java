package com.simbongsa.global;

import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import com.simbongsa.group.entity.Group;
import com.simbongsa.group.repository.GroupRepository;
import com.simbongsa.member.entity.Member;
import com.simbongsa.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityFacade {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    public Member getMember(Long memberId) {
        Optional<Member> memberById = memberRepository.findById(memberId);
        if (memberById.isEmpty())
            throw new GeneralHandler(ErrorStatus.USER_NOT_FOUND);
        return memberById.get();
    }

    public Group getGroup(Long groupId) {
        Optional<Group> groupById = groupRepository.findById(groupId);
        if (groupById.isEmpty())
            throw new GeneralHandler(ErrorStatus.GROUP_NOT_FOUND);
        return groupById.get();
    }
}
