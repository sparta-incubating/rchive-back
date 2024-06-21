package kr.sparta.rchive.domain.post.entity;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class EducationDataTrackId implements Serializable {
    private Long educationData;
    private Long track;
}
