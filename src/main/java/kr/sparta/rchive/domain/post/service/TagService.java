package kr.sparta.rchive.domain.post.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.dto.response.TagCreateRes;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    // 내가 원하는 태그로 검색해오는 기능
    public List<TagSearchRes> searchTag(String tagName) {

        String lowerTagName = tagName.toLowerCase();

        List<Tag> tagList = tagRepository.findByTagNameContains(lowerTagName);

        return tagList.stream().map(
                tag -> TagSearchRes.builder()
                        .tagName(tag.getTagName())
                        .tagId(tag.getId())
                        .build()
        ).toList();
    }

    // 태그를 추가하는 로직
    @Transactional
    public TagCreateRes createTag(String name) {

        String lowerTagName = name.toLowerCase();

        Tag findTag = tagRepository.findByTagNameNotOptional(lowerTagName);

        if(tagExist(findTag)) {
            throw new PostCustomException(PostExceptionCode.CONFLICT_TAG);
        }

        Tag createTag = Tag.builder()
                .tagName(lowerTagName)
                .build();

        Tag savedTag = tagRepository.save(createTag);

        return TagCreateRes.builder()
                .tagId(savedTag.getId())
                .tagName(savedTag.getTagName())
                .build();
    }

    // 태그가 존재하는지 체크하는 로직
    private Boolean tagExist(Tag tag) {
        return tag != null;
    }

    // 태그 ID로 태그를 검색해오는 로직
    public Tag findTagById(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(
                () -> new PostCustomException(PostExceptionCode.NOT_FOUND_TAG)
        );
    }

    public List<Tag> findTagIdListByTagNameList(List<String> tagNameList) {
        return tagNameList.stream()
                .map(tagRepository::findByTagName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<String> findTagNameListBytagIdList(List<Long> tagIdList) {
        List<Tag> tagList = tagRepository.findAllByIdIn(tagIdList);
        return tagList.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
    }
}
