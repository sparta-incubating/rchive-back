package kr.sparta.rchive.domain.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_education_data_track")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(EducationDataTrackId.class)

public class EducationDataTrack extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "educationData_id")
    private EducationData educationData;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

}
