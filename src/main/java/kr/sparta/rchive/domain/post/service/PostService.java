package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.enums.DataTypeEnum;
import kr.sparta.rchive.domain.post.exception.PostCustomExeption;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    
    // 교육자료 테이블에서 ID를 이용하여 검색하는 로직
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostCustomExeption(PostExceptionCode.NOT_FOUND_POST_NOT_EXIST)
        );
    }

    public Post createVideoPost(PostCreateReq request, DataTypeEnum dataType) {
        Post createPost = Post.builder()
                .postType(request.postType())
                .title(request.title())
                .tutor(request.tutor())
                .uploadedAt(request.uploadedAt())
                .dataType(dataType)
                .dataLink(request.videoLink())
                .build();

        return postRepository.save(createPost);
    }

    public Post createContentPost(PostCreateReq request, DataTypeEnum dataType) {
        Post createPost = Post.builder()
                .postType(request.postType())
                .title(request.title())
                .tutor(request.tutor())
                .uploadedAt(request.uploadedAt())
                .dataType(dataType)
                .dataLink(request.contentLink())
                .build();

        return postRepository.save(createPost);
    }

    public void updateConnectData(Post contentPost, Post videoPost) {

        Post contentPostInsertConnectId = Post.builder()
                .id(contentPost.getId())
                .postType(contentPost.getPostType())
                .title(contentPost.getTitle())
                .tutor(contentPost.getTutor())
                .uploadedAt(contentPost.getUploadedAt())
                .dataType(contentPost.getDataType())
                .dataLink(contentPost.getDataLink())
                .connectDataId(videoPost.getId())
                .build();

        Post videoPostInsertConnectId = Post.builder()
                .id(videoPost.getId())
                .postType(videoPost.getPostType())
                .title(videoPost.getTitle())
                .tutor(videoPost.getTutor())
                .uploadedAt(videoPost.getUploadedAt())
                .dataType(videoPost.getDataType())
                .dataLink(videoPost.getDataLink())
                .connectDataId(contentPost.getId())
                .build();

        postRepository.save(contentPostInsertConnectId);
        postRepository.save(videoPostInsertConnectId);
    }
}
