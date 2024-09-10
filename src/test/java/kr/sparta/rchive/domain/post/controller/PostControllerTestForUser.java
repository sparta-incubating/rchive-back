package kr.sparta.rchive.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.dto.response.CommentGetRes;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.PostBookmarkCoreService;
import kr.sparta.rchive.domain.core.service.PostCommentCoreService;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.dto.request.RecentSearchKeywordReq;
import kr.sparta.rchive.domain.post.dto.response.PostGetRecentKeywordRes;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.post.dto.response.PostGetSinglePostRes;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.security.WithMockCustomUser;
import kr.sparta.rchive.test.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class})
@ActiveProfiles("test")
@WithMockCustomUser
public class PostControllerTestForUser implements PostTest, TutorTest, TagTest, CommentTest, UserTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper obj;

    @MockBean
    private PostTagCoreService postTagCoreService;
    @MockBean
    private PostBookmarkCoreService postBookmarkCoreService;
    @MockBean
    private PostCommentCoreService postCommentCoreService;
    @MockBean
    private TagService tagService;
    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("POST-004 유저 게시물 검색 기능 테스트")
    public void 유저_게시물_검색() throws Exception {
        // Given
        List<TagInfo> tagList = new ArrayList<>();

        TagInfo tagInfo = TagInfo.builder()
                .tagId(TEST_TAG_1L_ID)
                .tagName(TEST_1L_TAG.getTagName())
                .build();

        tagList.add(tagInfo);

        List<PostGetRes> postGetResList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PostGetRes postGetRes = PostGetRes.builder()
                    .postId((long) i)
                    .thumbnailUrl(TEST_POST_1L.getThumbnailUrl())
                    .postType(TEST_POST_1L.getPostType())
                    .tutor(TEST_TUTOR.getTutorName())
                    .title(TEST_POST_1L.getTitle())
                    .tagList(tagList)
                    .build();

            postGetResList.add(postGetRes);
        }

        Pageable pageable = PageRequest.of(1, 10);
        Page<PostGetRes> response = new PageImpl<>(postGetResList, pageable, 2);

        given(postTagCoreService.searchPosts(any(User.class), any(), eq(TrackNameEnum.ANDROID),
                eq(1), eq("Test"), any(), any(Pageable.class))).willReturn(response);

        // When - Then
        mockMvc.perform(get("/apis/v1/posts/search")
                        .param("trackName", "ANDROID") // 트랙 이름 예시
                        .param("loginPeriod", "1") // 로그인 기간 예시
                        .param("keyword", "Test") // 키워드 예시
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("교육자료 검색 성공"),
                        jsonPath("$.data.content[0].postId").value(0L)
                );
    }

    @Test
    @DisplayName("POST-005 유저 게시판 조회 테스트")
    public void 유저_게시판_조회() throws Exception {
        // Given
        List<TagInfo> tagList = new ArrayList<>();

        TagInfo tagInfo = TagInfo.builder()
                .tagId(TEST_TAG_1L_ID)
                .tagName(TEST_1L_TAG.getTagName())
                .build();

        tagList.add(tagInfo);

        List<PostGetRes> postGetResList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PostGetRes postGetRes = PostGetRes.builder()
                    .postId((long) i)
                    .thumbnailUrl(TEST_POST_1L.getThumbnailUrl())
                    .postType(TEST_POST_1L.getPostType())
                    .tutor(TEST_TUTOR.getTutorName())
                    .title(TEST_POST_1L.getTitle())
                    .tagList(tagList)
                    .build();

            postGetResList.add(postGetRes);
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<PostGetRes> response = new PageImpl<>(postGetResList, pageable, 2);

        given(postTagCoreService.getPostListByCategory(any(User.class), any(TrackNameEnum.class),
                any(Integer.class), any(PostTypeEnum.class), any(Long.class), any())).willReturn(response);
        // When - Then
        mockMvc.perform(get("/apis/v1/posts/category")
                        .param("trackName", "ANDROID")
                        .param("loginPeriod", "1")
                        .param("category", String.valueOf(PostTypeEnum.Sparta_Lecture))
                        .param("tutorId", "1")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("교육자료 카테고리 별 조회 성공"),
                        jsonPath("$.data.content[0].thumbnailUrl").value(TEST_POST_1L.getThumbnailUrl())
                );
    }

    @Test
    @DisplayName("POST-006 유저 게시물 단건 조회 기능 테스트")
    public void 유저_게시물_단건_조회() throws Exception {
        // Given
        Long postId = TEST_POST_1L_ID;

        List<TagInfo> tagInfoList = new ArrayList<>();

        TagInfo tagInfo = TagInfo.builder()
                .tagId(TEST_1L_TAG.getId())
                .tagName(TEST_1L_TAG.getTagName())
                .build();

        tagInfoList.add(tagInfo);

        PostGetSinglePostRes response = PostGetSinglePostRes.builder()
                .postId(postId)
                .title(TEST_POST_1L.getTitle())
                .tutor(TEST_POST_1L.getTutor().getTutorName())
                .videoLink(TEST_POST_1L.getVideoLink())
                .contentLink(TEST_POST_1L.getContentLink())
                .tagList(tagInfoList)
                .isBookmarked(false)
                .build();

        given(postTagCoreService.getPost(any(User.class), any(Long.class), any(TrackNameEnum.class),
                any(Integer.class))).willReturn(response);

        // When - Then
        mockMvc.perform(get("/apis/v1/posts/{postId}", postId)
                        .param("trackName", "ANDROID")
                        .param("period", "1"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("교육자료 단건 조회 성공"),
                        jsonPath("$.data.title").value(TEST_POST_1L.getTitle())
                );
    }

    @Test
    @DisplayName("POST-007 유저 게시물 댓글 작성 기능 테스트")
    public void 유저_게시물_댓글_작성() throws Exception {
        // Given
        Long postId = 1L;

        CommentCreateReq request = CommentCreateReq.builder()
                .content(TEST_1L_COMMENT.getContent())
                .build();

        String json = obj.writeValueAsString(request);
        // When
        postCommentCoreService.createComment(any(User.class), any(Long.class), any(), any(CommentCreateReq.class));

        // Then
        mockMvc.perform(post("/apis/v1/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("댓글 작성 성공")
                );
    }

    @Test
    @DisplayName("POST-008 유저 게시물 댓글 삭제 기능 테스트")
    public void 유저_게시물_댓굴_삭제() throws Exception {
        // Given
        Long commentId = 1L;

        // When
        commentService.deleteComment(any(User.class), any(Long.class));

        // Then
        mockMvc.perform(delete("/apis/v1/posts/comment/{commentId}", commentId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("댓글 삭제 성공")
                );
    }

    @Test
    @DisplayName("POST-009 유저 게시물 부모 댓글 리스트 조회 기능 테스트")
    public void 유저_게시물_부모_댓글_리스트_조회() throws Exception {
        // Given
        Long postId = 1L;

        List<CommentGetRes> commentGetResList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            CommentGetRes commentGetRes = CommentGetRes.builder()
                    .id((long) i)
                    .content(TEST_COMMENT_CONTENT)
                    .username(TEST_STUDENT_USER.getUsername())
                    .email(TEST_STUDENT_USER.getEmail())
                    .nickname(TEST_STUDENT_USER.getNickname())
                    .build();

            commentGetResList.add(commentGetRes);
        }

        given(commentService.getParentCommentList(any(Long.class))).willReturn(commentGetResList);
        // When - Then
        mockMvc.perform(get("/apis/v1/posts/{postId}/comments", postId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("부모 댓글 리스트 조회 성공"),
                        jsonPath("$.data[0].content").value(TEST_COMMENT_CONTENT),
                        jsonPath("$.data[0].content").value(TEST_COMMENT_CONTENT)
                );
    }

    @Test
    @DisplayName("POST-012 유저 게시물을 태그를 클릭하여 검색하는 기능 테스트")
    public void 유저_태그로_게시물을_검색하는_기능() throws Exception {
        // Given
        List<TagInfo> tagList = new ArrayList<>();

        TagInfo tagInfo = TagInfo.builder()
                .tagId(TEST_TAG_1L_ID)
                .tagName(TEST_1L_TAG.getTagName())
                .build();

        tagList.add(tagInfo);

        List<PostGetRes> postGetResList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PostGetRes postGetRes = PostGetRes.builder()
                    .postId((long) i)
                    .thumbnailUrl(TEST_POST_1L.getThumbnailUrl())
                    .postType(TEST_POST_1L.getPostType())
                    .tutor(TEST_TUTOR.getTutorName())
                    .title(TEST_POST_1L.getTitle())
                    .tagList(tagList)
                    .build();

            postGetResList.add(postGetRes);
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<PostGetRes> response = new PageImpl<>(postGetResList, pageable, 2);

        given(postTagCoreService.searchPostByTag(any(TrackNameEnum.class), any(Integer.class), any(Long.class),
                any(User.class), any(PostTypeEnum.class), any(Pageable.class))).willReturn(response);
        // When - Then
        mockMvc.perform(get("/apis/v1/posts/tags/search")
                        .param("trackName", "ANDROID")
                        .param("loginPeriod", "1")
                        .param("tagId", "1")
                        .param("postType", PostTypeEnum.Sparta_Lecture.name())
                        .param("page", "1")
                        .param("size", "10"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("태그를 이용한 교육자료 검색 성공"),
                        jsonPath("$.data.content[0].thumbnailUrl").value(TEST_POST_1L.getThumbnailUrl())
                );
    }

    @Test
    @DisplayName("POST-013 유저 북마크를 추가하는 기능 테스트")
    public void 유저_북마크를_생성하는_기능() throws Exception {
        // When
        postBookmarkCoreService.createBookmark(any(User.class), any(Long.class));

        // Then
        mockMvc.perform(post("/apis/v1/posts/{postId}/bookmark", TEST_POST_1L_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("북마크 생성 성공")
                );
    }

    @Test
    @DisplayName("POST-014 유저 북마크를 삭제하는 기능 테스트")
    public void 유저_북마크를_삭제하는_기능() throws Exception {
        // When
        postBookmarkCoreService.deleteBookmark(any(User.class), any(Long.class));

        // Then
        mockMvc.perform(delete("/apis/v1/posts/{postId}/bookmark", TEST_POST_1L_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("북마크 삭제 성공")
                );
    }

    @Test
    @DisplayName("POST-018 댓글의 대댓글을 조회하는 기능 테스트")
    public void 댓글_대댓글_조회() throws Exception {
        // Given
        CommentGetRes commentGetRes = CommentGetRes.builder()
                .id(TEST_COMMENT_2L_ID)
                .content(TEST_2L_COMMENT.getContent())
                .username(TEST_STUDENT_USER.getUsername())
                .email(TEST_STUDENT_USER.getEmail())
                .nickname(TEST_STUDENT_USER.getNickname())
                .build();

        List<CommentGetRes> commentGetResList = List.of(commentGetRes);

        given(commentService.getReply(any(Long.class))).willReturn(commentGetResList);
        // When - Then
        mockMvc.perform(get("/apis/v1/posts/comment/{parentCommentId}", TEST_COMMENT_1L_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("대댓글 조회 성공"),
                        jsonPath("$.data[0].id").value(2L)
                );
    }

    @Test
    @DisplayName("POST-019 유저의 최근 검색어 저장 기능 테스트")
    public void 유저_최근_검색어_저장() throws Exception {
        // Given
        RecentSearchKeywordReq request = RecentSearchKeywordReq.builder()
                .trackName(TrackNameEnum.ANDROID)
                .period(1)
                .keyword("Test")
                .build();

        String json = obj.writeValueAsString(request);
        // When
        postTagCoreService.saveRecentSearchKeyword(any(User.class), any(RecentSearchKeywordReq.class));

        // Then
        mockMvc.perform(post("/apis/v1/posts/search/recent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("최근 검색어 저장 성공")
                );
    }

    @Test
    @DisplayName("POST-020 유저의 최근 검색어 조회 기능 테스트")
    public void 유저_최근_검색어_조회() throws Exception {
        // Given
        List<PostGetRecentKeywordRes> responseList = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            PostGetRecentKeywordRes postGetRecentKeywordRes = PostGetRecentKeywordRes.builder()
                    .keyword("TEST " + i)
                    .build();

            responseList.add(postGetRecentKeywordRes);
        }

        given(postTagCoreService.getRecentSearchKeyword(any(User.class), any(TrackNameEnum.class), any(Integer.class))).willReturn(responseList);
        // When - Then
        mockMvc.perform(get("/apis/v1/posts/search/recent")
                .param("trackName", "ANDROID")
                .param("loginPeriod", "1"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("최근 검색어 조회 성공"),
                        jsonPath("$.data[0].keyword").value("TEST 1")
                );
    }

    @Test
    @DisplayName("POST-021 유저의 최근 검색어 삭제 기능 테스트")
    public void 유저_최근_검색어_삭제() throws Exception {
        // Given
        RecentSearchKeywordReq recentSearchKeywordReq = RecentSearchKeywordReq.builder()
                .keyword("Test")
                .build();

        String json = obj.writeValueAsString(recentSearchKeywordReq);
        // When
        postTagCoreService.deleteRecentSearchKeyword(any(User.class), any(RecentSearchKeywordReq.class));

        // Then
        mockMvc.perform(delete("/apis/v1/posts/search/recent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("최근 검색어 삭제 성공")
                );
    }
}
