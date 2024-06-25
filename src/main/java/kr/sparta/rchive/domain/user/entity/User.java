package kr.sparta.rchive.domain.user.entity;

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
import kr.sparta.rchive.domain.user.enums.GenderEnum;
import kr.sparta.rchive.domain.user.enums.OauthTypeEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="tb_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true)
    private String oauthId;

    @Column(length = 20)
    @Enumerated(value = EnumType.STRING)
    private OauthTypeEnum oauthType;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(length = 20)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user")
    List<Role> roleList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Comment> commentList = new ArrayList<>();
}
