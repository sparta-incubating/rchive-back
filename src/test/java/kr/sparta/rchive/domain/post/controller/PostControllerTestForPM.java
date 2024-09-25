package kr.sparta.rchive.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.PostBookmarkCoreService;
import kr.sparta.rchive.domain.core.service.PostCommentCoreService;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.*;
import kr.sparta.rchive.domain.post.dto.response.*;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.security.WithMockCustomPM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class})
@ActiveProfiles("test")
@WithMockCustomPM
public class PostControllerTestForPM {

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
    @MockBean
    private PostService postService;
    @Autowired
    private PostController postController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("POST-001 게시물 생성 테스트")
    public void 관리자_게시물_생성() throws Exception {
        // Given
        PostCreateReq request = PostCreateReq.builder()
                .postType(PostTypeEnum.Sparta_Lecture)
                .title("Test_Post_Create")
                .tutorId(1L)
                .uploadedAt(LocalDate.now())
                .thumbnailUrl("test")
                .videoLink("test")
                .contentLink("test")
                .content("test")
                .postPeriod(1)
                .isOpened(true)
                .build();

        String json = obj.writeValueAsString(request);

        PostCreateRes response = PostCreateRes.builder()
                .postId(1L)
                .build();

        given(postTagCoreService.createPost(any(User.class), any(TrackNameEnum.class),
                any(Integer.class), any(PostCreateReq.class)))
                .willReturn(response);
        // When - Then
        mockMvc.perform(post("/apis/v1/posts?trackName=ANDROID&loginPeriod=0")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.message").value("교육자료 생성 성공"),
                jsonPath("$.data.postId").value(1L)
        );
    }

    @Test
    @DisplayName("POST-002 게시물 수정 테스트")
    public void 관리자_게시물_수정() throws Exception {
        // Given
        Long postId = 1L;
        PostUpdateReq request = PostUpdateReq.builder()
                .postType(PostTypeEnum.Sparta_Lecture)
                .title("change test")
                .tutorId(1L)
                .uploadedAt(LocalDate.now())
                .thumbnailUrl("change test")
                .videoLink("change test")
                .contentLink("change test")
                .content("change test")
                .updatePeriod(2)
                .isOpened(true)
                .build();

        String json = obj.writeValueAsString(request);

        PostModifyRes response = PostModifyRes.builder()
                .postId(1L)
                .build();

        given(postTagCoreService.updatePost(any(User.class), any(TrackNameEnum.class),
                any(Integer.class),
                any(Long.class), any(PostUpdateReq.class))).willReturn(response);

        // When - Then
        mockMvc.perform(patch("/apis/v1/posts/{postId}?trackName=ANDROID&loginPeriod=0", postId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.message").value("교육자료 수정 성공"),
                jsonPath("$.data.postId").value(1L)
        );
    }

    @Test
    @DisplayName("POST-003 게시물 삭제 테스트")
    public void 관리자_게시물_삭제() throws Exception {
        // Given
        Long postId = 1L;

        // When
        postTagCoreService.deletePost(any(User.class), any(TrackNameEnum.class), any(Integer.class),
                any(Long.class));

        // Then
        mockMvc.perform(delete("/apis/v1/posts/{postId}?trackName=ANDROID&loginPeriod=0", postId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("교육자료 삭제 성공")
                );
    }

    @Test
    @DisplayName("POST-010 태그 생성 테스트")
    public void 사용할_태그_생성() throws Exception {
        // Given
        List<String> tagNameList = List.of("tagTest");

        TagCreateReq request = TagCreateReq.builder()
                .tagNameList(tagNameList)
                .build();

        TagCreateRes response = TagCreateRes.builder()
                .tagName("tagTest")
                .build();

        List<TagCreateRes> responseList = List.of(response);

        String json = obj.writeValueAsString(request);

        given(tagService.createTag(request)).willReturn(responseList);

        // When - Then
        mockMvc.perform(post("/apis/v1/posts/tags")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.message").value("태그 생성 성공"),
                jsonPath("$.data[0].tagName").value("tagTest")
        );
    }

    @Test
    @DisplayName("POST-011 태그 검색 테스트")
    public void 사용할_태그_검색() throws Exception {
        // Given
        List<TagSearchRes> responseList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            TagSearchRes tagSearchRes = TagSearchRes.builder()
                    .tagId((long) i)
                    .tagName("test tag")
                    .build();

            responseList.add(tagSearchRes);
        }

        given(tagService.searchTag(any(String.class))).willReturn(responseList);

        // When - Then
        mockMvc.perform(get("/apis/v1/posts/tags?tagName=test"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("태그 검색 성공"),
                        jsonPath("$.data[0].tagId").value(1L)
                );
    }

    @Test
    @DisplayName("POST-015 게시물 공개로 변경")
    public void 게시물_공개_여부_공개로_변경() throws Exception {
        // Given
        List<Long> postIdList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            postIdList.add((long) i);
        }

        PostOpenCloseReq request = PostOpenCloseReq.builder()
                .postIdList(postIdList)
                .build();

        String json = obj.writeValueAsString(request);

        // When
        postTagCoreService.openPost(any(User.class), any(TrackNameEnum.class), any(Integer.class),
                any());

        // Then
        mockMvc.perform(patch("/apis/v1/posts/open?trackName=ANDROID&loginPeriod=0")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("게시물 공개 여부 공개로 변경")
                );
    }

    @Test
    @DisplayName("POST-016 게시물 비공개로 변경")
    public void 게시물_공개_여부_비공개로_변경() throws Exception {
        // Given
        List<Long> postIdList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            postIdList.add((long) i);
        }

        PostOpenCloseReq request = PostOpenCloseReq.builder()
                .postIdList(postIdList)
                .build();

        String json = obj.writeValueAsString(request);

        // When
        postTagCoreService.closePost(any(User.class), any(TrackNameEnum.class), any(Integer.class),
                any());

        // Then
        mockMvc.perform(patch("/apis/v1/posts/close?trackName=ANDROID&loginPeriod=0")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("게시물 공개 여부 비공개로 변경")
                );
    }

    @Test
    @DisplayName("POST-017 튜터 목록 검색")
    public void 게시물_작성_시_튜터_목록_검색() throws Exception {
        // Given
        List<TutorRes> responseList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            TutorRes tutorRes = TutorRes.builder()
                    .tutorId((long) i)
                    .tutorName("test tutor")
                    .build();

            responseList.add(tutorRes);
        }

        given(postTagCoreService.searchTutor(any(User.class), any(TrackNameEnum.class),
                any(Integer.class),
                any(Integer.class), any(String.class))).willReturn(responseList);

        // When - Then
        mockMvc.perform(
                        get("/apis/v1/posts/tutors?trackName=ANDROID&loginPeriod=0&inputPeriod=1&tutorName=test"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("튜터 검색 성공"),
                        jsonPath("$.data[0].tutorId").value(1L)
                );
    }

    @Test
    @DisplayName("POST-022 썸네일 삭제")
    public void 게시물_썸네일_삭제() throws Exception {
        // Given
        Long postId = 1L;
        DeleteThumbnailReq request = DeleteThumbnailReq.builder()
                .trackName(TrackNameEnum.ANDROID)
                .period(0)
                .build();

        String json = obj.writeValueAsString(request);

        // When
        postController.deleteThumbnail(any(User.class), any(Long.class),
                any(DeleteThumbnailReq.class));

        // Then
        mockMvc.perform(delete("/apis/v1/posts/{postId}/thumbnail", postId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.message").value("썸네일 DB에서 삭제 성공")
        );
    }
}
