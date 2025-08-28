package org.ahavah.portal.contollers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.auth.LoginDto;
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
@RequiredArgsConstructor
public class AuthController {
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto
//            HttpServletResponse response
    ) {
           try {
               this.authenticationManager.authenticate(
                       new UsernamePasswordAuthenticationToken(
                               loginDto.getEmail(),
                               loginDto.getPassword()
                       )
               );
               var user = userRepository.findByEmail(loginDto.getEmail());
               var access = jwtService.generateAccessToken(user.getId(), loginDto.getEmail(), user.getRole(),  user.getDivision());

               var refresh = jwtService.generateRefreshToken(user.getId(), loginDto.getEmail(), user.getRole(),  user.getDivision());
//               var refreshCookie = new Cookie("refresh_token", refresh);
//               refreshCookie.setHttpOnly(true);
//               refreshCookie.setPath("/auth/refresh");
//               refreshCookie.setMaxAge(86400);
//               refreshCookie.setSecure(false);
//               response.addCookie(refreshCookie);
//
//                var accessCookie = new Cookie("access_token", access);
//                accessCookie.setHttpOnly(true);
//                accessCookie.setPath("/");
//                accessCookie.setMaxAge(3600);
//                accessCookie.setSecure(false);
//                response.addCookie(accessCookie);

               return ResponseEntity.ok(Map.of(
                       "msg", "Login Successful",
                       "ROLE", user.getRole(),
                       "DIVISION", user.getDivision(),
                       "ACCESS_TOKEN", access ,
                       "REFRESH_TOKEN", refresh,
                       "EMAIL", user.getEmail(),
                       "NAME", user.getName()));
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
