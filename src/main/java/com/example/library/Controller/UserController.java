package com.example.library.Controller;
import com.example.library.Exception.NullJWTException;
import com.example.library.Model.CustomUser;

import com.example.library.Model.Role;
import com.example.library.Repository.UserRepository;
import com.example.library.Service.IMyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
public class UserController {

    @Autowired private UserRepository userRepo;
    @Autowired
    private IMyUserDetailsService userDetailsService;

    @GetMapping("/info")
    public CustomUser getUserDetails(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByUsername(username).get();
    }
    @PutMapping("/updaterole/{id}")
    public ResponseEntity updaterole(@PathVariable(value = "id") Long id, @RequestBody Role role) {
        userDetailsService.addroletouser(id,role.getId());
        return new ResponseEntity<>(userRepo.findById(id), HttpStatus.OK);
    }
    @GetMapping("/users")
    public List<CustomUser> getusers(){
        try{
        return userRepo.findAll();}
        catch (Exception e){
            throw new NullJWTException();
        }
    }


}
