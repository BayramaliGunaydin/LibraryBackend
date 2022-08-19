package com.example.library.Controller;

import com.example.library.Exception.IdNotFoundException;
import com.example.library.Model.*;
import com.example.library.Repository.BookRepository;
import com.example.library.Repository.ImageRepository;
import com.example.library.Repository.UserRepository;
import com.example.library.Service.ILibraryService;
import com.example.library.Service.IMyUserDetailsService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class LibraryController {
    @Autowired
    private ILibraryService libraryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IMyUserDetailsService userDetailsService;
    @Autowired
    private BookRepository bookRepository;

    @JsonFormat(pattern="yyyy-MM-dd-HH-mm-ss")
    private Date date=new Date();


    @GetMapping
    public ResponseEntity getall() {

        List<Book> bookList = libraryService.getall();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }
    @PostMapping("/message")
    public void getmessage(@RequestBody CustomMessage message){
        libraryService.messagesend(message.getMessage());
    }
    @PostMapping("/message2")
    public void getmessage2(@RequestBody CustomMessage message) throws InterruptedException {
            libraryService.messagesend2(message.getMessage());
        }


    @PostMapping("/addbook")
    public ResponseEntity addbook(@RequestBody Book newbook) {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newbook.setCreateUser(userRepository.findByUsername(username).get().getId());
        date=new Date();
        newbook.setCreatedate(date);
        libraryService.addbook(newbook);
                return new ResponseEntity<>("Kitap oluşturuldu.", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity getsingle(@PathVariable(value = "id") Long id) {
        Optional<Book> book = libraryService.getsingle(id);
        try{
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
         catch (Exception e){
             throw new IdNotFoundException();
         }

}
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {

        try{
            libraryService.delete(id);
            return new ResponseEntity<>(id+"idli kitap Silindi.", HttpStatus.OK);
        }
        catch (Exception e){
            throw new IdNotFoundException();
        }

    }
    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long id,@RequestBody Book newbook) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newbook.setUpdateUser(userRepository.findByUsername(username).get().getId());
        date=new Date();
        newbook.setUpdatedate(date);
            Book book = libraryService.update(id, newbook);
            return new ResponseEntity<>(book, HttpStatus.OK);
    }
    @PutMapping("/addbooktouser/{id}")
    public ResponseEntity addbooktouser(@PathVariable(value = "id") Long id,@RequestBody Bookid bookid) {

        Optional<CustomUser> user = userRepository.findById(id);
        libraryService.addbooktouser(id,bookid.getBookid());
        return new ResponseEntity<>("Kitap eklendi.", HttpStatus.CREATED);
    }
    @GetMapping("/userbooks/{id}")
    public ResponseEntity userbooks(@PathVariable(value = "id") Long id) {
        List<Book> books = libraryService.userbooks(id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/image/{id}")
    @ResponseBody
    public void image(@PathVariable(value = "id") Long id, HttpServletResponse response) throws IOException {
        Optional<Book> book = bookRepository.findById(id);
        response.setContentType("image/jpeg,image/png,image/jpg");
        response.getOutputStream().write(book.get().getPic());
        response.getOutputStream().close();
    }
    @DeleteMapping("/deletebookfromuser/{id}")
    public ResponseEntity deletebookfromuser(@PathVariable(value = "id") Long id,@RequestBody Bookid bookid) {
        libraryService.deletebookfromuser(id,bookid.getBookid());
        return new ResponseEntity<>("Kitap kullanıcıdan silindi.", HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    @ResponseBody
    public ResponseEntity userposts(@PathVariable(value = "id") Long id){
        List<Post> posts = libraryService.userposts(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


}
