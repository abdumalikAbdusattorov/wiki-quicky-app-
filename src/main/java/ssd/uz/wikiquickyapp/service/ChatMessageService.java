package ssd.uz.wikiquickyapp.service;

import com.google.inject.internal.asm.$Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssd.uz.wikiquickyapp.entity.ChatMessage;
import ssd.uz.wikiquickyapp.entity.enums.MessageStatus;
import ssd.uz.wikiquickyapp.payload.ApiResponse;
import ssd.uz.wikiquickyapp.payload.ReqChatMessage;
import ssd.uz.wikiquickyapp.repository.ChatMessageRepository;

import java.awt.*;
import java.util.List;

@Service
public class ChatMessageService {
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public ApiResponse edit(ReqChatMessage reqChatMessage) {
        ApiResponse response = new ApiResponse();
        try {
            ChatMessage chatMessage = chatMessageRepository
                    .findById(reqChatMessage.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("can't find message (" + reqChatMessage.getId() + ")"));
            chatMessage.setContent(reqChatMessage.getContent());
            chatMessageRepository.save(chatMessage);
            response.setSuccess(true);
            response.setMessage("Edited");
            response.setMessageType(TrayIcon.MessageType.INFO);
        } catch (Exception e) {
            response.setMessage("Unexpected error");
            response.setSuccess(false);
            response.setMessageType(TrayIcon.MessageType.ERROR);
        }
        return response;
    }


    public Integer countNewMessages(String senderId, String recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.DELIVERED);
    }

    @Transactional
    public List<ChatMessage> getAllChatMessages(String senderId, String recipientId) {
        String chatId = chatRoomService.getChatId(senderId, recipientId, false);
        List<ChatMessage> messages = chatMessageRepository.findAllByChatId(chatId);
        if (!messages.isEmpty()) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }
        return messages;
    }

    public ChatMessage findById(Integer id) {
        return chatMessageRepository.findById(id)
                .map(chatMessage -> { chatMessage.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    @Transactional
    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        chatMessageRepository.updateChatMessage(status.ordinal(), senderId, recipientId);
    }

}
