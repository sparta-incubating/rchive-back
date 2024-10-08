package kr.sparta.rchive.domain.post.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostResponseCode implements ResponseCode {

    // OK 200
    OK_CREATE_POST(HttpStatus.OK, "POST-001", "교육자료 생성 성공"),
    OK_UPDATE_POST(HttpStatus.OK, "POST-002", "교육자료 수정 성공"),
    OK_DELETE_POST(HttpStatus.OK, "POST-003", "교육자료 삭제 성공"),
    OK_SEARCH_POST(HttpStatus.OK, "POST-004", "교육자료 검색 성공"),
    OK_GET_CATEGORY_POST(HttpStatus.OK, "POST-005", "교육자료 카테고리 별 조회 성공"),
    OK_GET_SINGLE_POST(HttpStatus.OK, "POST-006", "교육자료 단건 조회 성공"),
    OK_CREATE_COMMENT(HttpStatus.OK, "POST-007", "댓글 작성 성공"),
    OK_DELETE_COMMENT(HttpStatus.OK, "POST-008", "댓글 삭제 성공"),
    OK_GET_PARENT_COMMENT(HttpStatus.OK, "POST-009", "부모 댓글 리스트 조회 성공"),
    OK_CREATE_TAG(HttpStatus.OK, "POST-010", "태그 생성 성공"),
    OK_SEARCH_TAG(HttpStatus.OK, "POST-011", "태그 검색 성공"),
    OK_SEARCH_POST_BY_TAG(HttpStatus.OK, "POST-012", "태그를 이용한 교육자료 검색 성공"),
    OK_CREATE_BOOKMARK(HttpStatus.OK, "POST-013", "북마크 생성 성공"),
    OK_DELETE_BOOKMARK(HttpStatus.OK, "POST-014", "북마크 삭제 성공"),
    OK_OPEN_POST(HttpStatus.OK, "POST-015", "게시물 공개 여부 공개로 변경"),
    OK_CLOSE_POST(HttpStatus.OK, "POST-016", "게시물 공개 여부 비공개로 변경"),
    OK_SEARCH_TUTOR(HttpStatus.OK, "POST-017", "튜터 검색 성공"),
    OK_GET_REPLY(HttpStatus.OK, "POST-018", "대댓글 조회 성공"),
    OK_SAVE_RECENT_SEARCH_KEYWORD(HttpStatus.OK, "POST-019", "최근 검색어 저장 성공"),
    OK_GET_RECENT_SEARCH_KEYWORD(HttpStatus.OK, "POST-020", "최근 검색어 조회 성공"),
    OK_DELETE_RECENT_SEARCH_KEYWORD(HttpStatus.OK, "POST-021", "최근 검색어 삭제 성공"),
    OK_DELETE_THUMBNAIL(HttpStatus.OK, "POST-022", "썸네일 DB에서 삭제 성공"),
    OK_GET_POST_TYPE_LIST(HttpStatus.OK, "POST-023", "카테고리 리스트 조회 성공"),
    OK_UPDATE_CONTENT(HttpStatus.OK, "POST_024", "게시물 내용 업데이트 성공");

    private final HttpStatus httpStatus;
    private final String responseCode;
    private final String message;

    public HttpStatus getHttpStatus(PostResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getResponseCode(PostResponseCode responseCode) {
        return responseCode.responseCode;
    }

    public String getMessage(PostResponseCode responseCode) {
        return responseCode.getMessage();
    }
}
