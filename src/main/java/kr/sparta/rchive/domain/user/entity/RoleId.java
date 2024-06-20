package kr.sparta.rchive.domain.user.entity;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class RoleId implements Serializable {
    private Long user;
    private Long track;
}
