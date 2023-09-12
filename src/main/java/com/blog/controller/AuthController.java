package com.blog.controller;

import com.blog.entities.Post;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.payload.JWTAuthResponse;
import com.blog.payload.LoginDto;
import com.blog.payload.SignUpDto;
import com.blog.repository.RoleRepository;
import com.blog.repository.UserRepository;
import com.blog.security.JwtTokenProvider;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //http://localhost:8080/api/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication=authenticationManager.authenticate(
                new
                        UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token from tokenProvider
        String token= jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    //http://localhost:8080/api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDTo){
        //add check for username exists in a DB
        if (userRepository.existsByUsername(signUpDTo.getUsername())){
            return new ResponseEntity<>("Username is already taken!",HttpStatus.BAD_REQUEST);
        }

        //add check for email exists in a DB
        if (userRepository.existsByEmail(signUpDTo.getEmail())){
            return new ResponseEntity<>("Email is already token!",HttpStatus.BAD_REQUEST);
        }

        // create user objet
        User user=new User();
        user.setName(signUpDTo.getName());
        user.setUsername(signUpDTo.getUsername());
        user.setEmail(signUpDTo.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTo.getPassword()));

        Role roles=roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);

        return  new ResponseEntity<>("User registered successfully",HttpStatus.OK);
    }

}
