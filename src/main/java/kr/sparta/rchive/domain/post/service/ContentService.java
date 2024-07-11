package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.entity.Content;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {
    private final ContentRepository contentRepository;

    public void createContent(String content, Post post) {
        Content createContent = Content.builder()
                .content(content)
                .seq(1)
                .post(post)
                .build();

        contentRepository.save(createContent);
    }

    public void updateContent(String content, Post modifyPost) {
        List<Content> findContentList = findContentByPostId(modifyPost.getId());

        if(isContentExist(findContentList)) {
            contentRepository.deleteAll(findContentList);
        }

        createContent(content, modifyPost);
    }

    private List<Content> findContentByPostId(Long postId) {
        return contentRepository.findByPostId(postId);
    }

    private Boolean isContentExist(List<Content> contentList) {
        return !contentList.isEmpty();
    }
}
