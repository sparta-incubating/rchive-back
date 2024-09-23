package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.PostTag;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.repository.PostTagRepository;
import kr.sparta.rchive.test.PostTagTest;
import kr.sparta.rchive.test.PostTest;
import kr.sparta.rchive.test.TagTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PostTagServiceTest implements TagTest, PostTest, PostTagTest {

    @InjectMocks
    private PostTagService postTagService;

    @Mock
    private PostTagRepository postTagRepository;

    @Test
    @DisplayName("게시물 ID 리스트를 태그 ID로 찾아오는 서비스 로직 성공 테스트")
    void 게시물_ID_리스트_태그_ID로_찾아오는_서비스_성공_테스트() {
        // Given
        Long tagId = TEST_TAG_1L_ID;
        List<Long> postIdList = List.of(1L, 2L, 3L);

        given(postTagRepository.findPostTagListByTagIdAlive(any(Long.class))).willReturn(postIdList);
        // When
        List<Long> result = postTagService.findPostIdByTagIdAndIsDeletedFalse(tagId);

        // Then
        assertThat(result.size()).isEqualTo(postIdList.size());
        assertThat(result.get(0)).isEqualTo(postIdList.get(0));
    }

    @Test
    @DisplayName("PostTag를 Post와 TagList로 저장하는 서비스 로직 성공 테스트")
    void PostTag_Post_TagList_저장하는_서비스_성공_테스트() {
        // Given
        Post post = TEST_POST_1L;
        List<Tag> tagList = List.of(TEST_1L_TAG);
        List<PostTag> postTagList = List.of(TEST_POST_TAG_1);

        // When
        postTagService.savePostTagByPostAndTagList(post, tagList);

        // Then
        verify(postTagRepository, times(1)).saveAll(anyList());
    }
}
