package kr.sparta.rchive.domain.post.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.entity.PostTag;
import kr.sparta.rchive.domain.post.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostTagService {

    private final PostTagRepository postTagRepository;

    // 교육자료태그 테이블에서 태그 ID를 이용하여 태그가 붙어있는 교육자료들을 List로 가져오는 로직
    public List<Long> findPostIdByTagIdAndIsDeletedFalse(Long tagId) {
        return postTagRepository.findPostTagListByTagIdAndIsDeletedFalse(tagId)
                .stream()
                .map(postTag -> postTag.getPost().getId())
                .toList();
    }

    // 교육자료에 붙어있는 모든 태그들을 가져오는 로직
    public List<PostTag> findPostTagByPostIdList(List<Long> postIdList) {
        return postTagRepository.findByPostIdIn(postIdList);
    }

    public Map<Long, List<Long>> findPostTagListByTagId(List<Long> postIdList) {
        List<PostTag> postTagList = findPostTagByPostIdList(postIdList);

        // 교육자료 ID를 Key로 태그 List를 Value로 갖는 Map 형식으로 변환
        return postTagList.stream()
                .collect(Collectors.groupingBy(
                        postTag -> postTag.getPost().getId(),
                        Collectors.mapping(postTag -> postTag.getTag().getId(), Collectors.toList())
                ));
    }
}
