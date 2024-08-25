package kr.sparta.rchive.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.PostBookmarkCoreService;
import kr.sparta.rchive.domain.core.service.PostCommentCoreService;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.security.WithMockCustomUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class})
@ActiveProfiles("test")
@WithMockCustomUser
public class PostControllerTestForUser implements PostTest, TutorTest, TagTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

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
        for(int i = 0; i < 2; i++) {
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
}
