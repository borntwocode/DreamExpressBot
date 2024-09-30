package uz.pdp.dreamexpressbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendLocation;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.bot.BotUtils;
import uz.pdp.dreamexpressbot.entity.Location;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.BotMessages;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final TelegramBot telegramBot;
    private final BotUtils botUtils;
    private final PhotoService photoService;

    public Integer sendWithButton(TelegramUser user, String text, Keyboard buttons) {
        SendMessage message = new SendMessage(user.getChatId(), text);
        message.replyMarkup(buttons);
        SendResponse execute = telegramBot.execute(message);
        return execute.message().messageId();
    }

    public void sendWithButton(TelegramUser user, BotMessages botMessages, Keyboard buttons) {
        String text = botMessages.getMessage(user);
        SendMessage message = new SendMessage(user.getChatId(), text);
        message.replyMarkup(buttons);
        telegramBot.execute(message);
    }

    public Integer sendMessage(TelegramUser user, String text) {
        SendMessage message = new SendMessage(user.getChatId(), text);
        SendResponse execute = telegramBot.execute(message);
        return execute.message().messageId();
    }

    public void sendMessage(TelegramUser user, BotMessages botMessages) {
        String text = botMessages.getMessage(user);
        SendMessage message = new SendMessage(user.getChatId(), text);
        telegramBot.execute(message);
    }

    public void sendLocation(TelegramUser user, Float latitude, Float longitude) {
        SendLocation sendLocation = new SendLocation(user.getChatId(), latitude, longitude);
        sendLocation.replyMarkup(botUtils.createBackButton(user));
        telegramBot.execute(sendLocation);
    }

    public void sendPhotoWithButton(TelegramUser user, String text, Keyboard buttons) {
        SendPhoto sendPhoto = createSendPhoto(user);
        sendPhoto.replyMarkup(buttons);
        sendPhoto(sendPhoto, text);
    }

    public void sendPhoto(TelegramUser user, String text) {
        SendPhoto sendPhoto = createSendPhoto(user);
        sendPhoto(sendPhoto, text);
    }

    private SendPhoto createSendPhoto(TelegramUser user) {
        byte[] bytes = photoService.downloadImage(user.getPhotoFilePath());
        return new SendPhoto(user.getChatId(), bytes);
    }

    private void sendPhoto(SendPhoto sendPhoto, String caption) {
        sendPhoto.caption(caption);
        telegramBot.execute(sendPhoto);
    }

    public void sendLocation(Object chatId, Location location, Integer messageId) {
        SendLocation sendLocation = new SendLocation(chatId, location.getLatitude(), location.getLongitude());
        sendLocation.replyToMessageId(messageId);
        telegramBot.execute(sendLocation);
    }

    public Integer sendLocation(Object chatId, Location location) {
        SendLocation sendLocation = new SendLocation(chatId, location.getLatitude(), location.getLongitude());
        return telegramBot.execute(sendLocation).message().messageId();
    }

}
