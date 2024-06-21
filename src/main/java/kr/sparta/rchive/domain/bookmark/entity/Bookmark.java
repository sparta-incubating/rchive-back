package kr.sparta.rchive.domain.bookmark.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.sparta.rchive.domain.post.entity.EducationData;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_bookmark")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BookmarkId.class)
public class Bookmark extends BaseTimeEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "education_data_id")
    private EducationData educationData;
}
