package com.example.library.Controller;
import com.example.library.Exception.NullJWTException;
import com.example.library.Model.*;

import com.example.library.Repository.UserRepository;
import com.example.library.Service.IMyUserDetailsService;
import com.example.library.Service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping
public class UserController {

    @Autowired private UserRepository userRepo;
    @Autowired
    private IMyUserDetailsService userDetailsService;
    @Autowired
    private LibraryService libraryService;

    @GetMapping("/info")
    @CrossOrigin(origins = "http://localhost:3000")
    public CustomUser getUserDetails(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByUsername(username).get();
    }
    @PutMapping("/updaterole/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity updaterole(@PathVariable(value = "id") Long id, @RequestBody Role role) {
        userDetailsService.addroletouser(id,role.getId());
        return new ResponseEntity<>(userRepo.findById(id), HttpStatus.OK);
    }
    @GetMapping("/users")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<CustomUser> getusers(){
        try{
        return userRepo.findAll();}
        catch (Exception e){
            throw new NullJWTException();
        }
    }

    @PutMapping("/saveimage/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public void saveuserimage(@PathVariable(value = "id") Long id,@RequestBody base64 base64){
        libraryService.saveuserimage(id,base64);
    }

    @GetMapping("/userlikes/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Like> getuserlikes(@PathVariable(value = "id") Long id){
        return libraryService.userlikes(id);
    }
    @GetMapping("/userbooks/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity userbooks(@PathVariable(value = "id") Long id) {
        List<Book> books = libraryService.userbooks(id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


}
