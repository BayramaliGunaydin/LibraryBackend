package com.example.library.Repository;

import com.example.library.Model.Like;
import com.example.library.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
}
