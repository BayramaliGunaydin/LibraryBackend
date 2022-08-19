package com.example.library.Repository;

import com.example.library.Model.Image;
import com.example.library.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    public Optional<Post> findByid(Long id);
}