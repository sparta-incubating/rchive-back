package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

import java.time.LocalDate;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findPostListInBackOfficePostTypeAllByPm(LocalDate startDate, LocalDate endDate, Boolean isOpened, Integer searchPeriod, TrackNameEnum trackName, Long tutorId);

    List<Post> findPostListInBackOfficePostTypeAllByApm(LocalDate startDate, LocalDate endDate, Boolean isOpened, Long trackId, Long tutorId);

    List<Post> findPostListInBackOfficePostTypeNotNullByPM(PostTypeEnum postType, LocalDate startDate, LocalDate endDate, Integer searchPeriod, Boolean isOpened, TrackNameEnum trackName, Long tutorId);

    List<Post> findPostListInBackOfficePostTypeNotNullApm(PostTypeEnum postType, LocalDate startDate, LocalDate endDate, Long trackId, Boolean isOpened, Long tutorId);

    List<Post> findAllByPostTypeAndTrackIdUserRoleUser(PostTypeEnum postType, Long trackId);

    List<Post> findAllByPostTypeAndTrackIdUserRoleManager(PostTypeEnum postType, Long trackId);

    List<Post> findPostListByTagIdAndTrackIdWithTagList(Long tagId, Long trackId);

    List<Post> findPostListBySearchTypeContentAndKeywordAndTrack(String keyword, Long trackId);

    List<Post> findPostListBySearchTypeTitleAndKeywordAndTrack(String keyword, Long trackId);

    List<Post> findPostListBySearchTypeTutorAndKeywordAndTrack(String keyword, Long trackId);

    List<Post> findPostListBySearchTypeTagAndKeywordAndTrack(String keyword, Long trackId);

    List<Post> findPostListBySearchTypeContentAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId);

    List<Post> findPostListBySearchTypeTitleAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId);

    List<Post> findPostListBySearchTypeTagAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId);

    List<Post> findPostListBySearchTypeTutorAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId);
}
