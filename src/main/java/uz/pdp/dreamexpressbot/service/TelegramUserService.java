package uz.pdp.dreamexpressbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.model.Message;
import uz.pdp.dreamexpressbot.entity.Location;
import uz.pdp.dreamexpressbot.entity.Order;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.BotMessages;
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
    private final MessageService messageService;

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
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            messageService.sendMessage(user, BotMessages.INVALID_PHONE_NUMBER);
        }

        // Remove spaces from the phone number
        assert phoneNumber != null;
        String cleanedPhoneNumber = phoneNumber.replaceAll("\\s+", "");

        // Regular expressions for Uzbek numbers
        String regexUzbekWithCode = "^\\+998\\d{9}$";  // +998 followed by 9 digits
        String regexUzbekWithoutCode = "^\\d{9}$";     // 9 digits without country code
        String regexUzbekWithoutPlus = "^998\\d{9}$";  // 998 followed by 9 digits without the +

        // Regular expressions for Korean numbers
        String regexKoreanWithCode = "^\\+82\\d{8,10}$";     // +82 followed by 8 to 10 digits
        String regexKoreanWithoutCode = "^010\\d{7,8}$";     // 010 followed by 7 or 8 digits
        String regexKoreanWithoutPlus = "^82\\d{8,10}$";     // 82 followed by 8 to 10 digits without the +

        String phoneWithCountryCode;

        // Check if it's a valid Uzbek number with or without country code or missing '+'
        if (cleanedPhoneNumber.matches(regexUzbekWithoutCode)) {
            phoneWithCountryCode = "+998" + cleanedPhoneNumber;
        } else if (cleanedPhoneNumber.matches(regexUzbekWithCode)) {
            phoneWithCountryCode = cleanedPhoneNumber;
        } else if (cleanedPhoneNumber.matches(regexUzbekWithoutPlus)) {
            phoneWithCountryCode = "+" + cleanedPhoneNumber; // Add the missing '+' sign
        }
        // Check if it's a valid Korean number with or without country code
        else if (cleanedPhoneNumber.matches(regexKoreanWithoutCode)) {
            phoneWithCountryCode = "+82" + cleanedPhoneNumber.substring(1); // Remove leading '0' for Korean numbers
        } else if (cleanedPhoneNumber.matches(regexKoreanWithCode)) {
            phoneWithCountryCode = cleanedPhoneNumber;
        } else if (cleanedPhoneNumber.matches(regexKoreanWithoutPlus)) {
            phoneWithCountryCode = "+" + cleanedPhoneNumber; // Add the missing '+' sign
        } else {
            messageService.sendMessage(user, BotMessages.INVALID_PHONE_NUMBER);
            return;
        }

        user.setPhoneNumber(phoneWithCountryCode);
        telegramUserRepo.save(user);
    }

//    public static boolean checkPhoneNumber(String phoneNumber) {
//        String cleanedPhoneNumber = phoneNumber.replaceAll(" ", "");
//
//        // Regular expressions for Uzbek numbers
//        String regexUzbekWithCode = "^\\+998\\d{9}$"; // +998 followed by 9 digits
//        String regexUzbekWithoutCode = "^\\d{9}$"; // 9 digits without country code
//
//        // Regular expressions for Korean numbers
//        String regexKoreanWithCode = "^\\+82\\d{8,10}$"; // +82 followed by 8 to 10 digits
//        String regexKoreanWithoutCode = "^010\\d{7,8}$"; // 010 followed by 7 or 8 digits
//
//        // Check if the phone number matches any of the valid Uzbek or Korean patterns
//        return cleanedPhoneNumber.matches(regexUzbekWithCode) || cleanedPhoneNumber.matches(regexUzbekWithoutCode) ||
//                cleanedPhoneNumber.matches(regexKoreanWithCode) || cleanedPhoneNumber.matches(regexKoreanWithoutCode);
//    }


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

    public void editLocation(TelegramUser user, Location location) {
        user.setLatitude(location.getLatitude());
        user.setLongitude(location.getLongitude());
        telegramUserRepo.save(user);
    }

}
