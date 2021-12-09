package ssd.uz.wikiquickyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ssd.uz.wikiquickyapp.entity.ChatMessage;
import ssd.uz.wikiquickyapp.entity.ChatNotification;
import ssd.uz.wikiquickyapp.payload.ReqChatMessage;
import ssd.uz.wikiquickyapp.service.ChatMessageService;
import ssd.uz.wikiquickyapp.service.ChatRoomService;

@CrossOrigin(origins = {"*"})
@RestController

public class ChatController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        String chatId = chatRoomService.getChatId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                false);
        chatMessage.setChatId(chatId);
        ChatMessage saved = chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSend("/user/" + chatMessage.getRecipientId() + "/queue/notification",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
//        simpMessagingTemplate.convertAndSendToUser(
////                chatMessage.getRecipientId(),"/queue/messages",
////                new ChatMessage(
////                        saved.getId(),
////                        saved.getChatId(),
////                        saved.getSenderId(),
////                        saved.getRecipientId(),
////                        saved.getSenderName(),
////                        saved.getRecipientName(),
////                        saved.getContent(),
////                        saved.getTimestamp(),
////                        saved.getStatus()));

    }

    @GetMapping("/api/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Integer> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        return ResponseEntity
                .ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/api/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages(@PathVariable String senderId,
                                              @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.getAllChatMessages(senderId, recipientId));
    }

    @GetMapping("/api/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable Integer id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }

    @PutMapping("/api/messages")

    public ResponseEntity<?> editMessage(@RequestBody ReqChatMessage reqChatMessage) {
        return ResponseEntity
                .ok(chatMessageService.edit(reqChatMessage));
    }


}
