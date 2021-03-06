package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
    private MessageMapper messageMapper;
    private SensitiveFilter sensitiveFilter;

    @Autowired
    public MessageService(MessageMapper messageMapper, SensitiveFilter sensitiveFilter) {
        this.messageMapper = messageMapper;
        this.sensitiveFilter = sensitiveFilter;
    }

    public List<Message> findConversations(int userId, int offset, int limit){
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit){
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId){
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message){
        message.setContent(message.getContent());
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    //将消息状态变成已读
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids, 1);
    }
}
