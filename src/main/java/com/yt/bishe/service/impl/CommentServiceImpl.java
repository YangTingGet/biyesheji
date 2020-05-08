package com.yt.bishe.service.impl;

import com.yt.bishe.dao.CommentDao;
import com.yt.bishe.entity.Comment;
import com.yt.bishe.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Override
    public boolean addComment(Comment comment) {
        return commentDao.insertComment(comment);
    }

    @Override
    public List<Comment> getCommentsByBookId(Integer bookId) {
        return commentDao.selectComments(bookId);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentDao.selectAllComments();
    }

    @Override
    public boolean deleteComment(Integer commentId) {
        return commentDao.deleteComment(commentId);
    }
}
