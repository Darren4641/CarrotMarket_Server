package com.Carrot.Jwt;


import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private int refreshTokenId;

    private String refreshToken;

    private String keyId;

    private String userAgent;
}
