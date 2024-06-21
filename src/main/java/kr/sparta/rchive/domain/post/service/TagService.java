package kr.sparta.rchive.domain.post.service;

import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.entity.Tag;
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
}
