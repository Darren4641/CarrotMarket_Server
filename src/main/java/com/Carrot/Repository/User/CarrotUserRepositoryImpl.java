package com.Carrot.Repository.User;

import com.Carrot.Role.Role;
import com.Carrot.CR_Model.CarrotUser;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarrotUserRepositoryImpl implements CarrotUserRepository{

    private final JdbcTemplate jdbcTemplate;


    @Override
    public int save(CarrotUser carrotUser) {
        return jdbcTemplate.update(
                "INSERT INTO `carrotUser` (`id`,`password`,`phone`,`nickName`,`temperature`,`image`, `role`)" +
                        "VALUES(?,?,?,?,?,?,?)",
                carrotUser.getId(),
                carrotUser.getPassword(),
                carrotUser.getPhone(),
                carrotUser.getNickName(),
                carrotUser.getTemperature(),
                carrotUser.getImage(),
                Role.USER.toString());
    }

    @Override
    public int update(CarrotUser carrotUser) {
        return jdbcTemplate.update("UPDATE carrotUser SET `password` = ?, `phone` = ?, `nickName` = ?, `temperature` = ?, `image` = ?" +
                "WHERE `id` = ?",
                carrotUser.getPassword(),
                carrotUser.getPhone(),
                carrotUser.getNickName(),
                carrotUser.getTemperature(),
                carrotUser.getImage(),
                carrotUser.getId());
    }

    @Override
    public int deleteById(CarrotUser carrotUser) {
        return jdbcTemplate.update("DELETE FROM `carrotUser`" +
                "WHERE `id` = ?",
                carrotUser.getId());
    }

    @Override
    public List<CarrotUser> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM `carrotUser`",
                (rs, rowNum) ->
                        new CarrotUser(
                                rs.getString("id"),
                                rs.getString("password"),
                                rs.getString("phone"),
                                rs.getString("nickName"),
                                rs.getLong("temperature"),
                                rs.getString("image"),
                                Role.from(rs.getString("role"))
                        )
        );
    }

    @Override
    public Optional<CarrotUser> findById(String id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM `carrotUser` WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) ->
                        Optional.of(new CarrotUser(
                                rs.getString("id"),
                                rs.getString("password"),
                                rs.getString("phone"),
                                rs.getString("nickName"),
                                rs.getLong("temperature"),
                                rs.getString("image"),
                                Role.from(rs.getString("role"))
                        ))
        );
    }

    public CarrotUser findByIdAndPassword(String id, String password) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM `carrotUser` WHERE id = ? and password = ?",
                new Object[]{id, password},
                (rs, rowNum) ->
                        new CarrotUser(
                                rs.getString("id"),
                                rs.getString("password"),
                                rs.getString("phone"),
                                rs.getString("nickName"),
                                rs.getInt("temperature"),
                                rs.getString("image"),
                                Role.from(rs.getString("role"))
                        )
        );
    }

}
