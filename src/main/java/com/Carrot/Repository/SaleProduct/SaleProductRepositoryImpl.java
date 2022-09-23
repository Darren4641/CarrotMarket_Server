package com.Carrot.Repository.SaleProduct;

import com.Carrot.CR_Model.SaleProduct;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SaleProductRepositoryImpl implements SaleProductRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int save(SaleProduct saleProduct) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `saleProduct` (`id`, `title`, `category`, `price`, `content`, `status`, `createDate`, `updateDate`)" +
                    "VALUES (?,?,?,?,?,?,?,?)", new String[]{"postId"});
            preparedStatement.setString(1, saleProduct.getId());
            preparedStatement.setString(2, saleProduct.getTitle());
            preparedStatement.setString(3, saleProduct.getCategory());
            preparedStatement.setInt(4, saleProduct.getPrice());
            preparedStatement.setString(5, saleProduct.getContent());
            preparedStatement.setString(6, saleProduct.getStatus());
            preparedStatement.setTimestamp(7, getTime());
            preparedStatement.setTimestamp(8, getTime());

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(int postId, SaleProduct saleProduct) {
        return jdbcTemplate.update("UPDATE `saleProduct` SET `id` = ?, `title` = ?, `category` = ?, `price` = ?, `content` = ?, `status` = ?, `createDate` = ?, `updateDate` = ? WHERE `postId` = ?",
                saleProduct.getId(),
                saleProduct.getTitle(),
                saleProduct.getCategory(),
                saleProduct.getPrice(),
                saleProduct.getContent(),
                saleProduct.getStatus(),
                saleProduct.getCreateDate(),
                saleProduct.getUpdateDate(),
                postId);
    }

    @Override
    public int updateForUpdateDate(int postId) {
        return jdbcTemplate.update("UPDATE `saleProduct` SET `updateDate` = ? WHERE `postId` = ?",
                getTime(), postId);
    }

    @Override
    public List<SaleProduct> findAll() {
        return jdbcTemplate.query(
                "SELECT * from saleProduct AS sale JOIN Photo AS photo ON sale.postId = photo.postId ORDER BY sale.updateDate DESC",
                (rs, rowNum) ->
                        new SaleProduct(
                                rs.getInt("postId"),
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("category"),
                                rs.getInt("price"),
                                rs.getString("content"),
                                rs.getString("status"),
                                rs.getTimestamp("createDate"),
                                rs.getTimestamp("updateDate"),
                                rs.getString("filePath"),
                                rs.getString("fileDownloadPath")
                        )
        );
    }

    @Override
    public List<SaleProduct> findPageCount(int limit, int offset) {
        List<SaleProduct> results = jdbcTemplate.query(
                "SELECT distinct sale.postId, sale.id, sale.title, sale.category, sale.price, sale.price, sale.content, sale.`status`, sale.createDate, sale.updateDate, photo.filePath, photo.fileDownloadPath " +
                        "from saleProduct AS sale JOIN Photo AS photo ON sale.postId = photo.postId " +
                        "GROUP BY sale.postId " +
                        "ORDER BY updateDate DESC LIMIT ? OFFSET ?",
                new RowMapper<SaleProduct>() {
                    @Override
                    public SaleProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                        SaleProduct saleProduct = new SaleProduct(
                                rs.getInt("postId"),
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("category"),
                                rs.getInt("price"),
                                rs.getString("content"),
                                rs.getString("status"),
                                rs.getTimestamp("createDate"),
                                rs.getTimestamp("updateDate"),
                                rs.getString("filePath"),
                                rs.getString("fileDownloadPath"));
                                return saleProduct;
                    }
                }, limit, offset);
        return results.isEmpty() ? null : results;
    }

    @Override
    public Optional<SaleProduct> findById(int postId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM `saleProduct` WHERE `postId` = ?",
                    new Object[]{postId},
                    (rs, rowNum) ->
                            Optional.of(new SaleProduct(
                                    rs.getInt("postId"),
                                    rs.getString("id"),
                                    rs.getString("title"),
                                    rs.getString("category"),
                                    rs.getInt("price"),
                                    rs.getString("content"),
                                    rs.getString("status"),
                                    rs.getTimestamp("createDate"),
                                    rs.getTimestamp("updateDate"),
                                    null,
                                    null
                            ))
            );
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<SaleProduct> findByIdJoinPhoto(int postId) {
        return jdbcTemplate.queryForObject(
                "SELECT * from saleProduct AS sale JOIN Photo AS photo ON sale.postId = photo.postId WHERE sale.postId = ? LIMIT 1",
                new Object[]{postId},
                (rs, rowNum) ->
                        Optional.of(new SaleProduct(
                                rs.getInt("postId"),
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("category"),
                                rs.getInt("price"),
                                rs.getString("content"),
                                rs.getString("status"),
                                rs.getTimestamp("createDate"),
                                rs.getTimestamp("updateDate"),
                                rs.getString("filePath"),
                                rs.getString("fileDownloadPath")
                        ))
        );
    }

    @Override
    public List<SaleProduct> findByTitleOrContent(String title) {
        return null;
    }


    private Timestamp getTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        return Timestamp.valueOf(sdf.format(timestamp));
    }

}
