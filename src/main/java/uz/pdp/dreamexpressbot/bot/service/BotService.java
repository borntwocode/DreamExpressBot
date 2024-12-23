package uz.pdp.dreamexpressbot.bot.service;

import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.PhotoSize;
import uz.pdp.dreamexpressbot.entity.TelegramUser;

public interface BotService {

    void onStartCommand(TelegramUser user);

    void askLang(TelegramUser user);

    void getLangThenShowMenuOrRegister(TelegramUser user, String text);

    void askFirstname(TelegramUser user);

    void getFirstNameAndAskPhoneNumber(TelegramUser user, String text);

    void getContactAndShowMenu(TelegramUser user, String text);

    void showMenu(TelegramUser user);

    void handleMainMenu(TelegramUser user, String text);

    void handleServiceMenu(TelegramUser user, String text);

    void handleBackToServiceMenu(TelegramUser user, String text);

    void handleCityMenu(TelegramUser user, String text);

    void handleOrder(TelegramUser user, String text);

    void handleBackToMainMenu(TelegramUser user, String text);

    void handleProfileMenu(TelegramUser user, String text);

    void getFirstNameAskGoBack(TelegramUser user, String text);

    void handleBackToProfileMenu(TelegramUser user, String text);

    void getPhoneNumberAskGoBack(TelegramUser user, String text);

    void handleOrderActions(String text);

    void handlePhotoMessages(TelegramUser user, PhotoSize[] photo, String text);

    boolean isSubscribed(Long userId);

    void promptToFollow(TelegramUser userId);

    void handleSubscription(TelegramUser user, String text);

    void handleQuestionAskGoBack(TelegramUser user, String text);

    void handleBackToFAQMenu(TelegramUser user, String text);

    void handleLocationMessages(TelegramUser user, Location location);

    void handleBackToLoadWeight(TelegramUser user, String text);
}
