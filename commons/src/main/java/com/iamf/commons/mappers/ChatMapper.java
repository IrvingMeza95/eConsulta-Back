package com.iamf.commons.mappers;

import com.iamf.commons.dtos.ChatDTO;
import com.iamf.commons.models.Chat;

import java.util.List;
import java.util.stream.Collectors;

public class ChatMapper {

    private final MessageMapper messageMapper = new MessageMapper();
    private final PersonaMapper personaMapper = new PersonaMapper();

    public ChatDTO getChatDTO(Chat chat){
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setId(chat.getId());
        if (chat.getPersonasIds() != null)
            chatDTO.setPersonasIds(chat.getPersonasIds());
        if (chat.getMessages() != null)
            chatDTO.setMessages(messageMapper.getMessageDTOList(chat.getMessages()));
        return chatDTO;
    }

    public List<ChatDTO> getChatDTOList(List<Chat> chats){
        return chats.stream().map(this::getChatDTO).collect(Collectors.toList());
    }

}
