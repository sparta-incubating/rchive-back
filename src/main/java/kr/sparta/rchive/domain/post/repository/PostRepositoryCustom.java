package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.enums.PostTypeEnum;

import java.time.LocalDate;
import java.util.List;

public interface PostRepositoryCustom {
    List<Long> findPostIdListByOption(PostTypeEnum postType, LocalDate uploadedAt, Integer period,
                                      String tutor, Boolean isOpened);

    List<Long> findPostIdListInBackOfficeByPM(PostTypeEnum postType, LocalDate uploadedAt,
                                              String tutor, Boolean isOpened);

}
