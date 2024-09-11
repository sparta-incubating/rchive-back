package kr.sparta.rchive.domain.user.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

    USER(Authority.USER),
    MANAGER(Authority.MANAGER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {

        public static final String USER = "ROLE_USER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

}
