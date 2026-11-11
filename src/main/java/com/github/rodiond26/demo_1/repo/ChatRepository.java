package com.github.rodiond26.demo_1.repo;

import com.github.rodiond26.demo_1.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> /*, ChatMemoryRepository */ {
//
//    @Override
//    default List<String> findConversationIds() {
//        return findAll().stream()
//                .map(Chat::getId)
//                .map(String::valueOf)
//                .toList();
//    }
//
//    @Override
//    default List<Message> findByConversationId(String conversationId) {
//        Chat chat = findById(Long.valueOf(conversationId)).orElseThrow();
//        return chat.getHistory().stream()
//                .map(ChatEntry::toMessage)
//                .toList();
//    }
//
//    @Override
//    default void saveAll(String conversationId, List<Message> messages) {
//        Chat chat = findById(Long.valueOf(conversationId)).orElseThrow();
//        messages.stream().map(ChatEntry::toChatEntry).forEach(chat::addEntry);
//        save(chat);
//    }
//
//    @Override
//    default void deleteByConversationId(String conversationId) {
//        // not implemented never
//    }
}
