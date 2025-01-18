package com.iamf.commons.mappers;

import com.iamf.commons.dtos.MessageDTO;
import com.iamf.commons.models.Message;

import java.util.List;
import java.util.stream.Collectors;

public class MessageMapper {

    public MessageDTO getMessageDTO(Message message){
        MessageDTO messageDTO = MessageDTO.builder()
        		.id(message.getId())
        		.content(message.getContent())
        		.date(message.getDate())
        		.build();
        return messageDTO;
    }

    public List<MessageDTO> getMessageDTOList(List<Message> messages){
        return messages.stream().map(this::getMessageDTO).collect(Collectors.toList());
    }

}
