package kr.sparta.rchive.domain.post.service;

import java.util.List;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.PostTag;
import kr.sparta.rchive.domain.post.entity.Tag;
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
        return postTagRepository.findPostTagListByTagIdAlive(tagId);
    }

    public void savePostTagByPostAndTagList(Post post, List<Tag> tagList) {
        List<PostTag> postTagList = tagList.stream()
                .map(
                        tag -> PostTag.builder()
                                .post(post)
                                .tag(tag)
                                .build()
                )
                .toList();

        postTagRepository.saveAll(postTagList);
    }

    public void updatePostTagByPostAndTag(Post updatePost, List<Tag> tagList) {
        List<PostTag> existingPostTag = findPostTagByPostId(updatePost.getId());
        postTagRepository.deleteAll(existingPostTag);
        savePostTagByPostAndTagList(updatePost, tagList);
    }

    private List<PostTag> findPostTagByPostId(Long postId) {
        return postTagRepository.findByPostId(postId);
    }
}
