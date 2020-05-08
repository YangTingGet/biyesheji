package com.yt.bishe.service;

import com.yt.bishe.entity.Comment;

import java.util.List;

public interface CommentService {
    boolean addComment(Comment comment);

    List<Comment> getCommentsByBookId(Integer bookId);

    List<Comment> getAllComments();

    boolean deleteComment(Integer commentId);
}
