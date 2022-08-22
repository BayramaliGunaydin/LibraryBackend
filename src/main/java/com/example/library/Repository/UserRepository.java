package com.example.library.Repository;


import com.example.library.Model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
     public Optional<CustomUser> findByUsername(String username);

}
