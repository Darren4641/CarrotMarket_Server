package com.Carrot.Repository.PhotoRepository;

import com.Carrot.CR_Model.Photo_SaleProduct;
import lombok.AllArgsConstructor;
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
public class PhotoRepositoryImpl implements PhotoRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public long save(Photo_SaleProduct photo_saleProduct) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `carrotsql`.`Photo`(`category`,`postId`,`id`,`fileName`,`uuid`,`filePath`,`fileDownloadPath`,`fileSize`)" +
                    "VALUES (?,?,?,?,?,?,?,?)", new String[]{"photoId"});
            preparedStatement.setString(1, photo_saleProduct.getCategory());
            preparedStatement.setLong(2, photo_saleProduct.getPostId());
            preparedStatement.setString(3, photo_saleProduct.getId());
            preparedStatement.setString(4, photo_saleProduct.getFileName());
            preparedStatement.setString(5, photo_saleProduct.getUuid());
            preparedStatement.setString(6, photo_saleProduct.getFilePath());
            preparedStatement.setString(7, photo_saleProduct.getFileDownloadPath());
            preparedStatement.setLong(8, photo_saleProduct.getFileSize());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int update(int photoId, String category, Photo_SaleProduct photo_saleProduct) {

        return jdbcTemplate.update("UPDATE `carrotsql`.`Photo` SET `postId` = ?,`id` = ?,`fileName` = ?,`uuid` = ?, `filePath` = ?,`fileDownloadPath` = ?, `fileSize` = ? WHERE `photoId` = ? AND `category` = ?",
                photo_saleProduct.getPostId(),
                photo_saleProduct.getId(),
                photo_saleProduct.getFileName(),
                photo_saleProduct.getUuid(),
                photo_saleProduct.getFilePath(),
                photo_saleProduct.getFileDownloadPath(),
                photo_saleProduct.getFileSize(),
                photoId, category);
    }

    @Override
    public List<Photo_SaleProduct> findByPostIdAndCategory(long postId, String category) {
        List<Photo_SaleProduct> results = jdbcTemplate.query(
                "SELECT * FROM `Photo` WHERE `postId` = ? AND `category` = ? ",
                new RowMapper<Photo_SaleProduct>() {
                    @Override
                    public Photo_SaleProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Photo_SaleProduct photo_saleProduct = new Photo_SaleProduct(
                                rs.getString("category"),
                                rs.getInt("postId"),
                                rs.getString("id"),
                                rs.getString("fileName"),
                                rs.getString("uuid"),
                                rs.getString("filePath"),
                                rs.getString("fileDownloadPath"),
                                rs.getLong("fileSize"));
                        return photo_saleProduct;
                    }
                }, postId, category);
        return results.isEmpty() ? null : results;
    }

    @Override
    public Optional<Photo_SaleProduct> findByPostUuId(String uuid) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM `Photo` WHERE `uuid` = ?",
                new Object[]{uuid},
                (rs, rowNum) ->
                        Optional.of(new Photo_SaleProduct(
                                rs.getString("category"),
                                rs.getInt("postId"),
                                rs.getString("id"),
                                rs.getString("fileName"),
                                rs.getString("uuid"),
                                rs.getString("filePath"),
                                rs.getString("fileDownloadPath"),
                                rs.getLong("fileSize")
                        ))
        );
    }

    @Override
    public void deleteByPostIdAndCategory(long postId, String category) {
        jdbcTemplate.update("DELETE FROM `Photo` WHERE `postId` = ? AND `Category` = ?", postId, category);
    }
}
