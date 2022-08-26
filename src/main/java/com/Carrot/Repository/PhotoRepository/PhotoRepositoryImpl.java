package com.Carrot.Repository.PhotoRepository;

import com.Carrot.CR_Model.Photo_SaleProduct;
import com.Carrot.CR_Model.SaleProduct;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
    public int update(int photoId, Photo_SaleProduct photo_saleProduct) {

        return jdbcTemplate.update("UPDATE `carrotsql`.`Photo` SET `category` = ?,`postId` = ?,`id` = ?,`fileName` = ?,`uuid` = ?, `filePath` = ?,`fileDownloadPath` = ?, `fileSize` = ? WHERE `photoId` = ?",
                photo_saleProduct.getCategory(),
                photo_saleProduct.getPostId(),
                photo_saleProduct.getId(),
                photo_saleProduct.getFileName(),
                photo_saleProduct.getUuid(),
                photo_saleProduct.getFilePath(),
                photo_saleProduct.getFileDownloadPath(),
                photo_saleProduct.getFileSize(),
                photoId);
    }

    @Override
    public Optional<Photo_SaleProduct> findByPostId(long postId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM `Photo` WHERE `postId` = ?",
                new Object[]{postId},
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
}
