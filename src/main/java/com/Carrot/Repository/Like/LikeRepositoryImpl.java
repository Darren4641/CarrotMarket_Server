package com.Carrot.Repository.Like;

import com.Carrot.CR_Model.Like;
import com.Carrot.CR_Model.SaleProduct;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class LikeRepositoryImpl implements LikeRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int save(Like like) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `carrotsql`.`like`(`id`,`category`,`postId`,`isLike`) " +
                    "VALUES (?,?,?,?)", new String[]{"likeId"});
            preparedStatement.setString(1, like.getId());
            preparedStatement.setString(2, like.getCategory());
            preparedStatement.setInt(3, like.getPostId());
            preparedStatement.setInt(4, like.getIsLike());

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(Like like) {
        return jdbcTemplate.update("UPDATE `carrotsql`.`like` SET `id` = ?, `category` = ?,`postId` = ?, `isLike` = ? WHERE `likeId` = ?",
                like.getId(),
                like.getCategory(),
                like.getPostId(),
                like.getIsLike(),
                like.getLikeId());
    }

    @Override
    public List<Like> findByPostIdAndCategory(int postId, String category) {
        List<Like> results = jdbcTemplate.query(
                "SELECT * FROM `carrotsql`.`like` WHERE `postId` = ? AND `category` = ?",
                new RowMapper<Like>() {
                    @Override
                    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Like like = Like.builder()
                                .likeId(rs.getInt("likeId"))
                                .id(rs.getString("id"))
                                .category(rs.getString("category"))
                                .postId(rs.getInt("postId"))
                                .isLike(rs.getInt("isLike")).build();
                        return like;
                    }
                }, postId, category);
        return results.isEmpty() ? null : results;
    }

    @Override
    public int countOfLike(int postId, String category) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM `carrotsql`.`like` WHERE `postId` = ? AND `category` = ?",
                    new Object[]{postId, category},
                    (rs, rowNum) -> Optional.of(rs.getInt(1))).get();

        } catch(EmptyResultDataAccessException e) {
            return 0;
        }

    }
}
