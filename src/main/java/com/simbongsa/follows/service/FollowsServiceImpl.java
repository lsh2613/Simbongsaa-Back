package com.simbongsa.follows.service;

import com.simbongsa.follows.entity.Follows;
import com.simbongsa.follows.repository.FollowsRepository;
import com.simbongsa.global.EntityFacade;
import com.simbongsa.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.simbongsa.global.common.exception.GeneralHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FollowsServiceImpl implements FollowsService {
    private final FollowsRepository followsRepository;
    private final EntityFacade entityFacade;

    @Override
    public void deleteMyFollows(Long loginId, Long followsId) {
        Follows follows = entityFacade.getFollows(followsId);

        if (!validateMyFollowsTarget(loginId, follows.getFollowingMember().getId())) {
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        }

        followsRepository.delete(follows);
    }

    @Override
    public void deleteOppositeFollows(Long loginId, Long followsId) {
        Follows follows = entityFacade.getFollows(followsId);

        if (!validateOppositeFollowsTarget(loginId, follows.getFollowedMember().getId())) {
            throw new GeneralHandler(ErrorStatus.USER_FORBIDDEN);
        }

        followsRepository.delete(follows);
    }

    private boolean validateOppositeFollowsTarget(Long loginId, Long followedId) {
        return followedId.equals(loginId);
    }

    private boolean validateMyFollowsTarget(Long loginId, Long followingId) {
        return followingId.equals(loginId);
    }
}
