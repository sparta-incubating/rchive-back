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

        List<Tag> tagList = tagRepository.findByTagContains(tag);

        for(Tag t : tagList) {
            TagSearchRes tagSearchRes = TagSearchRes.builder().tagId(t.getId()).tag(t.getTag()).build();
            responseList.add(tagSearchRes);
        }

        return responseList;
    }

    @Transactional
    public TagCreateRes createTag(String tag) {
        if(findTagBytag(tag) != null) {
            throw new PostCustomExeption(PostExceptionCode.CONFLICT_TAG);
        }

        Tag createTag = Tag.builder().tag(tag).build();

        Tag savedTag = tagRepository.save(createTag);

        return TagCreateRes.builder().tagId(savedTag.getId()).tag(savedTag.getTag()).build();
    }

    public Tag findTagBytag(String tag) {
        return tagRepository.findByTag(tag);
    }
}
