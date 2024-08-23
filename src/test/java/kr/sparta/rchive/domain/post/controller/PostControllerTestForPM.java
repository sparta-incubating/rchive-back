package kr.sparta.rchive.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.PostBookmarkCoreService;
import kr.sparta.rchive.domain.core.service.PostCommentCoreService;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.dto.response.PostCreateRes;
import kr.sparta.rchive.domain.post.dto.response.PostModifyRes;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.security.WithMockCustomPM;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { PostController.class })
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

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
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

        given(postTagCoreService.createPost(any(User.class), any(TrackNameEnum.class), any(Integer.class), any(PostCreateReq.class)))
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

        given(postTagCoreService.updatePost(any(User.class), any(TrackNameEnum.class), any(Integer.class),
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
}
