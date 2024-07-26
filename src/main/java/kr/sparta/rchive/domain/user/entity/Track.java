package kr.sparta.rchive.domain.user.entity;

import jakarta.persistence.*;
import kr.sparta.rchive.domain.post.entity.Tutor;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_track")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private TrackNameEnum trackName;

    @Column(nullable = false)
    private Integer period;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isPermission;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "track")
    List<Role> roleList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "track")
    List<Tutor> tutorList = new ArrayList<>();
}
