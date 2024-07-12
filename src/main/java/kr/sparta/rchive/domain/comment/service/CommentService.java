package kr.sparta.rchive.domain.comment.service;

import kr.sparta.rchive.domain.comment.dto.response.CommentRes;
import kr.sparta.rchive.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentRes> findCommentResListByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> CommentRes.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
