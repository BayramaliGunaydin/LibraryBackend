package com.example.library.Service;

import com.example.library.Model.Book;
import com.example.library.Model.GetAllRequest;
import com.example.library.Model.Image;
import com.example.library.Model.Post;
import com.example.library.Repository.BookRepository;

import java.util.List;
import java.util.Optional;

public interface ILibraryService {
    public List<Book> getall();
    public void addbook(Book newbook);
    public Optional<Book> getsingle(Long id);
    public void delete(Long id);
    public Book update(Long id, Book book);
   /* public void messagesend(String message);
    public void messagesend2(String message) throws InterruptedException;*/
    public void addbooktouser(Long id,Long bookid);
    public List<Book> userbooks(Long id);
    public void deletebookfromuser(Long id, Long bookid);
    public List<Post> userposts(Long id);
}
