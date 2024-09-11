package kr.sparta.rchive.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrackNameEnum {
    ANDROID("Android"),
    DATA("Data"),
    REACT("React"),
    IOS("iOS"),
    NODEJS("Node.js"),
    SPRING_JAVA("Java"),
    SPRING_KOTLIN("Kotlin"),
    UNITY("Unity"),
    UXUI("UX/UI"),
    AI("AI"),
    SPRING_DEEP("Spring 단기심화");

    private final String value;
}
