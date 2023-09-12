package com.blog.service;

import com.blog.payload.PostDto;
import com.blog.payload.PostResponse;

import java.util.List;

public interface PostService {

      PostDto createPost(PostDto postDto);

      PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);

      PostDto getPostById(long id);

      PostDto updatePostById(PostDto postDto, long id);

      void deleteById(long id);
}
