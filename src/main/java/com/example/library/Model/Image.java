package com.example.library.Model;

import javax.persistence.*;
import javax.swing.*;

@Entity
public class Image {
    @Id
    @Column(name="id_book")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    byte[] imageIcon;
}
