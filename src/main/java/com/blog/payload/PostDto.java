package com.blog.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostDto {

    private long id;

    @Column(unique = true)
    @NotEmpty
    @Size(min=2,message = "Post title should have at least 2 characters")
    private String title;
    @NotEmpty
    @Size(min=10,message = "Post title should have at least 10 characters")
    private String description;

    @NotEmpty(message = "should not be empty")
    private String content;
}
