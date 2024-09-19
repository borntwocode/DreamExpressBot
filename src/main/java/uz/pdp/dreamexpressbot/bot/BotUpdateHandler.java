package uz.pdp.dreamexpressbot.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.bot.service.BotService;
import uz.pdp.dreamexpressbot.entity.Photo;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.TgState;
import uz.pdp.dreamexpressbot.messages.BotCommands;
import uz.pdp.dreamexpressbot.service.PhotoService;
import uz.pdp.dreamexpressbot.service.TelegramUserService;

@Service
@RequiredArgsConstructor
public class BotUpdateHandler {

    private final BotService botService;
    private final TelegramUserService userService;
    private final PhotoService photoService;

    @Async
    public void handleUpdate(Update update) {
        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }
    }

    private void handleMessage(Message message) {
        String text = message.text();
        PhotoSize[] photo = message.photo();
        TelegramUser user = userService.findUser(message);
        if (text != null && !user.getState().equals(TgState.SENDING_PHOTO)) {
            handleTextMessages(user, text);
        } else if (user.getState().equals(TgState.SENDING_PHOTO)) {
            botService.handlePhotoMessages(user, photo, text);
        }
    }

    private void handleTextMessages(TelegramUser user, String text) {
        if (text.equals(BotCommands.START)) {
            if (!user.isRegistered()) {
                botService.askLang(user);
            } else {
                botService.showMenu(user);
            }
        } else {
            switch (user.getState()) {
                case ENTERING_FIRST_NAME -> botService.getFirstNameAndAskPhoneNumber(user, text);
                case ENTERING_PHONE_NUMBER -> botService.getPhoneNumberAndShowMenu(user, text);
                case CHOOSING_MENU -> botService.handleMainMenu(user, text);
                case CHOOSING_SERVICE -> botService.handleServiceMenu(user, text);
                case GOING_BACK_TO_SERVICE_MENU -> botService.handleBackToServiceMenu(user, text);
                case GOING_BACK_TO_MAIN_MENU -> botService.handleBackToMainMenu(user, text);
                case GOING_BACK_TO_PROFILE_MENU -> botService.handleBackToProfileMenu(user, text);
                case SUBMITTING_ORDER -> botService.handleOrder(user, text);
                case CHOOSING_PROFILE_MENU -> botService.handleProfileMenu(user, text);
                case CHANGING_FIRST_NAME -> botService.getFirstNameAskGoBack(user, text);
                case CHANGING_PHONE_NUMBER -> botService.getPhoneNumberAskGoBack(user, text);
            }
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String text = callbackQuery.data();
        if (text != null) {
            TelegramUser user = userService.findUser(callbackQuery);
            handleDataCallbackQueries(user, text);
        }
    }

    private void handleDataCallbackQueries(TelegramUser user, String text) {
        if (text.startsWith("ORDER")) {
            botService.handleOrderActions(text);
        } else if (user.getState().equals(TgState.CHOOSING_LANG)) {
            botService.getLangThenShowMenuOrRegister(user, text);
        } else if (user.getState().equals(TgState.CHOOSING_CITY) && text.startsWith("CITY_")) {
            botService.handleCityMenu(user, text);
        }
    }


}
