package com.Carrot.Repository.RefreshToken;

import com.Carrot.Jwt.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    int save(RefreshToken refreshToken);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    boolean existsByKeyIdAndUserAgent(String userEmail, String userAgent);
    void deleteByKeyIdAndUserAgent(String userEmail, String userAgent);
}
