package com.example.library.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "bookid",referencedColumnName = "id_book")
    Book book;
    @ManyToOne
    @JoinColumn(name = "userid",referencedColumnName = "id")
    CustomUser customuser;
    String post;
}
