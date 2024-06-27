package kr.sparta.rchive.domain.post.service;

import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.post.dto.response.TagCreateRes;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
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

    public List<TagSearchRes> searchTag(String tag) {

        List<TagSearchRes> responseList = new ArrayList<>();

        List<Tag> tagList = tagRepository.findByTagNameContains(tag);

        for(Tag t : tagList) {
            TagSearchRes tagSearchRes = TagSearchRes.builder().tagId(t.getId()).tagName(t.getTagName()).build();
            responseList.add(tagSearchRes);
        }

        return responseList;
    }

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

    public Tag findTagBytagName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }

    public Boolean tagExist(Tag tag) {
        if(tag == null) {
            return false;
        }

        return true;
    }
}
