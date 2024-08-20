package kr.sparta.rchive.domain.user.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import kr.sparta.rchive.domain.user.entity.QRole;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.OrderRoleListEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleRepositoryCustomImpl implements RoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public int countByRoleRequestByPm(
            TrackNameEnum trackName, Integer searchPeriod, AuthEnum auth) {
        QRole role = QRole.role;

        return Objects.requireNonNull(queryFactory
                .select(role.count())
                .from(role)
                .where(role.track.trackName.eq(trackName)
                        .and(searchPeriod != null ? role.track.period.eq(searchPeriod) : null)
                        .and(role.track.period.ne(0))
                        .and(auth != null ? role.auth.eq(auth) : role.auth.ne(AuthEnum.REJECT))
                )
                .fetchFirst()).intValue();
    }

    @Override
    public int countByRoleRequestByApm(
            TrackNameEnum trackName, int period, AuthEnum auth) {
        QRole role = QRole.role;

        return Objects.requireNonNull(queryFactory
                .select(role.count())
                .from(role)
                .where(role.track.trackName.eq(trackName)
                        .and(role.track.period.eq(period))
                        .and(role.trackRole.ne(TrackRoleEnum.APM))
                        .and(auth != null ? role.auth.eq(auth) : role.auth.ne(AuthEnum.REJECT))
                )
                .fetchFirst()).intValue();
    }

    @Override
    public List<Role> findRoleListInBackOfficeAuthNoRejectByPm(
            TrackNameEnum trackName, Integer searchPeriod, String email, TrackRoleEnum trackRole,
            OrderRoleListEnum sort) {

        QRole role = QRole.role;

        return queryFactory
                .select(role)
                .from(role)
                .where(role.track.trackName.eq(trackName)
                        .and(searchPeriod != null ? role.track.period.eq(searchPeriod) : null)
                        .and(role.track.period.ne(0))
                        .and(email != null ? role.user.email.eq(email) : null)
                        .and(trackRole != null ? role.trackRole.eq(trackRole) : null)
                        .and(role.auth.ne(AuthEnum.REJECT))
                )
                .orderBy(roleListOrderSpecifier(sort, role))
                .fetch();
    }

    public List<Role> findRoleListInBackOfficeAuthNoRejectByApm(
            TrackNameEnum trackName, Integer period, String email, TrackRoleEnum trackRole,
            OrderRoleListEnum sort) {

        QRole role = QRole.role;

        return queryFactory
                .select(role)
                .from(role)
                .where(role.track.trackName.eq(trackName)
                        .and(role.track.period.eq(period))
                        .and(email != null ? role.user.email.eq(email) : null)
                        .and(trackRole != null ? role.trackRole.eq(trackRole) : null)
                        .and(role.trackRole.ne(TrackRoleEnum.APM))
                        .and(role.auth.ne(AuthEnum.REJECT))
                )
                .orderBy(roleListOrderSpecifier(sort, role))
                .fetch();
    }

    public List<Role> findRoleListInBackOfficeByPm(
            TrackNameEnum trackName, Integer searchPeriod,
            AuthEnum auth, String email, TrackRoleEnum trackRole, OrderRoleListEnum sort) {

        QRole role = QRole.role;

        return queryFactory
                .select(role)
                .from(role)
                .where(role.track.trackName.eq(trackName)
                        .and(searchPeriod != null ? role.track.period.eq(searchPeriod) : null)
                        .and(role.track.period.ne(0))
                        .and(email != null ? role.user.email.eq(email) : null)
                        .and(trackRole != null ? role.trackRole.eq(trackRole) : null)
                        .and(auth != null ? role.auth.eq(auth) : null)
                )
                .orderBy(roleListOrderSpecifier(sort, role))
                .fetch();
    }

    public List<Role> findRoleListInBackOfficeByApm(
            TrackNameEnum trackName, Integer period,
            AuthEnum auth, String email, TrackRoleEnum trackRole, OrderRoleListEnum sort) {

        QRole role = QRole.role;

        return queryFactory
                .select(role)
                .from(role)
                .where(role.track.trackName.eq(trackName)
                        .and(role.track.period.eq(period))
                        .and(email != null ? role.user.email.eq(email) : null)
                        .and(trackRole != null ? role.trackRole.eq(trackRole) : null)
                        .and(role.trackRole.ne(TrackRoleEnum.APM))
                        .and(auth != null ? role.auth.eq(auth) : null)
                )
                .orderBy(roleListOrderSpecifier(sort, role))
                .fetch();
    }

    private OrderSpecifier<?>[] roleListOrderSpecifier(OrderRoleListEnum sort, QRole role) {
        if (sort.equals(OrderRoleListEnum.NAME_ALPHABETICALLY)) {
            return new OrderSpecifier[]{role.user.username.asc(), role.createdAt.desc()};
        }
        return new OrderSpecifier[]{role.createdAt.desc()};
    }
}
