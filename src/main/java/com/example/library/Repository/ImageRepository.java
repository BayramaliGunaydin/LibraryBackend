package com.example.library.Repository;

import com.example.library.Model.Image;
import com.example.library.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {
    public Optional<Image> findByid(Long id);
}