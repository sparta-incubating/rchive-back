package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.repository.TagRepository;
import kr.sparta.rchive.test.TagTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
}
