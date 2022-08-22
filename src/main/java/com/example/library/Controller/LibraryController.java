package com.example.library.Controller;

import com.example.library.Exception.IdNotFoundException;
import com.example.library.Model.*;
import com.example.library.Repository.BookRepository;
import com.example.library.Repository.ImageRepository;
import com.example.library.Repository.UserRepository;
import com.example.library.Response.BookResponse;
import com.example.library.Response.LikeResponse;
import com.example.library.Service.ILibraryService;
import com.example.library.Service.IMyUserDetailsService;
import com.example.library.Service.LikeService;
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
    @Autowired
    private LikeService likeService;

    @JsonFormat(pattern="yyyy-MM-dd-HH-mm-ss")
    private Date date=new Date();


    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity getall() {

        List<BookResponse> bookList = libraryService.getall();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }
    /*@PostMapping("/message")
    public void getmessage(@RequestBody CustomMessage message){
        libraryService.messagesend(message.getMessage());
    }
    @PostMapping("/message2")
    public void getmessage2(@RequestBody CustomMessage message) throws InterruptedException {
            libraryService.messagesend2(message.getMessage());
        }*/


    @PostMapping("/addbook")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity addbook(@RequestBody Book newbook) {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newbook.setCreateUser(userRepository.findByUsername(username).get().getId());
        date=new Date();
        newbook.setCreatedate(date);
        libraryService.addbook(newbook);
                return new ResponseEntity<>("Kitap oluşturuldu.", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
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
    @CrossOrigin(origins = "http://localhost:3000")
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
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity update(@PathVariable(value = "id") Long id,@RequestBody Book newbook) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newbook.setUpdateUser(userRepository.findByUsername(username).get().getId());
        date=new Date();
        newbook.setUpdatedate(date);
            Book book = libraryService.update(id, newbook);
            return new ResponseEntity<>(book, HttpStatus.OK);
    }
    @PutMapping("/addbooktouser/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity addbooktouser(@PathVariable(value = "id") Long id,@RequestBody Bookid bookid) {

        Optional<CustomUser> user = userRepository.findById(id);
        libraryService.addbooktouser(id,bookid.getBookid());
        return new ResponseEntity<>("Kitap eklendi.", HttpStatus.CREATED);
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public void image(@PathVariable(value = "id") Long id, HttpServletResponse response) throws IOException {
        Optional<Book> book = bookRepository.findById(id);
        response.setContentType("image/jpeg,image/png,image/jpg");
        response.getOutputStream().write(book.get().getPic());
        response.getOutputStream().close();
    }
    @DeleteMapping("/deletebookfromuser/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity deletebookfromuser(@PathVariable(value = "id") Long id,@RequestBody Bookid bookid) {
        libraryService.deletebookfromuser(id,bookid.getBookid());
        return new ResponseEntity<>("Kitap kullanıcıdan silindi.", HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity bookposts(@PathVariable(value = "id") Long id){
        List<Post> posts = libraryService.bookposts(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity getuser(@PathVariable(value = "id") Long id){
        CustomUser user = libraryService.getuser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/post/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public void addposttobook(@PathVariable(value = "id") Long id,@RequestBody PostRequest postRequest){
        Post post = new Post();
        post.setBook(bookRepository.findById(id).get());
        post.setCustomuser(userRepository.findById(postRequest.getUserid()).get());
        post.setPost(postRequest.getText());
        libraryService.addposttobook(post);
    }
    @GetMapping("/pic/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity getpic(@PathVariable(value = "id") Long id){
        Book book = bookRepository.findById(id).get();
        return new ResponseEntity<>(book.getPic(), HttpStatus.OK);
    }
    @GetMapping("/userposts/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity userposts(@PathVariable(value = "id") Long id){
        List<Post> posts = libraryService.userposts(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
  /*  @GetMapping("/booklikes/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<LikeResponse> getbookslikes(@PathVariable(value = "id") Long id){
        return libraryService.getbookslikes(id);
    }*/
  @PostMapping ("/addlike/{id}")
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public void addlike(@PathVariable(value = "id") Long id,@RequestBody Bookid bookid){
      likeService.addlike(id,bookid.getBookid());
  }
    @DeleteMapping ("/deletelike/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public void deletelike(@PathVariable(value = "id") Long id){
        likeService.deletelike(id);
    }

}
