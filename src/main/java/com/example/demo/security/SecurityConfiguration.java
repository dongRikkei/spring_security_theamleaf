package com.example.demo.security;

import com.example.demo.security.jwt.AuthEntryPointJwt;
import com.example.demo.security.jwt.AuthTokenFilter;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfiguration {
    @Autowired
    private AuthEntryPointJwt authEntryPointHandler;

    @Autowired
    private AuthTokenFilter authTokenFilter;

//    @Autowired
//    UserServiceImpl userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.GET, "/css/**", "/js/**","/fonts/**","/favicon.ico", "/about").permitAll()
                        // Ko cần check authencation
//                        .requestMatchers("/","/registration**", "/product", "/auth/**", "/fb/**").permitAll()
                        // Hasrole thi la phan quyen theo user
                        .requestMatchers("/admin**").hasRole("ADMIN")
                        // Khong can xac thu van co the vao duoc
                        .requestMatchers("/homepage", "/auth/**").permitAll()
                        // tất cả các request còn lại đều phải xaác thực(kiem tra xem login hay chua)
                        .anyRequest().authenticated()
                );

        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(authEntryPointHandler)).csrf((csrf) -> csrf.disable());

//        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
