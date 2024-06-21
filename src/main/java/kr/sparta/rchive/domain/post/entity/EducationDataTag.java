package kr.sparta.rchive.domain.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_education_data_tag")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(EducationDataTagId.class)
public class EducationDataTag extends BaseTimeEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "educationData_id")
    private EducationData educationData;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
