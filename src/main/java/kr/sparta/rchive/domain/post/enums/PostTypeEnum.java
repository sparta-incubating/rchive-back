package kr.sparta.rchive.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostTypeEnum {
    Sparta_Lecture("강의자료"),
    Special_Lecture("특강/실시간 세션"),
    Level_Challenge("챌린지"),
    Level_Standard("스탠다드"),
    Level_Basic("베이직"),
    Level_All("수준별강의"),
    Project_Description("과제해설");

     private final String name;
}
