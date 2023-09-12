package com.blog.controller;
import com.blog.payload.CommentDto;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

   //http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,@PathVariable("postId")long postId) {
        CommentDto dto = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(
            @PathVariable("postId") long postId) {
        List<CommentDto> commentDto = commentService.getCommentByPostId(postId);

           return commentDto;
        }

    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable("postId") long postId,
            @PathVariable("commentId") long commentId
    ){
        CommentDto dto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("postId") long postId,
            @PathVariable("commentId") long commentId,
            @RequestBody CommentDto commentDto
    ){
        CommentDto dto=commentService.updateCommentById(postId,commentId,commentDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String>  deleteCommentById(
            @PathVariable("postId") long postId,
            @PathVariable("commentId") long commentId
    ){
        commentService.deleteCommentById(postId,commentId);
        return new ResponseEntity<>("Comment is deleted",HttpStatus.OK);
    }
}

