package kr.sparta.rchive.domain.bookmark.entity;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class BookmarkId implements Serializable {
    private Long user;
    private Long post;
}
