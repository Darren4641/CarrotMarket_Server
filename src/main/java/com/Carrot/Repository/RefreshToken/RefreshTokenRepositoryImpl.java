package com.Carrot.Repository.RefreshToken;

import com.Carrot.Jwt.RefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int save(RefreshToken refreshToken) {
        return jdbcTemplate.update(
                "INSERT INTO `refreshToken` (`refreshToken`, `keyId`, `userAgent`)" +
                        "VALUES (?, ?, ?)",
                refreshToken.getRefreshToken(),
                refreshToken.getKeyId(),
                refreshToken.getUserAgent()
        );
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM `refreshToken` WHERE `refreshToken` = ?",
                new Object[]{refreshToken},
                (rs, rowNum) ->
                        Optional.of(new RefreshToken(
                                rs.getDouble("refreshTokenId"),
                                rs.getString("refreshToken"),
                                rs.getString("keyId"),
                                rs.getString("userAgent")
                        ))
        );
    }

    @Override
    public boolean existsByKeyIdAndUserAgent(String userId, String userAgent) {
        try {
            Optional<RefreshToken> refreshToken = jdbcTemplate.queryForObject(
                    "SELECT * FROM `refreshToken` WHERE `keyId` = ? and userAgent = ?",
                    new Object[]{userId, userAgent},
                    (rs, rowNum) ->
                            Optional.of(new RefreshToken(
                                    rs.getDouble("refreshTokenId"),
                                    rs.getString("refreshToken"),
                                    rs.getString("keyId"),
                                    rs.getString("userAgent")
                            ))
            );
            return true;
        } catch(EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void deleteByKeyIdAndUserAgent(String userId, String userAgent) {
        jdbcTemplate.update("DELETE FROM `refreshToken` WHERE `keyId` = ? AND `userAgent` = ?",
                userId, userAgent);
    }
}
