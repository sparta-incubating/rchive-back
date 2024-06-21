package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.post.service.EducationDataService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationDataTagCoreService {
    private final EducationDataService educationDataService;
    private final TagService tagService;
    private final UserService userService;
}
