package kr.sparta.rchive.domain.post.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@Table(name = "tb_post")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostTypeEnum postType;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDate uploadedAt;

    @Column
    private String thumbnailUrl;

    @Column(name = "video_link")
    private String videoLink;

    @Column(name = "content_link")
    private String contentLink;

    @Column(length = 65535)
    private String content;

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

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<PostTag> postTagList = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToOne()
    @JoinColumn(name = "track_id")
    private Track track;

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void update(PostUpdateReq request, Track track, Tutor tutor) {
        this.postType = request.postType() == null ? this.postType : request.postType();
        this.title = request.title() == null ? this.title : request.title();
        this.uploadedAt = request.uploadedAt() == null ? this.uploadedAt : request.uploadedAt();
        this.thumbnailUrl = request.thumbnailUrl() == null ? this.thumbnailUrl : request.thumbnailUrl();
        this.videoLink = request.videoLink() == null ? this.videoLink : request.videoLink();
        this.contentLink = request.contentLink() == null ? this.contentLink : request.contentLink();
        this.content = request.content() == null ? this.content : request.content();
        this.isOpened = request.isOpened() == null ? this.isOpened : request.isOpened();
        this.tutor = tutor == null? this.tutor : tutor;
        this.track = track == null ? this.track : track;
    }

    public void openPost() {
        this.isOpened = true;
    }

    public void closePost() {
        this.isOpened = false;
    }
}