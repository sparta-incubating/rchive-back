package kr.sparta.rchive.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.PostBookmarkCoreService;
import kr.sparta.rchive.domain.core.service.PostCommentCoreService;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.post.dto.response.PostGetSinglePostRes;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.security.WithMockCustomUser;
import kr.sparta.rchive.test.CommentTest;
import kr.sparta.rchive.test.PostTest;
import kr.sparta.rchive.test.TagTest;
import kr.sparta.rchive.test.TutorTest;
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
public class PostControllerTestForUser implements PostTest, TutorTest, TagTest, CommentTest {

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
                    .thumbnailUrl(TEST_POST.getThumbnailUrl())
                    .postType(TEST_POST.getPostType())
                    .tutor(TEST_TUTOR.getTutorName())
                    .title(TEST_POST.getTitle())
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
                    .thumbnailUrl(TEST_POST.getThumbnailUrl())
                    .postType(TEST_POST.getPostType())
                    .tutor(TEST_TUTOR.getTutorName())
                    .title(TEST_POST.getTitle())
                    .tagList(tagList)
                    .build();

            postGetResList.add(postGetRes);
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<PostGetRes> response = new PageImpl<>(postGetResList, pageable, 2);

        given(postTagCoreService.getPostListByCategory(any(User.class), any(TrackNameEnum.class),
                any(Integer.class), any(PostTypeEnum.class), any())).willReturn(response);
        // When - Then
        mockMvc.perform(get("/apis/v1/posts/category")
                        .param("trackName", "ANDROID")
                        .param("loginPeriod", "1")
                        .param("category", String.valueOf(PostTypeEnum.Sparta_Lecture))
                        .param("page", "1")
                        .param("size", "10"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("교육자료 카테고리 별 조회 성공"),
                        jsonPath("$.data.content[0].thumbnailUrl").value(TEST_POST.getThumbnailUrl())
                );
    }

    @Test
    @DisplayName("POST-006 게시물 단건 조회 기능 테스트")
    public void 유저_게시물_단건_조회() throws Exception {
        // Given
        Long postId = TEST_POST_ID;

        List<TagInfo> tagInfoList = new ArrayList<>();

        TagInfo tagInfo = TagInfo.builder()
                .tagId(TEST_1L_TAG.getId())
                .tagName(TEST_1L_TAG.getTagName())
                .build();

        tagInfoList.add(tagInfo);

        PostGetSinglePostRes response = PostGetSinglePostRes.builder()
                .postId(postId)
                .title(TEST_POST.getTitle())
                .tutor(TEST_POST.getTutor().getTutorName())
                .videoLink(TEST_POST.getVideoLink())
                .contentLink(TEST_POST.getContentLink())
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
                        jsonPath("$.data.title").value(TEST_POST.getTitle())
                );
    }

    @Test
    @DisplayName("POST-007 게시물 댓글 작성 기능 테스트")
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
    @DisplayName("POST-008 게시물 댓글 삭제 기능 테스트")
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
}
