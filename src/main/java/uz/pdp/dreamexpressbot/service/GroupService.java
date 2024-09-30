package uz.pdp.dreamexpressbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.bot.BotUtils;
import uz.pdp.dreamexpressbot.entity.Order;
import uz.pdp.dreamexpressbot.entity.enums.ServiceType;
import uz.pdp.dreamexpressbot.repo.PhotoRepo;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final MessageService messageService;
    @Value("${admin.group.chat.id}")
    private String groupChatId;

    private final TelegramBot telegramBot;
    private final OrderService orderService;
    private final BotUtils botUtils;
    private final PhotoRepo photoRepo;

    public void sendHomeOrder(Order order, String message) {
        Integer messageId = messageService.sendLocation(groupChatId, order.getOrderDetails().getLocation());
        SendMessage sendMessage = new SendMessage(groupChatId, message);
        sendMessage.replyMarkup(botUtils.createOrderAdminButtons(order.getId()));
        sendMessage.replyToMessageId(messageId);
        SendResponse execute = telegramBot.execute(sendMessage);
        orderService.editMessageId(order, execute.message().messageId());
    }

    public void sendOfficeOrder(Order order, String message) {
        byte[] content = photoRepo.findPhotoContentByOrderId(order.getId());
        SendPhoto sendPhoto = new SendPhoto(groupChatId, content);
        sendPhoto.replyMarkup(botUtils.createOrderAdminButtons(order.getId()));
        sendPhoto.caption(message);
        Integer messageId = telegramBot.execute(sendPhoto).message().messageId();
        orderService.editMessageId(order, messageId);
    }

    public void editOrderMessage(Order order) {
        Integer messageId = order.getGroupMessageId();
        String newMessage = orderService.getOrderMessage(order, order.getLang());
        if (order.getOrderDetails().getServiceType().equals(ServiceType.SEND_TO_OFFICE)) {
            updateMessageCaption(messageId, newMessage);
        } else {
            updateMessageText(messageId, newMessage);
        }
        updateMessageReplyMarkup(messageId);
    }

    private void updateMessageCaption(Integer messageId, String caption) {
        var editMessageCaption = new EditMessageCaption(groupChatId, messageId);
        editMessageCaption.caption(caption);
        telegramBot.execute(editMessageCaption);
    }

    private void updateMessageText(Integer messageId, String text) {
        var editMessageText = new EditMessageText(groupChatId, messageId, text);
        telegramBot.execute(editMessageText);
    }

    private void updateMessageReplyMarkup(Integer messageId) {
        var editMessageReplyMarkup = new EditMessageReplyMarkup(groupChatId, messageId);
        editMessageReplyMarkup.replyMarkup(new InlineKeyboardMarkup());
        telegramBot.execute(editMessageReplyMarkup);
    }

}
