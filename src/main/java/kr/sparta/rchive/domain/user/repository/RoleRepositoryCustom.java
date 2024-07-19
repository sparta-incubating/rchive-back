package kr.sparta.rchive.domain.user.repository;

import java.util.List;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;

public interface RoleRepositoryCustom {

    int countByRoleRequestByPm(
            TrackNameEnum trackName, Integer searchPeriod, AuthEnum auth);

    int countByRoleRequestByApm(
            TrackNameEnum trackName, int period, AuthEnum auth);

    List<Role> findRoleListInBackOfficeAuthNoRejectByPm(
            TrackNameEnum trackName, Integer searchPeriod, String email, TrackRoleEnum trackRole);

    List<Role> findRoleListInBackOfficeAuthNoRejectByApm(
            TrackNameEnum trackName, Integer period, String email, TrackRoleEnum trackRole);

    List<Role> findRoleListInBackOfficeByPm(TrackNameEnum trackName, Integer searchPeriod,
            AuthEnum auth, String email, TrackRoleEnum trackRole);

    List<Role> findRoleListInBackOfficeByApm(TrackNameEnum trackName, Integer period,
            AuthEnum auth, String email, TrackRoleEnum trackRole);

}
