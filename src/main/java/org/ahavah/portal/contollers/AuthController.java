package org.ahavah.portal.contollers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.auth.LoginDto;
import org.ahavah.portal.mappers.UserMapper;
import org.ahavah.portal.repositories.UserRepository;
import org.ahavah.portal.services.auth.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto,
            HttpServletResponse response) {
           try {
               authenticationManager.authenticate(
                       new UsernamePasswordAuthenticationToken(
                               loginDto.getEmail(),
                               loginDto.getPassword()
                       )
               );
               var user = userRepository.findByEmail(loginDto.getEmail());
               var token = jwtService.generateAccessToken(user.getId(), loginDto.getEmail(), user.getRole(),  user.getDivision());

               var refresh = jwtService.generateRefreshToken(user.getId(), loginDto.getEmail(), user.getRole(),  user.getDivision());
               var cookie = new Cookie("refresh_token", refresh);
               cookie.setHttpOnly(true);
               cookie.setPath("/auth/login");
               cookie.setMaxAge(86400);
               cookie.setSecure(false);
               response.addCookie(cookie);

               return ResponseEntity.ok(Map.of("access_token",token ));
           }
           catch (
                   BadCredentialsException e
           ){
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
           }

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue("refresh_token") String refreshToken
    ){
        if(!jwtService.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        var userID = jwtService.getUserIdFromToken(refreshToken);
        var user = userRepository.findById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var token = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getDivision());
        return ResponseEntity.ok(Map.of("access_token",token ));

    }


}
