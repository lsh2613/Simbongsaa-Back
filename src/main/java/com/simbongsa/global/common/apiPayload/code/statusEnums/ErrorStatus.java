package com.simbongsa.global.common.apiPayload.code.statusEnums;

import com.simbongsa.global.common.apiPayload.code.BaseCode;
import com.simbongsa.global.common.apiPayload.code.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 테스트용
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "Error 테스트"),

    // 토큰
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN4011", "블랙리스트에 존재하는 Access Token입니다"),
    TOKEN_ID_MISMATCH(HttpStatus.FORBIDDEN, "TOKEN4003", "토큰의 사용자와 로그인된 사용자가 일치하지 않습니다."),

    // 회원 관련
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "존재하지 않는 사용자 ID입니다."),
    DUPLICATED_USERID(HttpStatus.UNAUTHORIZED, "USER4011", "이미 존재하는 사용자 ID입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "USER4012", "사용자 비밀번호가 일치하지 않습니다"),
    SESSION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "USER4013", "존재하지 않는 유효한 세션입니다."),
    USER_FORBIDDEN(HttpStatus.FORBIDDEN, "USER403", "사용자에게 권한이 없습니다."),


    // 그룹 관련
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP404", "그룹을 찾을 수 없습니다."),
    GROUP_FULL(HttpStatus.BAD_REQUEST, "GROUP4001", "그룹 인원이 가득 찼습니다."),
    
    // 그룹 지원 관련
    GROUP_JOIN_CONFLICT(HttpStatus.CONFLICT, "GROUP_JOIN409", "지원 신청이 이미 존재합니다."),
    GROUP_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP_JOIN4042", "그룹 신청을 찾을 수 없습니다."),

    // 그룹 유저 관련
    GROUP_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUPUSER404", "해당 그룹에서 유저를 찾을 수 없습니다."),

    // 팔로우 요청 관련
    FOLLOWS_REQUESTS_NOT_FOUND(HttpStatus.BAD_REQUEST, "FOLLOWS_REQUESTS400", "존재하지 않는 팔로우 요청입니다"),

    // 공지 관련
    NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTICE4001", "존재하지 않는 공지 ID입니다."),

    // S3 이미지 업로드
    FAIL_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "S500", "S3에 이미지 업로드를 실패했습니다."),
    BAD_REQUEST_IMAGE(HttpStatus.BAD_REQUEST, "S400", "잘못된 이미지 데이터입니다."),
    UNAUTHORIZED_S3(HttpStatus.UNAUTHORIZED, "S401", "S3 접근 인증에 실패했습니다."),
    FORBIDDEN_S3(HttpStatus.FORBIDDEN, "S403", "S3 권한을 가지고 있지 않습니다."),
    UNAVAILABLE_S3(HttpStatus.SERVICE_UNAVAILABLE, "S503", "S3 서버가 일시적으로 데이터를 처리할 수 없습니다."),

    // 파일
    FAIL_FILE_CONVERT(HttpStatus.BAD_REQUEST, "CONVERT1000", "파일 변환에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ResponseDTO getDto() {
        return ResponseDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ResponseDTO getHttpStatusDto() {
        return ResponseDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
