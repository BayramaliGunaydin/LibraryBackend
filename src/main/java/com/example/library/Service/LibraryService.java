package com.example.library.Service;

import com.example.library.Exception.IdNotFoundException;
import com.example.library.Model.*;
import com.example.library.Repository.BookRepository;
import com.example.library.Repository.ImageRepository;
import com.example.library.Repository.UserRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@Service
public class LibraryService implements ILibraryService{
    Logger logger = Logger.getLogger("MyLog");
    @Value("${spring.rabbitmq.routingKey}")
    private String routingName;
    @Value("${spring.rabbitmq.Exchange}")
    private String exchange;
    @Autowired
    private BookRepository bookrepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @RabbitListener(queues = "${spring.rabbitmq.Queue}")
    public void message(Message message) throws InterruptedException {
        TimeUnit.SECONDS.sleep(15L);
        logger.info(message.toString());
    }
    public void messagesend(String message){
        rabbitTemplate.convertAndSend(exchange,routingName,message);
    }
    public void messagesend2(String message) throws InterruptedException {
        TimeUnit.SECONDS.sleep(15L);
        logger.info(message);
    }


    public List<Book> getall() {

        return bookrepository.findAll();
    }


    @RabbitListener(queues = "${spring.rabbitmq.SaveQueue}")
    public void addbookListener(Book newbook) throws InterruptedException {
        TimeUnit.SECONDS.sleep(15L);
        bookrepository.save(newbook);
    }

    @Override
    public void addbook(Book newbook) {
        rabbitTemplate.convertAndSend(exchange,routingName,newbook);
    }

    @Override
    public Optional<Book> getsingle(Long id) {
        return bookrepository.findById(id);
    }
    public void delete(Long id){
        bookrepository.deleteById(id);
    }
    public Book update(Long id, Book book){
        Optional<Book> bookupdate = bookrepository.findById(id);
        if(bookupdate.isPresent()){
            bookupdate.get().setbookname(book.getbookname());
            bookupdate.get().setauthor(book.getauthor());
            bookupdate.get().settopic(book.gettopic());
            bookupdate.get().setUpdateUser(book.getUpdateUser());
            bookupdate.get().setUpdatedate(book.getUpdatedate());
            bookrepository.save(bookupdate.get());
            return bookupdate.get();
            }
        else{
            throw new IdNotFoundException();
        }

    }
    public void addbooktouser(Long id,Long bookid){
        Optional<CustomUser> user = userRepository.findById(id);
        Optional<Book> book =bookrepository.findById(bookid);
        user.get().getBooks().add(book.get());
        userRepository.save(user.get());
        book.get().setCustomuser(user.get());
        bookrepository.save(book.get());
    }
    public List<Book> userbooks(Long id){
        return userRepository.findById(id).get().getBooks();
    }
    public void deletebookfromuser(Long id, Long bookid){
        Optional<CustomUser> user = userRepository.findById(id);
        Optional<Book> book =bookrepository.findById(bookid);
        user.get().getBooks().remove(book);
        userRepository.save(user.get());
        book.get().setCustomuser(null);
        bookrepository.save(book.get());

    }
     public List<Post> userposts(Long id){
        return userRepository.findById(id).get().getPosts();
     }

}
