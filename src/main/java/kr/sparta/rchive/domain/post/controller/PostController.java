package kr.sparta.rchive.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.dto.response.CommentGetRes;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.PostBookmarkCoreService;
import kr.sparta.rchive.domain.core.service.PostCommentCoreService;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.DeleteThumbnailReq;
import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostOpenCloseReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateContentReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.dto.request.RecentSearchKeywordReq;
import kr.sparta.rchive.domain.post.dto.request.TagCreateReq;
import kr.sparta.rchive.domain.post.dto.response.PostCreateRes;
import kr.sparta.rchive.domain.post.dto.response.PostGetPostTypeRes;
import kr.sparta.rchive.domain.post.dto.response.PostGetRecentKeywordRes;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.post.dto.response.PostGetSinglePostRes;
import kr.sparta.rchive.domain.post.dto.response.PostModifyRes;
import kr.sparta.rchive.domain.post.dto.response.TagCreateRes;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.dto.response.TutorRes;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.exception.statement.ClosePostException;
import kr.sparta.rchive.domain.post.exception.statement.CreateBookmarkException;
import kr.sparta.rchive.domain.post.exception.statement.CreateCommentException;
import kr.sparta.rchive.domain.post.exception.statement.CreatePostException;
import kr.sparta.rchive.domain.post.exception.statement.CreateTagException;
import kr.sparta.rchive.domain.post.exception.statement.DeleteBookmarkException;
import kr.sparta.rchive.domain.post.exception.statement.DeletePostException;
import kr.sparta.rchive.domain.post.exception.statement.DeleteRecentSearchKeywordException;
import kr.sparta.rchive.domain.post.exception.statement.GetPostCategoryException;
import kr.sparta.rchive.domain.post.exception.statement.GetPostException;
import kr.sparta.rchive.domain.post.exception.statement.GetRecentSearchKeywordException;
import kr.sparta.rchive.domain.post.exception.statement.OpenPostException;
import kr.sparta.rchive.domain.post.exception.statement.SaveRecentSearchKeywordException;
import kr.sparta.rchive.domain.post.exception.statement.SearchPostByTagException;
import kr.sparta.rchive.domain.post.exception.statement.SearchPostException;
import kr.sparta.rchive.domain.post.exception.statement.SearchTutorException;
import kr.sparta.rchive.domain.post.exception.statement.UpdatePostException;
import kr.sparta.rchive.domain.post.response.PostResponseCode;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.global.custom.CustomPageable;
import kr.sparta.rchive.global.execption.ApiExceptionCodeExample;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/posts")
@Tag(name = "4. Post API", description = "Post 관련 API입니다.")
public class PostController {

    private final PostTagCoreService postTagCoreService;
    private final PostBookmarkCoreService postBookmarkCoreService;
    private final PostCommentCoreService postCommentCoreService;
    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;

