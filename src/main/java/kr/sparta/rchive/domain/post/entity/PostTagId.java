package kr.sparta.rchive.domain.post.entity;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class PostTagId implements Serializable {
    private Long post;
    private Long tag;
}
