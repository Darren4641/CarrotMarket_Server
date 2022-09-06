package com.Carrot.CR_Service;

import com.Carrot.ErrorHandler.ApiResponse;
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

import static com.Carrot.ErrorHandler.ErrorCode.PostNotFoundException;
import static com.Carrot.ErrorHandler.ErrorCode.UsernameOrPasswordNotFoundException;

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

    public ApiResponse signUp(CarrotUser carrotUser) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        int status = carrotUserRepository.save(carrotUser);
        if(status != 0) {
            result.setResult(carrotUser);
        }
        return result;
    }

    public ApiResponse find(String id) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        Optional<CarrotUser> carrotUser = carrotUserRepository.findById(id);
        if(carrotUser.isPresent()) {
            result.setResult(carrotUser.get());
        }else {
            result.setResponseData("code", UsernameOrPasswordNotFoundException.getCode());
            result.setResponseData("message", UsernameOrPasswordNotFoundException.getMessage());
            result.setResponseData("HttpStatus", UsernameOrPasswordNotFoundException.getStatus());
        }


        return result;
    }
}
