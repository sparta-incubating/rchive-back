package kr.sparta.rchive.domain.user.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import kr.sparta.rchive.domain.user.entity.QRole;
import kr.sparta.rchive.domain.user.entity.QTrack;
import kr.sparta.rchive.domain.user.entity.QUser;
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
                        .and(auth != null ? role.auth.eq(auth) : role.auth.ne(AuthEnum.REJECT))
                )
                .fetchFirst()).intValue();
    }

    @Override
    public List<Role> findRoleListInBackOfficeAuthNoRejectByPm(
            TrackNameEnum trackName, Integer searchPeriod, String keyword, TrackRoleEnum trackRole,
            OrderRoleListEnum sort) {

        QRole role = QRole.role;
        QUser user = QUser.user;
        QTrack track = QTrack.track;

        return queryFactory
                .select(role)
                .from(role)
                .join(role.user, user).fetchJoin()
                .join(role.track, track).fetchJoin()
                .where(
                        track.trackName.eq(trackName),
                        searchPeriod != null ? track.period.eq(searchPeriod) : null,
                        track.period.ne(0),
                        (keyword != null) ? user.email.contains(keyword).or(user.username.contains(keyword)) : null,
                        trackRole != null ? role.trackRole.eq(trackRole) : null,
                        role.auth.ne(AuthEnum.REJECT)
                )
                .orderBy(roleListOrderSpecifier(sort, role))
                .fetch();
    }

    public List<Role> findRoleListInBackOfficeAuthNoRejectByApm(
            TrackNameEnum trackName, Integer period, String keyword, TrackRoleEnum trackRole,
            OrderRoleListEnum sort) {

        QRole role = QRole.role;
        QTrack track = QTrack.track;
        QUser user = QUser.user;

        return queryFactory
                .select(role)
                .from(role)
                .join(role.user, user).fetchJoin()
                .join(role.track, track).fetchJoin()
                .where(
                        track.trackName.eq(trackName),
                        track.period.eq(period),
                        keyword != null ? user.email.contains(keyword).or(user.username.contains(keyword)) : null,
                        trackRole != null ? role.trackRole.eq(trackRole) : null,
                        role.trackRole.ne(TrackRoleEnum.APM),
                        role.auth.ne(AuthEnum.REJECT)
                )
                .orderBy(roleListOrderSpecifier(sort, role))
                .fetch();
    }

    public List<Role> findRoleListInBackOfficeByPm(
            TrackNameEnum trackName, Integer searchPeriod,
            AuthEnum auth, String keyword, TrackRoleEnum trackRole, OrderRoleListEnum sort) {

        QRole role = QRole.role;
        QTrack track = QTrack.track;
        QUser user = QUser.user;

        return queryFactory
                .select(role)
                .from(role)
                .join(role.user, user).fetchJoin()
                .join(role.track, track).fetchJoin()
                .where(
                        track.trackName.eq(trackName),
                        searchPeriod != null ? track.period.eq(searchPeriod) : null,
                        track.period.ne(0),
                        keyword != null ? user.email.contains(keyword).or(user.username.contains(keyword)) : null,
                        trackRole != null ? role.trackRole.eq(trackRole) : null,
                        auth != null ? role.auth.eq(auth) : null
                )
                .orderBy(roleListOrderSpecifier(sort, role))
                .fetch();
    }

    public List<Role> findRoleListInBackOfficeByApm(
            TrackNameEnum trackName, Integer period,
            AuthEnum auth, String keyword, TrackRoleEnum trackRole, OrderRoleListEnum sort) {

        QRole role = QRole.role;
        QTrack track = QTrack.track;
        QUser user = QUser.user;

        return queryFactory
                .select(role)
                .from(role)
                .join(role.user, user).fetchJoin()
                .join(role.track, track).fetchJoin()
                .where(
                        track.trackName.eq(trackName),
                        track.period.eq(period),
                        keyword != null ? user.email.contains(keyword).or(user.username.contains(keyword)) : null,
                        trackRole != null ? role.trackRole.eq(trackRole) : null,
                        role.trackRole.ne(TrackRoleEnum.APM),
                        auth != null ? role.auth.eq(auth) : null
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
