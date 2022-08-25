package com.Carrot.Jwt;


import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private Double refreshTokenId;

    private String refreshToken;

    private String keyId;

    private String userAgent;
}
