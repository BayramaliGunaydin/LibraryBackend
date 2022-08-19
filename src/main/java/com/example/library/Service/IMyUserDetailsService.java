package com.example.library.Service;

import com.example.library.Model.CustomUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface IMyUserDetailsService extends UserDetailsService {
    public UserDetails loadUserByUsername(String username);
    public CustomUser addroletouser(Long id, Long roleid);
}
