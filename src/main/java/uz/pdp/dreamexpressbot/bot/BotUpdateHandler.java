package uz.pdp.dreamexpressbot.bot;

import com.pengrad.telegrambot.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.bot.service.BotService;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.TgState;
import uz.pdp.dreamexpressbot.messages.BotCommands;
import uz.pdp.dreamexpressbot.messages.BotConstants;
import uz.pdp.dreamexpressbot.service.TelegramUserService;

@Service
@RequiredArgsConstructor
public class BotUpdateHandler {

    private final BotService botService;
    private final TelegramUserService userService;

    @Async
    public void handleUpdate(Update update) {
        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }
    }

    private void handleMessage(Message message) {
        String text = message.text(); // This might be null
        Location location = message.location();
        Contact contact = message.contact();
        PhotoSize[] photo = message.photo();
        TelegramUser user = userService.findUser(message);

        if (botService.isSubscribed(user.getUserId())) {
            // Proceed only if contact is present or text is not null
            if (contact != null || (text != null && !user.getState().equals(TgState.SENDING_PHOTO))) {
                handleTextMessages(user, text, contact);
            } else if (user.getState().equals(TgState.SENDING_PHOTO)) {
                botService.handlePhotoMessages(user, photo, text);
            } else if (location != null && user.getState().equals(TgState.SENDING_LOCATION)) {
                botService.handleLocationMessages(user, location);
            }
        } else {
            botService.promptToFollow(user);
        }
    }

    private void handleTextMessages(TelegramUser user, String text, Contact contact) {

        if (text != null) {
            if (text.equals(BotCommands.START)) {
                botService.onStartCommand(user);
            } else {
                switch (user.getState()) {
                    case ENTERING_FIRST_NAME -> botService.getFirstNameAndAskPhoneNumber(user, text);
                    case ENTERING_PHONE_NUMBER -> botService.getContactAndShowMenu(user, contact != null ? contact.phoneNumber() : (text));
                    case CHOOSING_MENU -> botService.handleMainMenu(user, text);
                    case CHOOSING_SERVICE -> botService.handleServiceMenu(user, text);
                    case CHOOSING_CITY -> botService.handleCityMenu(user, text);
                    case GOING_BACK_TO_lOAD_WEIGHT -> botService.handleBackToLoadWeight(user, text);
                    case GOING_BACK_TO_SERVICE_MENU -> botService.handleBackToServiceMenu(user, text);
                    case GOING_BACK_TO_MAIN_MENU -> botService.handleBackToMainMenu(user, text);
                    case GOING_BACK_TO_PROFILE_MENU -> botService.handleBackToProfileMenu(user, text);
                    case GOING_BACK_TO_QUESTION_MENU -> botService.handleBackToFAQMenu(user, text);
                    case SUBMITTING_ORDER -> botService.handleOrder(user, text);
                    case CHOOSING_PROFILE_MENU -> botService.handleProfileMenu(user, text);
                    case CHANGING_FIRST_NAME -> botService.getFirstNameAskGoBack(user, text);
                    case CHANGING_PHONE_NUMBER -> botService.getPhoneNumberAskGoBack(user, text);
                    case SELECTING_FAQ -> botService.handleQuestionAskGoBack(user, text);
                }
            }
        } else if (contact != null && contact.phoneNumber()!=null) {
            botService.getContactAndShowMenu(user, contact.phoneNumber());
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
        if (text.startsWith(BotConstants.CHECK)) {
            botService.handleSubscription(user, text);
        } else if (text.startsWith("ORDER")) {
            botService.handleOrderActions(text);
        } else if (user.getState().equals(TgState.CHOOSING_LANG)) {
            botService.getLangThenShowMenuOrRegister(user, text);
        } else if (user.getState().equals(TgState.CHOOSING_CITY) && text.startsWith("CITY_")) {
            botService.handleCityMenu(user, text);
        }
    }


}
