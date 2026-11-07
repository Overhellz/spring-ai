package com.github.rodiond26.demo_1.service;

import com.github.rodiond26.demo_1.model.Chat;
import com.github.rodiond26.demo_1.model.ChatEntry;
import com.github.rodiond26.demo_1.model.Role;
import com.github.rodiond26.demo_1.repository.ChatRepo;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.github.rodiond26.demo_1.model.Role.ASSISTANT;
import static com.github.rodiond26.demo_1.model.Role.USER;

@Service
public class ChatService {

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatService selfProxy;

    @Autowired
    private PostgresChatMemory postgresChatMemory;

    public List<Chat> getAllChats() {
        return chatRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Chat getChat(Long chatId) {
        return chatRepo.findById(chatId).orElseThrow();
    }

    public Chat createNewChat(String title) {
        Chat chat = Chat.builder().title(title).build();
        return chatRepo.save(chat);
    }

    public void deleteChat(Long chatId) {
        chatRepo.deleteById(chatId);
    }

    public void proceedInteraction(Long chatId, String prompt) {
        selfProxy.addChatEntry(chatId, prompt, USER);
        String answer = chatClient.prompt().user(prompt).call().content();
        selfProxy.addChatEntry(chatId, answer, ASSISTANT);
    }

    @Transactional
    public void addChatEntry(Long chatId, String prompt, Role role) {
        Chat chat = chatRepo.findById(chatId).orElseThrow();
        chat.addEntry(ChatEntry.builder().content(prompt).role(role).build());
    }

    public SseEmitter proceedInteractionWithStreaming(Long chatId, String prompt) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        StringBuilder answer = new StringBuilder();

        chatClient.prompt()
                .user(prompt)
                .advisors(MessageChatMemoryAdvisor.builder(postgresChatMemory).conversationId(String.valueOf(chatId)).build())
                .stream()
                .chatResponse().subscribe(
                        response -> processToken(response, sseEmitter, answer),
                        sseEmitter::completeWithError);

        return sseEmitter;
    }

    @SneakyThrows
    private static void processToken(ChatResponse response, SseEmitter sseEmitter, StringBuilder answer) {
        AssistantMessage token = response.getResult().getOutput();
        sseEmitter.send(token);
        answer.append(token.getText());
    }
}
