package com.Carrot.Category;

import com.Carrot.Role.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NeighborhoodCategory {
    NEIGHBORHOOD_QUESTION("동네질문"),
    NEIGHBORHOOD_ACCIDENT("동네사건사고"),
    WEATHER("실시간 동네날씨"),
    RESTAURANT("동네맛집"),
    NEWS_OF_NEIGHBORHOOD("동네소식"),
    HOBBY("취미생활"),
    LOSTCENTER("분실/실종센터"),
    PLEASE("해주세요"),
    DAILY("일상"),
    PHOTO_EXHIBITION("동네사진전");

    private String value;

    @JsonCreator
    public static Role from(String s) {
        return Role.valueOf(s.toUpperCase());
    }
}
