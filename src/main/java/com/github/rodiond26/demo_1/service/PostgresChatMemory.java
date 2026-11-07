package com.github.rodiond26.demo_1.service;

import com.github.rodiond26.demo_1.model.Chat;
import com.github.rodiond26.demo_1.model.ChatEntry;
import com.github.rodiond26.demo_1.repository.ChatRepo;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PostgresChatMemory implements ChatMemory {

    @Autowired
    private ChatRepo chatRepo;

    @Override
    @Transactional
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            Chat chat = chatRepo.findById(Long.valueOf(conversationId)).orElseThrow();
            chat.addEntry(ChatEntry.toChatEntry(message));
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        Chat chat = chatRepo.findById(Long.valueOf(conversationId)).orElseThrow();
        return chat.getHistory().stream()
                .map(ChatEntry::toMessage)
                .limit(lastN)
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        // not implemented
    }
}
