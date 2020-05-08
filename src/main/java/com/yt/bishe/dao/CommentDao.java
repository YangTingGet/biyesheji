package com.yt.bishe.dao;

import com.yt.bishe.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {
    boolean insertComment(Comment comment);

    List<Comment> selectComments(Integer bookId);

    List<Comment> selectAllComments();

    boolean deleteComment(Integer commentId);
}
