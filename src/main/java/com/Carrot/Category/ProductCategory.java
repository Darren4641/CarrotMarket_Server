package com.Carrot.Category;

import com.Carrot.Role.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductCategory {
    DIGITAL_DEVICES("디지털기기"),
    HOUSEHOLD_APPLIANCES("생활가전"),
    FURNITURE_OR_INTERIOR("가구/인테리어"),
    LIVING_OR_KITCHEN("생활/주방"),
    CHILD("유아동"),
    CHILDRENBOOKS("유아도서"),
    WOMEN_CLOTHING("여성의류"),
    WOMEN_MISCELLANEOUS("여성잡화"),
    MEN_FASHION_OR_MISCELLANEOUS("남성패션/잡화"),
    BEAUTY("뷰티/미용"),
    SPORTS_OR_LEISURE("스포츠/레저"),
    HOBBIES_OR_GAMES_OR_DISCOGRAPHY("취미/게임/음반"),
    BOOKS("도서"),
    TICKET_OR_EXCHANGETICKET("티켓/교환권"),
    PROCESSED_FOOD("가공식품"),
    PET_SUPPLIES("반려동물용품"),
    PLANTS("식물"),
    OTHER("기타");

    private String value;

    @JsonCreator
    public static Role from(String s) {
        return Role.valueOf(s.toUpperCase());
    }
}
