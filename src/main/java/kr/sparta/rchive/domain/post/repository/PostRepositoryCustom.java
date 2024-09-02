package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

import java.time.LocalDate;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findPostListInBackOfficePostTypeAllByPm(String title, LocalDate startDate, LocalDate endDate, Boolean isOpened, Integer searchPeriod, TrackNameEnum trackName, Long tutorId);

    List<Post> findPostListInBackOfficePostTypeAllByApm(String title, LocalDate startDate, LocalDate endDate, Boolean isOpened, Long trackId, Long tutorId);

    List<Post> findPostListInBackOfficePostTypeNotNullByPM(PostTypeEnum postType, String title, LocalDate startDate, LocalDate endDate, Integer searchPeriod, Boolean isOpened, TrackNameEnum trackName, Long tutorId);

    List<Post> findPostListInBackOfficePostTypeNotNullApm(PostTypeEnum postType, String title, LocalDate startDate, LocalDate endDate, Long trackId, Boolean isOpened, Long tutorId);

    List<Post> findAllByPostTypeAndTrackIdUserRoleUser(PostTypeEnum postType, Long trackId, Long tutorId);

    List<Post> findAllByPostTypeAndTrackIdUserRoleManager(PostTypeEnum postType, Long trackId, Long tutorId);

    List<Post> findPostListByTagIdAndTrackIdWithTagList(Long tagId, Long trackId);

    List<Post> findPost(PostTypeEnum postType, String keyword, Long tutorId, Long trackId);

    Post findPostDetail(Long postId);
}
