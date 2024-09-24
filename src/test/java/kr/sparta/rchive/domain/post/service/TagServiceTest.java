package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.TagCreateReq;
import kr.sparta.rchive.domain.post.dto.response.TagCreateRes;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.repository.TagRepository;
import kr.sparta.rchive.test.TagTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest implements TagTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Test
    @DisplayName("태그를 검색하는 서비스 로직 성공 테스트")
    void 태그_검색_서비스_성공_테스트() {
        // Given
        String tagName = TEST_TAG_1L_NAME;
        List<Tag> tagList = List.of(TEST_1L_TAG);

        List<TagSearchRes> responseList = tagList.stream().map(
            tag -> TagSearchRes.builder()
                .tagName(tag.getTagName())
                .tagId(tag.getId())
                .build()
        ).toList();

        given(tagRepository.findByTagNameContains(any(String.class))).willReturn(tagList);

        // When
        List<TagSearchRes> result = tagService.searchTag(tagName);

        // Then
        assertThat(result.size()).isEqualTo(responseList.size());
        assertThat(result.get(0)).isEqualTo(responseList.get(0));
    }

    @Test
    @DisplayName("태그 생성하는 서비스 로직 성공 테스트")
    void 태그_생성_서비스_성공_테스트() {
        // Given
        List<String> tagNameList = List.of("tagTest", "tagTest2");

        TagCreateReq request = TagCreateReq.builder()
            .tagNameList(tagNameList)
            .build();

        given(tagRepository.findByTagNameNotOptional(tagNameList.get(0).toLowerCase().trim())).willReturn(TEST_1L_TAG);
        given(tagRepository.findByTagNameNotOptional(tagNameList.get(1).toLowerCase().trim())).willReturn(null);
        given(tagRepository.save(any(Tag.class))).willReturn(TEST_2L_TAG);

        // When
        List<TagCreateRes> result = tagService.createTag(request);

        // Then
        assertThat(result.get(0).tagName()).isEqualTo(TEST_1L_TAG.getTagName());
        assertThat(result.get(1).tagName()).isEqualTo(TEST_2L_TAG.getTagName());
    }

    @Test
    @DisplayName("태그ID로 태그를 찾아오는 서비스 로직 성공 테스트")
    void 태그ID로_태그_찾아오는_서비스_성공_테스트() {
        // Given
        Long tagId = TEST_TAG_1L_ID;
        Tag tag = TEST_1L_TAG;

        given(tagRepository.findById(any(Long.class))).willReturn(Optional.of(tag));

        // When
        Tag result = tagService.findTagById(tagId);

        // Then
        assertThat(result.getTagName()).isEqualTo(tag.getTagName());
    }

    @Test
    @DisplayName("태그ID로 태그를 찾아오는 서비스 로직 실패 테스트")
    void 태그ID로_태그_찾아오는_서비스_실패_테스트() {
        // Given
        Long tagId = TEST_TAG_1L_ID;

        given(tagRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // When
        PostCustomException exception = assertThrows(
            PostCustomException.class, () -> tagService.findTagById(tagId)
        );

        // Then
        assertThat(exception.getErrorCode()).isEqualTo("POST-4002");
    }

    @Test
    @DisplayName("태그 내용 리스트로 태그 ID를 찾아오는 서비스 로직 성공 테스트")
    void 태그내용_리스트_태그ID_찾아오는_서비스_성공_테스트() {
        // Given
        List<String> tagNameList = List.of(TEST_TAG_1L_NAME, TEST_TAG_2L_NAME, TEST_TAG_3L_NAME);
        List<Tag> tagList = List.of(TEST_1L_TAG, TEST_2L_TAG);

        given(tagRepository.findByTagName(tagNameList.get(0))).willReturn(Optional.of(TEST_1L_TAG));
        given(tagRepository.findByTagName(tagNameList.get(1))).willReturn(Optional.of(TEST_2L_TAG));
        given(tagRepository.findByTagName(tagNameList.get(2))).willReturn(Optional.empty());

        // When
        List<Tag> result = tagService.findTagIdListByTagNameList(tagNameList);

        // Then
        assertThat(result.size()).isEqualTo(tagList.size());
        assertThat(result.get(0).getTagName()).isEqualTo(TEST_TAG_1L_NAME);
        assertThat(result.get(1).getTagName()).isEqualTo(TEST_TAG_2L_NAME);
    }
}
