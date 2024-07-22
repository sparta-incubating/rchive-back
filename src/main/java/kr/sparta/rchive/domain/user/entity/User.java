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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.user.enums.GenderEnum;

import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name="tb_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true)
    private String oAuthId;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private OAuthTypeEnum oAuthType;

    @Column(nullable = false, length = 50, unique = true)
    @Email(message = "올바르지 않은 이메일 형식")
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, length = 20)
    @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
    private String phone;

    @Column(nullable = true, length = 20)
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    private String profileImg;

    @Column(nullable = true, length = 20, unique = true)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리")
    private String nickname;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole;

    @Column(nullable = false)
    private Boolean termUserAge;

    @Column(nullable = false)
    private Boolean termUseService;

    @Column(nullable = false)
    private Boolean termPersonalInfo;

    @Column(nullable = false)
    private Boolean termAdvertisement;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<Role> roleList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<Comment> commentList = new ArrayList<>();

    public void delete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateProfileByUser(String profileImg, String nickname){
        this.profileImg = profileImg;
        this.nickname = nickname;
    }

    public void updateProfileByManager(String profileImg){
        this.profileImg = profileImg;
    }
}
