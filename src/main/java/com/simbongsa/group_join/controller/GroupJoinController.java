package com.simbongsa.group_join.controller;

import com.simbongsa.global.common.apiPayload.CustomApiResponse;
import com.simbongsa.group_join.dto.req.GroupJoinApplyReq;
import com.simbongsa.group_join.dto.req.GroupJoinDecideReq;
import com.simbongsa.group_join.dto.res.GroupJoinMyRes;
import com.simbongsa.group_join.dto.res.GroupJoinRes;
import com.simbongsa.group_join.service.GroupJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/group-joins")
public class GroupJoinController {
    private final GroupJoinService groupJoinService;

    /**
     * 참가 신청
     * @param memberId 신청자 Id
     * @param groupJoinApplyReq 신청할 그룹, 상태
     * @return ?
     */
    @PostMapping
    public CustomApiResponse<?> applyJoin(@AuthenticationPrincipal Long memberId,
                                          @RequestBody GroupJoinApplyReq groupJoinApplyReq) {
        groupJoinService.applyJoin(memberId, groupJoinApplyReq);
        return CustomApiResponse.onSuccess();
    }

    /**
     * 참가 결정
     * @param memberId 방장 Id
     * @param groupJoinDecideReq 그룹 조인 Id, 수락/거절
     * @return ?
     */
    @PostMapping("/{groupId}/decision")
    public CustomApiResponse<?> decideJoin(@AuthenticationPrincipal Long memberId,
                                           @RequestBody GroupJoinDecideReq groupJoinDecideReq) {
        groupJoinService.decideJoin(memberId, groupJoinDecideReq);
        return CustomApiResponse.onSuccess();
    }

    /**
     * 지원자 현황
     * @param memberId 유저 Id
     * @param groupId 그룹 Id
     * @return 지원자 신청 현황
     */
    @GetMapping("/{groupId}/applications")
    public CustomApiResponse<List<GroupJoinRes>> getJoin(@AuthenticationPrincipal Long memberId,
                                        @PathVariable Long groupId) {
        List<GroupJoinRes> groupJoinList = groupJoinService.getGroupJoinList(memberId, groupId);
        return CustomApiResponse.onSuccess(groupJoinList);
    }

    /**
     * 내 지원 현황
     * @param memberId 유저 Id
     * @return 내 지원 현황
     */
    @GetMapping("/my-applications")
    public CustomApiResponse<List<GroupJoinMyRes>> getJoinList(@AuthenticationPrincipal Long memberId) {
        List<GroupJoinMyRes> myGroupJoinList = groupJoinService.getMyGroupJoinList(memberId);
        return CustomApiResponse.onSuccess(myGroupJoinList);
    }
}
