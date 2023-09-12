package com.blog.service.impl;
import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.exception.BlogAPIException;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.CommentDto;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepo;

    private ModelMapper mapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepo,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepo = postRepo;
        this.mapper=mapper;
    }


    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with Id: "+postId)
        );

        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        CommentDto dto = mapToDto(savedComment);

        return dto;
    }

    @Override
    public List<CommentDto> getCommentByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return  comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with Id: "+postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("comment not found with Id: "+commentId)
        );

        if (comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException("Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateCommentById(long postId, long commentId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with Id: "+postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("comment not found with Id: "+commentId)
        );

        if (comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException("Comment does not belong to post");
        }
         comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updatedcomment = commentRepository.save(comment);

        return mapToDto(updatedcomment);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with Id: "+postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("comment not found with Id: "+commentId)
        );

        if (comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException("Comment does not belong to post");
        }
        commentRepository.deleteById(commentId);
    }


    Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment=new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }

    CommentDto mapToDto(Comment comment){
        CommentDto dto = mapper.map(comment, CommentDto.class);
//        CommentDto dto=new CommentDto();
//        dto.setId(comment.getId());
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setBody(comment.getBody());
        return dto;
    }


}
