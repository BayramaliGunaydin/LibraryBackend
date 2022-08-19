package com.example.library.Controller;

import com.example.library.Config.JWTUtil;
import com.example.library.Exception.UsernameAlreadyExistException;
import com.example.library.Exception.UsernamePasswordNotFoundException;
import com.example.library.Model.CustomUser;
import com.example.library.Model.LoginCredentials;
import com.example.library.Model.Role;
import com.example.library.Repository.RoleRepository;
import com.example.library.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody CustomUser user) throws UsernameAlreadyExistException {

        if(userRepo.findByUsername(user.getUsername()).isPresent()){

            throw new UsernameAlreadyExistException();
        }
        else{
            String encodedPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPass);
            Optional<Role> role =roleRepository.findByid(1L);
            user.setRole(role.get());
            user = userRepo.save(user);
            String token = jwtUtil.generateToken(user.getUsername());
            return Collections.singletonMap("jwt-token", token);
        }

    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getUsername());

            return Collections.singletonMap("jwt-token", token);
        }catch (AuthenticationException authExc){
            throw new UsernamePasswordNotFoundException();
        }
    }
   


}