package com.Carrot.Security;

import com.Carrot.ErrorHandler.AutheniticationEntryPointHandler;
import com.Carrot.ErrorHandler.WebAccessDeniedHandler;
import com.Carrot.Jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AutheniticationEntryPointHandler autheniticationEntryPointHandler;
    private final WebAccessDeniedHandler webAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //http.httpBasic().disable(); // ???????????? ????????? ?????? ?????? ???????????? ????????? ??????, header??? id, pw??? ?????? token(jwt)??? ?????? ??????. ????????? basic??? ?????? bearer??? ????????????.
        http.httpBasic().disable()
                .authorizeRequests()// ????????? ?????? ???????????? ??????
                .antMatchers("/users/test").authenticated()
                .antMatchers("/write").authenticated()
                .antMatchers("/product/p1/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/users/v1/**").hasRole("USER")
                .antMatchers("/**").permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(autheniticationEntryPointHandler)
                .accessDeniedHandler(webAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter??? UsernamePasswordAuthenticationFilter ?????? ?????????

        // + ????????? ????????? ??????????????? ??????????????? ?????? ????????? CustomUserDetailService ???????????? ???????????????.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
