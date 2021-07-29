package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(Integer entityType, Integer entityId, int offset, int limit);
    int selectCountByEntity(Integer entityType, Integer entityId);
    int insertComment(Comment comment);
}
