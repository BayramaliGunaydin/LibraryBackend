package com.example.library.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostRequest {
    Long userid;
    String text;
}
