package kr.sparta.rchive.domain.post.service;

import kr.sparta.rchive.domain.post.dto.response.TutorRes;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.TutorRepository;
import kr.sparta.rchive.domain.user.entity.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;

    public Tutor findTutorById(Long tutorId) {
        return tutorRepository.findById(tutorId).orElseThrow(
                () -> new PostCustomException(PostExceptionCode.NOT_FOUND_TUTOR)
        );
    }

    public Tutor checkTutor(Long tutorId, Track track) {
        Tutor tutor = findTutorById(tutorId);
        if(tutor.getTrack() != track) {
            throw new PostCustomException(PostExceptionCode.BAD_REQUEST_NOT_TRACK_TUTOR);
        }
        return tutor;
    }

    public List<TutorRes> findTutorListByTutorNameAndTrackId(String tutorName, Long trackId) {
        return tutorRepository.findTutorList(tutorName, trackId).stream()
                .map(tutor -> TutorRes.builder()
                        .tutorId(tutor.getId())
                        .tutorName(tutor.getTutorName())
                        .build())
                .toList();
    }
}
