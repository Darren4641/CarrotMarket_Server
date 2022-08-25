package com.Carrot.Repository.User;

import com.Carrot.CR_Model.CarrotUser;

import java.util.List;
import java.util.Optional;

public interface CarrotUserRepository {
    int save(CarrotUser carrotUser);
    int update(CarrotUser carrotUser);
    int deleteById(CarrotUser carrotUser);
    List<CarrotUser> findAll();
    Optional<CarrotUser> findById(String id);
    CarrotUser findByIdAndPassword(String id, String password);
}
