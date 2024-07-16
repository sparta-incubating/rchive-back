package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;

import java.time.LocalDate;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findPostListInBackOfficePostTypeAllByPM(LocalDate startDate, LocalDate endDate, Boolean isOpened, Integer searchPeriod);

    List<Post> findPostListInBackOfficePostTypeAllByNotPM(LocalDate startDate, LocalDate endDate, Boolean isOpened, Integer period);

    List<Post> findPostListInBackOfficePostTypeNotNullByPM(PostTypeEnum postType, LocalDate startDate, LocalDate endDate, Integer searchPeriod, Boolean isOpened);

    List<Post> findPostListInBackOfficePostTypeNotNullByNotPM(PostTypeEnum postType, LocalDate startDate, LocalDate endDate, Integer period, Boolean isOpened);
}
