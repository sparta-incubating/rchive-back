package kr.sparta.rchive.domain.post.entity;

import jakarta.persistence.*;
import kr.sparta.rchive.domain.user.entity.Track;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "tb_post")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tutor_name", nullable = false, length = 20)
    private String tutorName;

    @Builder.Default
    @OneToMany(mappedBy = "tutor")
    private List<Post> posts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;
}
