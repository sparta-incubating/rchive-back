package kr.sparta.rchive.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.post.enums.DataTypeEnum;
import kr.sparta.rchive.domain.post.enums.EducationTypeEnum;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "tb_education_data")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EducationData extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "education_type", nullable = false)
    private EducationTypeEnum educationType;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 20)
    private String tutor;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDate uploadedAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private DataTypeEnum dataType;

    @Column(nullable = false)
    private String content;

    @Column(name = "connect_data_id")
    private Long connectDataId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer hits;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isOpened;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "educationData")
    List<Comment> commentList = new ArrayList<>();

}