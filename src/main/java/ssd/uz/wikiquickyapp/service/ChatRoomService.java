package ssd.uz.wikiquickyapp.service;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.ChatRoom;
import ssd.uz.wikiquickyapp.repository.ChatRoomRepository;

import java.util.Optional;

@Service
public class ChatRoomService {
    @Autowired
    ChatRoomRepository chatRoomRepository;

    public String getChatId(String senderId, String recipientId, boolean createIfNotExist) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findAllByRecipientIdAndSenderId(recipientId, senderId);
        if (chatRoom.isPresent()) {
            return chatRoom.get().getChatId();
        } else {
            var chatId =
                    String.format("%s_%s", senderId, recipientId);
            ChatRoom senderRecipient = ChatRoom
                    .builder()
                    .chatId(chatId)
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .build();

            ChatRoom recipientSender = ChatRoom
                    .builder()
                    .chatId(chatId)
                    .senderId(recipientId)
                    .recipientId(senderId)
                    .build();
            chatRoomRepository.save(senderRecipient);
            chatRoomRepository.save(recipientSender);
            return chatId;
        }
    }

}
