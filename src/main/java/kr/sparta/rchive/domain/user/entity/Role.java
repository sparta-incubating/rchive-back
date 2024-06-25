package kr.sparta.rchive.domain.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_role")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(RoleId.class)
public class Role extends BaseTimeEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private TrackRoleEnum role;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private AuthEnum auth;

}
