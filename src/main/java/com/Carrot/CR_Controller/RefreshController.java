package com.Carrot.CR_Controller;

import com.Carrot.ErrorHandler.ApiResponse;
import com.Carrot.Jwt.RefreshToken;
import com.Carrot.CR_Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final JwtService jwtService;

    @PostMapping("/refresh")
    public ApiResponse validateRefreshToken(@RequestBody RefreshToken bodyJson) {
        return jwtService.newAccessToken(bodyJson);

    }
}
