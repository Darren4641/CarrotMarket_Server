package com.Carrot.Controller;

import com.Carrot.ErrorHandler.ApiResponse;
import com.Carrot.Role.Role;
import com.Carrot.Service.JwtService;
import com.Carrot.Service.UserService;
import com.Carrot.Model.CarrotUser;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class CarrotUserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public CarrotUser signUp(@RequestBody Map<String, String> user) {
        Role role = Role.from(user.get("role"));
        CarrotUser carrotUser = CarrotUser.builder()
                .id(user.get("id"))
                .password(passwordEncoder.encode(user.get("password")))
                .phone(user.get("phone"))
                .nickName(user.get("nickName"))
                .temperature(Double.parseDouble(user.get("temperature")))
                .image(user.get("image"))
                .role(role)
                .build();

        return userService.signUp(carrotUser);

    }

    @PostMapping("/login")
    public ApiResponse login(HttpServletRequest request,  @RequestBody Map<String, String> user, @RequestHeader("User-Agent") String userAgent) {
        return jwtService.login(request, user, userAgent);
    }

    @PostMapping("/find")
    public CarrotUser findUser(@RequestBody Map<String, String> user) {
        return userService.find(user.get("id"));
    }

    @PostMapping("/test")
    public String test(){
        return "<h1>test 통과</h1>";
    }

}
