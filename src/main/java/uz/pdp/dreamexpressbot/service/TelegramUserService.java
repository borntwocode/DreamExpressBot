package uz.pdp.dreamexpressbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.model.Message;
import uz.pdp.dreamexpressbot.entity.Order;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.Lang;
import uz.pdp.dreamexpressbot.entity.enums.TgState;
import uz.pdp.dreamexpressbot.repo.OrderRepo;
import uz.pdp.dreamexpressbot.repo.TelegramUserRepo;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepo telegramUserRepo;
    private final OrderRepo orderRepo;

    public TelegramUser findUser(Message message) {
        Long chatId = message.chat().id();
        Long userId = message.from().id();
        String username = message.from().username();
        return telegramUserRepo.findByChatId(chatId)
                .orElseGet(() -> createAndSaveUser(chatId, userId, username));
    }

    public TelegramUser findUser(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.from().id();
        String username = callbackQuery.from().username();
        return telegramUserRepo.findByChatId(userId)
                .orElseGet(() -> createAndSaveUser(userId, userId, username));
    }

    private TelegramUser createAndSaveUser(Long chatId, Long userId, String username) {
        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .userId(userId)
                .username(username)
                .state(TgState.START)
                .build();
        return telegramUserRepo.save(user);
    }

    public void changeUserState(TelegramUser user, TgState state) {
        user.setState(state);
        telegramUserRepo.save(user);
    }

    public void editFirstName(TelegramUser user, String firstname) {
        user.setFirstName(firstname);
        telegramUserRepo.save(user);
    }

    public void editPhoneNumber(TelegramUser user, String phoneNumber) {
        String cleanedPhoneNumber = phoneNumber.replaceAll("\\s+", "");
        // Determine if the number is Uzbek or Korean and add the appropriate country code if missing
        String phoneWithCountryCode;
        if (cleanedPhoneNumber.matches("^\\d{9}$")) {
            phoneWithCountryCode = "+998" + cleanedPhoneNumber;
        } else if (cleanedPhoneNumber.matches("^010\\d{7,8}$")) {
            // Assume it's Korean if it starts with '010' and is 7 or 8 digits long
            phoneWithCountryCode = "+82" + cleanedPhoneNumber.substring(1); // Remove leading '0' for Korean numbers
        } else {
            // If it already has a valid country code, leave it as is
            phoneWithCountryCode = cleanedPhoneNumber;
        }
        // Set and save the updated phone number
        user.setPhoneNumber(phoneWithCountryCode);
        telegramUserRepo.save(user);
    }

    public void editOrderType(TelegramUser user, String text) {
        user.setOrderType(text);
        telegramUserRepo.save(user);
    }

    public void editSelectedCity(TelegramUser user, String cityName) {
        user.setSelectedCity(cityName);
        telegramUserRepo.save(user);
    }

    public boolean doesHavePendingOrder(TelegramUser user) {
        Optional<Order> orderOptional = orderRepo.findPendingOrderByUserId(user.getId());
        return orderOptional.isPresent();
    }

    public void editLang(TelegramUser user, Lang selectedLang) {
        user.setLang(selectedLang);
        telegramUserRepo.save(user);
    }

    public void editServiceType(TelegramUser user, String text) {
        user.setServiceType(text);
        telegramUserRepo.save(user);
    }

    public void editPhotoFilePath(TelegramUser user, String photoFilePath) {
        user.setPhotoFilePath(photoFilePath);
        telegramUserRepo.save(user);
    }

}