    @PostMapping
    @Operation(operationId = "POST-001", summary = "게시물 생성")
    @ApiExceptionCodeExample(CreatePostException.class)
    public ResponseEntity<CommonResponseDto> createPost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestBody PostCreateReq request
    ) {
        PostCreateRes response = postTagCoreService.createPost(user, trackName, period, request);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_POST, response));
    }

    @PatchMapping("/{postId}")
    @Operation(operationId = "POST-002", summary = "게시물 관리 - 수정")
    @ApiExceptionCodeExample(UpdatePostException.class)
    public ResponseEntity<CommonResponseDto> updatePost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @PathVariable Long postId,
            @RequestBody PostUpdateReq request
    ) {
        PostModifyRes response = postTagCoreService.updatePost(user, trackName, period, postId, request);

        return ResponseEntity.status(PostResponseCode.OK_UPDATE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_UPDATE_POST, response));
    }

    @DeleteMapping("/{postId}")
    @Operation(operationId = "POST-003", summary = "게시물 관리 - 삭제")
    @ApiExceptionCodeExample(DeletePostException.class)
    public ResponseEntity<CommonResponseDto> deletePost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @PathVariable Long postId
    ) {
        postTagCoreService.deletePost(user, trackName, period, postId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_POST, null));
    }

    @GetMapping("/search")
    @Operation(operationId = "POST-004", summary = "게시물 검색")
    @ApiExceptionCodeExample(SearchPostException.class)
    public ResponseEntity<CommonResponseDto> searchPosts(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("selectPeriod") Integer period,
            @RequestParam(value = "category", required = false) PostTypeEnum postType,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "tutorId", required = false) Long tutorId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<PostGetRes> responseList = postTagCoreService.searchPosts(user, postType, trackName, period, keyword, tutorId, pageable);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_POST, responseList));
    }

    @GetMapping("/category")
    @Operation(operationId = "POST-005", summary = "게시물 목록 조회")
    @ApiExceptionCodeExample(GetPostCategoryException.class)
    public ResponseEntity<CommonResponseDto> getPostCategory(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("selectPeriod") Integer period,
            @RequestParam(value = "category", required = false) PostTypeEnum postType,
            @RequestParam(value = "tutorId", required = false) Long tutorId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<PostGetRes> responseList = postTagCoreService.getPostListByCategory(user, trackName, period, postType, tutorId, pageable);
        return ResponseEntity.status(PostResponseCode.OK_GET_CATEGORY_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_GET_CATEGORY_POST, responseList));
    }

    @GetMapping("/{postId}")
    @Operation(operationId = "POST-006", summary = "게시물 단건 조회")
    @ApiExceptionCodeExample(GetPostException.class)
    public ResponseEntity<CommonResponseDto> getPost(
            @LoginUser User user,
            @PathVariable Long postId,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("period") Integer period
    ) {
        PostGetSinglePostRes response = postTagCoreService.getPost(user, postId, trackName, period);

        return ResponseEntity.status(PostResponseCode.OK_GET_SINGLE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_GET_SINGLE_POST, response));
    }

    @PostMapping("/{postId}/comments")
    @Operation(operationId = "POST-007", summary = "게시물 댓글 작성")
    @ApiExceptionCodeExample(CreateCommentException.class)
    public ResponseEntity<CommonResponseDto> createComment(
            @LoginUser User user,
            @PathVariable Long postId,
            @RequestParam(value = "parentComment", required = false) Long parentCommentId,
            @RequestBody CommentCreateReq request
    ) {
        postCommentCoreService.createComment(user, postId, parentCommentId, request);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_COMMENT.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_COMMENT, null));
    }

    @DeleteMapping("/comment/{commentId}")
    @Operation(operationId = "POST-008", summary = "게시물 댓글 삭제")
    @ApiExceptionCodeExample(DeletePostException.class)
    public ResponseEntity<CommonResponseDto> deleteComment(
            @LoginUser User user,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(user, commentId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_COMMENT.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_COMMENT, null));
    }

    @GetMapping("/{postId}/comments")
    @Operation(operationId = "POST-009", summary = "게시물 부모 댓글 리스트 조회")
    public ResponseEntity<CommonResponseDto> getParentCommentList(
            @PathVariable Long postId
    ) {
        List<CommentGetRes> responseList = commentService.getParentCommentList(postId);

        return ResponseEntity.status(PostResponseCode.OK_GET_PARENT_COMMENT.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_GET_PARENT_COMMENT, responseList));
    }

    @PostMapping("/tags")
    @Operation(operationId = "POST-010", summary = "사용할 태그 생성")
    @ApiExceptionCodeExample(CreateTagException.class)
    public ResponseEntity<CommonResponseDto> createTag(
            @RequestBody TagCreateReq request
    ) {
        List<TagCreateRes> responseList = tagService.createTag(request);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_TAG, responseList));
    }

    @GetMapping("/tags")
    @Operation(operationId = "POST-011", summary = "사용할 태그 검색")
    public ResponseEntity<CommonResponseDto> searchTag(
            @RequestParam("tagName") String tagName
    ) {

        List<TagSearchRes> responseList = tagService.searchTag(tagName);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_TAG, responseList));
    }

    @GetMapping("/tags/search")
    @Operation(operationId = "POST-012", summary = "태그를 클릭하여 검색하는 기능")
    @ApiExceptionCodeExample(SearchPostByTagException.class)
    public ResponseEntity<CommonResponseDto> searchPostByTag(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("selectPeriod") Integer period,
            @RequestParam("tagId") Long tagId,
            @RequestParam(value = "postType", required = false) PostTypeEnum postType,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<PostGetRes> responseList = postTagCoreService.searchPostByTag(trackName, period, tagId, user, postType, pageable);
        return ResponseEntity.status(PostResponseCode.OK_SEARCH_POST_BY_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_POST_BY_TAG, responseList));
    }

    @PostMapping("/{postId}/bookmark")
    @Operation(operationId = "POST-013", summary = "북마크 생성")
    @ApiExceptionCodeExample(CreateBookmarkException.class)
    public ResponseEntity<CommonResponseDto> createBookmark(
            @LoginUser User user,
            @PathVariable Long postId
    ) {
        postBookmarkCoreService.createBookmark(user, postId);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_BOOKMARK.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_BOOKMARK, null));
    }

    @DeleteMapping("/{postId}/bookmark")
    @Operation(operationId = "POST-014", summary = "북마크 삭제")
    @ApiExceptionCodeExample(DeleteBookmarkException.class)
    public ResponseEntity<CommonResponseDto> deleteBookmark(
            @LoginUser User user,
            @PathVariable Long postId
    ) {
        postBookmarkCoreService.deleteBookmark(user, postId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_BOOKMARK.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_BOOKMARK, null));
    }

    @PatchMapping("/open")
    @Operation(operationId = "POST-015", summary = "게시물 공개 여부 변경 - 공개")
    @ApiExceptionCodeExample(OpenPostException.class)
    public ResponseEntity<CommonResponseDto> openPost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestBody PostOpenCloseReq request
    ) {
        postTagCoreService.openPost(user, trackName, period, request.postIdList());

        return ResponseEntity.status(PostResponseCode.OK_OPEN_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_OPEN_POST, null));
    }

    @PatchMapping("/close")
    @Operation(operationId = "POST-016", summary = "게시물 공개 여부 변경 - 비공개")
    @ApiExceptionCodeExample(ClosePostException.class)
    public ResponseEntity<CommonResponseDto> closePost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestBody PostOpenCloseReq request
    ) {
        postTagCoreService.closePost(user, trackName, period, request.postIdList());

        return ResponseEntity.status(PostResponseCode.OK_CLOSE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CLOSE_POST, null));
    }

    @GetMapping("/tutors")
    @Operation(operationId = "POST-017", summary = "튜터를 검색")
    @ApiExceptionCodeExample(SearchTutorException.class)
    public ResponseEntity<CommonResponseDto> searchTutor(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestParam("inputPeriod") Integer inputPeriod,
            @RequestParam(value = "tutorName", required = false) String tutorName
    ) {
        List<TutorRes> responseList = postTagCoreService.searchTutor(user, trackName, period, inputPeriod, tutorName);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_TUTOR.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_TUTOR, responseList));
    }

    @GetMapping("/comment/{parentCommentId}")
    @Operation(operationId = "POST-018", summary = "댓글의 대댓글 조회")
    public ResponseEntity<CommonResponseDto> getReply(
            @PathVariable Long parentCommentId
    ) {
        List<CommentGetRes> responseList = commentService.getReply(parentCommentId);

        return ResponseEntity.status(PostResponseCode.OK_GET_REPLY.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_GET_REPLY, responseList));
    }

    @PostMapping("/search/recent")
    @Operation(operationId = "POST-019", summary = "유저의 최근 검색어 저장")
    @ApiExceptionCodeExample(SaveRecentSearchKeywordException.class)
    public ResponseEntity<CommonResponseDto> saveRecentSearchKeyword(
            @LoginUser User user,
            @RequestBody RecentSearchKeywordReq request
    ) {
        postTagCoreService.saveRecentSearchKeyword(user, request);

        return ResponseEntity.status(PostResponseCode.OK_SAVE_RECENT_SEARCH_KEYWORD.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SAVE_RECENT_SEARCH_KEYWORD, null));
    }

    @GetMapping("/search/recent")
    @Operation(operationId = "POST-020", summary = "유저의 최근 검색어 조회")
    @ApiExceptionCodeExample(GetRecentSearchKeywordException.class)
    public ResponseEntity<CommonResponseDto> getRecentSearchKeyword(
        @LoginUser User user,
        @RequestParam("trackName") TrackNameEnum trackName,
        @RequestParam("selectPeriod") Integer period
    ) {
        List<PostGetRecentKeywordRes> responseList = postTagCoreService.getRecentSearchKeyword(user, trackName, period);

        return ResponseEntity.status(PostResponseCode.OK_GET_RECENT_SEARCH_KEYWORD.getHttpStatus())
            .body(CommonResponseDto.of(PostResponseCode.OK_GET_RECENT_SEARCH_KEYWORD, responseList));
    }

    @DeleteMapping("/search/recent")
    @Operation(operationId = "POST-021", summary = "유저의 최근 검색어 삭제")
    @ApiExceptionCodeExample(DeleteRecentSearchKeywordException.class)
    public ResponseEntity<CommonResponseDto> deleteRecentSearchKeyword(
            @LoginUser User user,
            @RequestBody RecentSearchKeywordReq request
    ) {
        postTagCoreService.deleteRecentSearchKeyword(user, request);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_RECENT_SEARCH_KEYWORD.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_RECENT_SEARCH_KEYWORD, null));
    }

    @DeleteMapping("/{postId}/thumbnail")
    @Operation(operationId = "POST-022", summary = "썸네일 삭제")
    public ResponseEntity<CommonResponseDto> deleteThumbnail(
        @LoginUser User user,
        @PathVariable Long postId,
        @RequestBody DeleteThumbnailReq request
    ) {
        postTagCoreService.deleteThumbnail(user, postId, request);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_THUMBNAIL.getHttpStatus())
            .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_THUMBNAIL, null));
    }

    @GetMapping("/postType")
    @Operation(operationId = "POST-023", summary = "카테고리 목록 조회")
    public ResponseEntity<CommonResponseDto> getCategory() {
        List<PostGetPostTypeRes> responseList = postService.getCategory();

        return ResponseEntity.status(PostResponseCode.OK_GET_POST_TYPE_LIST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_GET_POST_TYPE_LIST, responseList));
    }

    @PatchMapping("/{postId}/content")
    @Operation(operationId = "POST-024", summary = "게시물 내용 업데이트")
    public ResponseEntity<CommonResponseDto> updateContent(
        @PathVariable Long postId,
        @RequestBody PostUpdateContentReq request
    ) {
        postService.updateContent(postId, request);

        return ResponseEntity.status(PostResponseCode.OK_UPDATE_CONTENT.getHttpStatus())
            .body(CommonResponseDto.of(PostResponseCode.OK_UPDATE_CONTENT, null));
    }
}