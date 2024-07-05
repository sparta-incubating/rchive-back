package kr.sparta.rchive.domain.post.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.PostTrack;
import kr.sparta.rchive.domain.post.repository.PostTrackRepository;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostTrackService {

    private final PostTrackRepository postTrackRepository;

    // 교육자료 List를 트랙 ID로 조회하여 필요한 데이터만 남기는 로직
    public List<Long> filterPostIdListByTrackId(List<Long> postIdList, Long userTrackId) {
        return postIdList.stream().filter(
                post -> {
                    PostTrack pt = findByPostId(post);
                    return Objects.equals(pt.getTrack().getId(), userTrackId);
                }
        ).collect(Collectors.toList());
    }

    // 교육자료 List를 트랙 이름으로 조회하여 필요한 데이터만 남기는 로직
    public List<Long> filterPostIdListByTrackName(List<Long> postIdList, TrackEnum trackName) {
        return postIdList.stream().filter(
                post -> {
                    PostTrack pt = findByPostId(post);
                    return pt.getTrack().getTrackName().equals(trackName);
                }
        ).collect(Collectors.toList());
    }

    // 교육자료트랙 테이블에서 교육자료 ID를 이용하여 교육자료트랙 데이터를 검색해오는 로직
    public PostTrack findByPostId(Long postId) {
        return postTrackRepository.findByPostId(postId);
    }

    public void savePostTrackByPostAndTrack(Post post, Track track) {
        PostTrack postTrack = PostTrack.builder()
                .post(post)
                .track(track)
                .build();

        postTrackRepository.save(postTrack);
    }
}