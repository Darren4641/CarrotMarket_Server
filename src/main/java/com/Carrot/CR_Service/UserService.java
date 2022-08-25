package com.Carrot.CR_Service;

import com.Carrot.ErrorHandler.AuthenticationCustomException;
import com.Carrot.ErrorHandler.ErrorCode;
import com.Carrot.CR_Model.CarrotUser;
import com.Carrot.Repository.User.CarrotUserRepository;
import com.Carrot.Repository.User.CarrotUserRepositoryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final CarrotUserRepository carrotUserRepository;

    public UserService(CarrotUserRepositoryImpl carrotUserRepositoryImpl) {
        this.carrotUserRepository = carrotUserRepositoryImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<CarrotUser> user = carrotUserRepository.findById(id);

        if(user.isPresent()) {
            return user.get();
        }
        throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
    }

    public CarrotUser signUp(CarrotUser carrotUser) {

        int status = carrotUserRepository.save(carrotUser);
        System.out.println("status = " + status);
        return carrotUser;
    }

    public CarrotUser find(String id) {
        return carrotUserRepository.findById(id).get();
    }
}
