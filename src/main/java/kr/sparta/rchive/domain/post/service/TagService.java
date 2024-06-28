package kr.sparta.rchive.domain.post.service;

import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.post.dto.response.PostSearchByTagRes;
import kr.sparta.rchive.domain.post.dto.response.TagCreateRes;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.entity.EducationData;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.exception.PostCustomExeption;
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

        List<TagSearchRes> responseList = new ArrayList<>();

        List<Tag> tagList = tagRepository.findByTagNameContains(tagName);

        for(Tag t : tagList) {
            TagSearchRes tagSearchRes = TagSearchRes.builder().tagId(t.getId()).tagName(t.getTagName()).build();
            responseList.add(tagSearchRes);
        }

        return responseList;
    }

    // 태그를 추가하는 로직
    @Transactional
    public TagCreateRes createTag(String name) {

        String lowerName = name.toLowerCase();

        Tag findTag = findTagBytagName(lowerName);

        if(tagExist(findTag)) {
            throw new PostCustomExeption(PostExceptionCode.CONFLICT_TAG);
        }

        Tag createTag = Tag.builder().tagName(lowerName).build();

        Tag savedTag = tagRepository.save(createTag);

        return TagCreateRes.builder().tagId(savedTag.getId()).tagName(savedTag.getTagName()).build();
    }

    // 태그 이름으로 태그 검색해오는 로직
    public Tag findTagBytagName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }

    // 태그가 존재하는지 체크하는 로직
    public Boolean tagExist(Tag tag) {
        if(tag == null) {
            return false;
        }

        return true;
    }

    // 태그 ID로 태그를 검색해오는 로직
    public Tag findTagById(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(
                () -> new PostCustomExeption(PostExceptionCode.NOT_FOUND_TAG_NOT_EXIST)
        );
    }
}
