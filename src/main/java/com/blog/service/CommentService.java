package com.blog.service;

import com.blog.payload.CommentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId,CommentDto commentDto);


    List<CommentDto> getCommentByPostId(Long postId);
    CommentDto getCommentById(long postId, long commentId);

    CommentDto updateCommentById(long postId, long commentId, CommentDto commentDto);

    void deleteCommentById(long postId, long commentId);
}

