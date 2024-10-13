package uz.pdp.dreamexpressbot.bot;

import com.pengrad.telegrambot.model.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.entity.FAQ;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.BotMessages;
import uz.pdp.dreamexpressbot.entity.enums.Lang;
import uz.pdp.dreamexpressbot.entity.enums.OrderType;
import uz.pdp.dreamexpressbot.entity.enums.ServiceType;
import uz.pdp.dreamexpressbot.messages.BotConstants;
import uz.pdp.dreamexpressbot.service.ChannelService;
import uz.pdp.dreamexpressbot.util.CityUtil;
import uz.pdp.dreamexpressbot.util.FaqUtil;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BotUtils {

    private final CityUtil cityUtil;
    private final ChannelService channelService;
    private final FaqUtil faqUtil;

    public Keyboard createLangButtons() {
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.addRow(
                new InlineKeyboardButton(Lang.UZ.getText()).callbackData(Lang.UZ.getText()),
                new InlineKeyboardButton(Lang.RU.getText()).callbackData(Lang.RU.getText())
        );
        return keyboardMarkup;
    }

    public Keyboard createMenuButtons(TelegramUser user) {
        var keyboardMarkup = new ReplyKeyboardMarkup(
                OrderType.CONTAINER.getText(),
                OrderType.AVIA.getText()    
        );
        keyboardMarkup.addRow(
                BotMessages.MY_ORDERS.getMessage(user),
                BotMessages.PROFILE.getMessage(user)
        );
        keyboardMarkup.addRow(
                BotMessages.ABOUT_US.getMessage(user),
                BotMessages.FAQ.getMessage(user)
        );
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createServiceButtons(TelegramUser user) {
        var keyboardMarkup = new ReplyKeyboardMarkup(
                ServiceType.SEND_FROM_HOME.getMessage(user),
                ServiceType.SEND_TO_OFFICE.getMessage(user)
        );
        keyboardMarkup.addRow(BotMessages.BACK.getMessage(user));
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createBackButton(TelegramUser user) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(
                BotMessages.BACK.getMessage(user)
        );
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }
    public Keyboard createContactButton(TelegramUser user) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(BotConstants.CONTACT).requestContact(true)
        );
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createCitiesButtons(TelegramUser user) {
        var keyboardMarkup = cityUtil.getCityNameMarkup();
        String back = BotMessages.BACK.getMessage(user);
        keyboardMarkup.addRow(
                new InlineKeyboardButton(back).callbackData("CITY_" + back)
        );
        return keyboardMarkup;
    }

    public Keyboard createSubmitButton(TelegramUser user) {
        var keyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(BotMessages.SUBMIT.getMessage(user)),
                new KeyboardButton(BotMessages.BACK.getMessage(user))
        );
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createProfileButtons(TelegramUser user) {
        var keyboardMarkup = new ReplyKeyboardMarkup(
                BotMessages.CHANGE_NAME.getMessage(user),
                BotMessages.CHANGE_NUMBER.getMessage(user)
        );
        keyboardMarkup.addRow(
                BotMessages.CHANGE_LANG.getMessage(user),
                BotMessages.BACK.getMessage(user)
        );
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createOrderAdminButtons(UUID orderId) {
        String confirmOrder = BotConstants.CONFIRM_ORDER;
        String rejectOrder = BotConstants.REJECT_ORDER;
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.addRow(
                new InlineKeyboardButton(confirmOrder).callbackData("ORDER_" + orderId + "_" + confirmOrder),
                new InlineKeyboardButton(rejectOrder).callbackData("ORDER_" + orderId + "_" + rejectOrder)
        );
        return keyboardMarkup;
    }

    public Keyboard createFollowChannelButtons(TelegramUser user) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(BotConstants.FOLLOW).url(channelService.getChannelUrl())
        ).addRow(new InlineKeyboardButton(BotConstants.CHECK).callbackData(BotConstants.CHECK + "/" + user.getUserId()));
    }

    public Keyboard createFAQButtons(TelegramUser user) {
        String[][] questionsAsMatrix = faqUtil.getQuestionsAsMatrix(user);
        var keyboardMarkup = new ReplyKeyboardMarkup(questionsAsMatrix);
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createLocationButton(TelegramUser user) {
        var keyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(BotMessages.SHARE_LOCATION.getMessage(user)).requestLocation(true)
        );
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

}
