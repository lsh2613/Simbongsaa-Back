package com.simbongsa.group.controller;

import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import com.simbongsa.group.dto.req.GroupCreateReq;
import com.simbongsa.group.dto.req.GroupSearchReq;
import com.simbongsa.group.dto.req.GroupUpdateReq;
import com.simbongsa.group.dto.res.GroupRes;
import com.simbongsa.group.dto.res.GroupSearchRes;
import com.simbongsa.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    /**
     * 그룹 목록 조회
     * @param memberId 사용자 Id
     * @param pageable 그룹 Id
     * @return 그룹 List
     */
    @GetMapping
    public CustomApiResponse<?> getGroupList(@AuthenticationPrincipal Long memberId,
                                             @PageableDefault Pageable pageable,
                                             GroupSearchReq groupSearchReq
                                             ) {
        GroupSearchRes groupList = groupService.getGroupList(memberId, pageable, groupSearchReq);
        return CustomApiResponse.onSuccess(groupList);
    }

    /**
     * 그룹 상세 조회
     * @param memberId 사용자 Id
     * @param groupId 그룹 Id
     * @return GroupRes
     */
    @GetMapping("/{groupId}")
    public CustomApiResponse<GroupRes> getGroup(@AuthenticationPrincipal Long memberId,
                                         @PathVariable Long groupId) {
        GroupRes groupRes = groupService.getGroup(memberId, groupId);
        return CustomApiResponse.onSuccess(groupRes);
    }

    /**
     * 그룹 생성
     * @param memberId 방장 Id
     * @param groupCreateReq 생성 Req
     * @return groupId
     */
    @PostMapping
    public CustomApiResponse<Long> createGroup(@AuthenticationPrincipal Long memberId,
                                            @Valid @RequestBody GroupCreateReq groupCreateReq) {
        Long group = groupService.createGroup(memberId, groupCreateReq);
        return CustomApiResponse.onSuccess(group);
    }

    /**
     * 그룹 업데이트
     * @param memberId 방장 Id
     * @param groupId 그룹 Id
     * @param groupUpdateReq 수정 Req
     * @return null
     */
    @PatchMapping("/{groupId}")
    public CustomApiResponse<?> updateGroup(@AuthenticationPrincipal Long memberId,
                                            @PathVariable Long groupId,
                                            @Valid @RequestBody GroupUpdateReq groupUpdateReq) {
        groupService.updateGroup(memberId, groupId, groupUpdateReq);
        return CustomApiResponse.onSuccess();
    }

    /**
     * 그룹 삭제
     * @param memberId 방장 Id
     * @param groupId 그룹 Id
     * @return null
     */
    @DeleteMapping("/{groupId}")
    public CustomApiResponse<?> deleteGroup(@AuthenticationPrincipal Long memberId,
                                            @PathVariable Long groupId) {
        groupService.deleteGroup(memberId, groupId);
        return CustomApiResponse.onSuccess();
    }
    
    //TODO: 그룹 인원 리스트 불러오기

    //TODO: 강퇴


}
