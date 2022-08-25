package com.Carrot.CR_Service;

import com.Carrot.ErrorHandler.ApiResponse;
import com.Carrot.ErrorHandler.AuthenticationCustomException;
import com.Carrot.ErrorHandler.ErrorCode;
import com.Carrot.Jwt.JwtTokenProvider;
import com.Carrot.Jwt.RefreshToken;
import com.Carrot.Jwt.Token;
import com.Carrot.CR_Model.CarrotUser;
import com.Carrot.Repository.User.CarrotUserRepositoryImpl;
import com.Carrot.Repository.RefreshToken.RefreshTokenRepository;
import com.Carrot.Repository.User.CarrotUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final CarrotUserRepository usersRepository;

    public JwtService(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository, AuthenticationManager authenticationManager, CarrotUserRepositoryImpl usersRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
    }
    
    @Transactional
    public ApiResponse login(HttpServletRequest request, Map<String, String> user, String userAgent) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.get("id"), user.get("password"))
            );

            CarrotUser userinfo = usersRepository.findById(user.get("id")).get();
            Token token = jwtTokenProvider.createToken(user.get("id"));
            
            //RefreshToken을 DB에 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .keyId(token.getKey())
                    .refreshToken(token.getRefreshToken()).
                    userAgent(userAgent).build();
            String loginUserId = refreshToken.getKeyId();

            //다음번 로그인시 UserAgent값이 다를 경우 다른 기기에서 로그인한경우인데 여기 if문에서 처리해주면 된다
            if(refreshTokenRepository.existsByKeyIdAndUserAgent(loginUserId, userAgent)) {
                refreshTokenRepository.deleteByKeyIdAndUserAgent(loginUserId, userAgent);
            }
            refreshTokenRepository.save(refreshToken);

            result.setResponseData("accessToken", token.getAccessToken());
            result.setResponseData("refreshToken", token.getRefreshToken());
            result.setResponseData("key", token.getKey());
        }catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", "UsernameOrPasswordNotFoundException");
            throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
        }
        return result;
    }

    public ApiResponse newAccessToken(RefreshToken refreshToken) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        if(refreshToken.getRefreshToken() != null) {
            String newToken = jwtTokenProvider.validateRefreshToken(refreshToken);
            result.setResponseData("accessToken", newToken);
        }else {
            result.setResponseData("code", ErrorCode.ReLogin.getCode());
            result.setResponseData("message", ErrorCode.ReLogin.getMessage());
            result.setResponseData("HttpStatus", ErrorCode.ReLogin.getStatus());
        }
        return result;
    }

}
