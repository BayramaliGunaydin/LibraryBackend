package com.example.library.Service;

import com.example.library.Model.Book;
import com.example.library.Model.CustomUser;
import com.example.library.Model.Like;
import com.example.library.Repository.BookRepository;
import com.example.library.Repository.LikeRepository;
import com.example.library.Repository.UserRepository;
import com.example.library.Response.LikeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService implements ILikeService {
    @Autowired
    private BookRepository bookrepository;
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;
    public List<LikeResponse> getbookslikes(Long id){
        List<Like> likelist = bookrepository.findById(id).get().getLikes();
        return likelist.stream().map(like->new LikeResponse(like)).collect(Collectors.toList());
    }
    public void addlike(Long userid,Long bookid){
        Like like =new Like(bookrepository.findById(bookid).get(),userRepository.findById(userid).get());
        likeRepository.save(like);
    }
    public void deletelike(Long likeid){
        Like like = likeRepository.findById(likeid).get();
        likeRepository.delete(like);
    }
}
