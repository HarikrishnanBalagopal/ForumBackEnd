package com.niit.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.niit.model.Message;

@Controller
public class ChatController
{
	private static int id = 0;
	private static final Logger log = LoggerFactory.getLogger(ChatController.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/chat_group")
	@SendTo("/topic/message")
	public Message sendMessageGroup(Message message)
	{
		log.debug("Method Start: sendMessageGroup");
		message.setId(id++);
		message.setTime(new Date());
		log.debug("Method End: sendMessageGroup");
		return message;
	}

	@MessageMapping("/chat")
	@SendTo("/queue/message/{userID}")
	public void sendMessage(Message message, @DestinationVariable("userID") int userID)
	{
		log.debug("Method Start: sendMessage");
		message.setId(id++);
		message.setReceiverID(userID);
		message.setTime(new Date());
		simpMessagingTemplate.convertAndSend("/user/" + userID, message);
		log.debug("Method End: sendMessage");
	}
}