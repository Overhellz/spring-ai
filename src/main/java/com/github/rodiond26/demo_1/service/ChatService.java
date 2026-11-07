package com.github.rodiond26.demo_1.service;

import com.github.rodiond26.demo_1.model.Chat;
import com.github.rodiond26.demo_1.model.ChatEntry;
import com.github.rodiond26.demo_1.model.Role;
import com.github.rodiond26.demo_1.repository.ChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.rodiond26.demo_1.model.Role.ASSISTANT;
import static com.github.rodiond26.demo_1.model.Role.USER;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatService selfProxy;

    public List<Chat> getAllChats() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Chat getChat(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow();
    }

    public Chat createNewChat(String title) {
        Chat chat = Chat.builder().title(title).build();
        return chatRepository.save(chat);
    }

    public void deleteChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }

    public void proceedInteraction(Long chatId, String prompt) {
        selfProxy.addChatEntry(chatId, prompt, USER);
        String answer = chatClient.prompt().user(prompt).call().content();
        selfProxy.addChatEntry(chatId, answer, ASSISTANT);
    }

    @Transactional
    public void addChatEntry(Long chatId, String prompt, Role role) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        chat.addEntry(ChatEntry.builder().content(prompt).role(role).build());
    }
}
