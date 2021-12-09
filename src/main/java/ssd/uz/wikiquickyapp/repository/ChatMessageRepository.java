package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.entity.ChatMessage;
import ssd.uz.wikiquickyapp.entity.enums.MessageStatus;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    Integer countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findAllByChatId(String chatId);

    @Modifying
    @Query(value = "update chat_message ch set status = :status where ch.sender_id = :senderId and ch.recipient_id = :recipientId", nativeQuery = true)
    void updateChatMessage(
            @Param(value = "status") Integer status,
            @Param(value = "senderId") String senderId,
            @Param(value = "recipientId") String recipientId);
}
