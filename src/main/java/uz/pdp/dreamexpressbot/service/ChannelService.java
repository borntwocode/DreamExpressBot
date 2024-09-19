package uz.pdp.dreamexpressbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class ChannelService {

    @Value("${channel.chat.id}")
    private String channelChatId;
    @Value("${channel.url}")
    private String channelUrl;

    private final TelegramBot telegramBot;

    public boolean isUserSubscribed(Long userId) {
        return isUserMemberOfChannel(channelChatId, userId);
    }

    private boolean isUserMemberOfChannel(String chatId, Long userId) {
        GetChatMember getChatMember = new GetChatMember(chatId, userId);
        GetChatMemberResponse response = telegramBot.execute(getChatMember);
        if (response == null || !response.isOk()) {
            return false;
        }
        ChatMember chatMember = response.chatMember();
        ChatMember.Status status = chatMember.status();
        return (status == ChatMember.Status.member || status == ChatMember.Status.administrator || status == ChatMember.Status.creator);
    }

}
