package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询当前用户的会话列表，每个会话只返回一条最新的私信

    /**
     *
     * @param userId
     * @param offset
     * @param limit
     * @return 当前用户的每个会话的最新私信
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    //查询当前用户会话的数量
    int selectConversationCount(int userId);

    //查询某个会话所包含的消息
    List<Message> selectLetters(String conversationId, int offset, int limit);

    //查询某个会话的消息的数量
    int selectLetterCount(String conversationId);

    //查询未读私信的数量
    /**
     *
     * @param userId
     * @param conversationId 可选，如果有，就是某个会话的未读数量，否则就是全部的未读数量
     * @return
     */
    int selectLetterUnreadCount(int userId, String conversationId);
}
