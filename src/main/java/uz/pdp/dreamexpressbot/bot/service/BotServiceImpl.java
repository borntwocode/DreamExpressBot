package uz.pdp.dreamexpressbot.bot.service;

import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.bot.BotUtils;
import uz.pdp.dreamexpressbot.company.CompanyDetails;
import uz.pdp.dreamexpressbot.entity.*;
import uz.pdp.dreamexpressbot.entity.enums.*;
import uz.pdp.dreamexpressbot.messages.BotConstants;
import uz.pdp.dreamexpressbot.service.*;
import uz.pdp.dreamexpressbot.util.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final MessageService messageService;
    private final TelegramUserService userService;
    private final BotUtils botUtils;
    private final CompanyDetails companyDetails;
    private final CityUtil cityUtil;
    private final OrderService orderService;
    private final GroupService groupService;
    private final PhotoService photoService;

    @Override
    public void askLang(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.CHOOSE_LANGUAGE.getText(), botUtils.createLangButtons());
        userService.changeUserState(user, TgState.CHOOSING_LANG);
    }

    @Override
    public void getLangThenShowMenuOrRegister(TelegramUser user, String text) {
        Lang selectedLang = Lang.getLang(text);
        Lang userLang = user.getLang();
        if (selectedLang != null) {
            userService.editLang(user, selectedLang);
            if (!user.isRegistered()) {
                askFirstname(user);
            } else if (userLang != null) {
                sendBackButtonWithSuccess(user);
            } else {
                showMenu(user);
            }
        } else {
            messageService.sendMessage(user, BotMessages.CHOOSE_LANGUAGE.getText());
        }
    }

    @Override
    public void askFirstname(TelegramUser user) {
        messageService.sendMessage(user, BotMessages.ENTER_YOUR_FIRST_NAME);
        userService.changeUserState(user, TgState.ENTERING_FIRST_NAME);
    }

    @Override
    public void getFirstNameAndAskPhoneNumber(TelegramUser user, String firstname) {
        if (ValidationUtil.checkFirstName(firstname)) {
            userService.editFirstName(user, firstname);
            askPhoneNumber(user);
        } else {
            messageService.sendMessage(user, BotMessages.INVALID_FIRST_NAME);
            askFirstname(user);
        }
    }

    private void askPhoneNumber(TelegramUser user) {
        messageService.sendMessage(user, BotMessages.ENTER_YOUR_PHONE_NUMBER);
        userService.changeUserState(user, TgState.ENTERING_PHONE_NUMBER);
    }

    @Override
    public void getPhoneNumberAndShowMenu(TelegramUser user, String phoneNumber) {
        if (ValidationUtil.checkPhoneNumber(phoneNumber)) {
            userService.editPhoneNumber(user, phoneNumber);
            messageService.sendMessage(user, BotMessages.REGISTERED);
            showMenu(user);
        } else {
            messageService.sendMessage(user, BotMessages.INVALID_PHONE_NUMBER);
            askPhoneNumber(user);
        }
    }

    @Override
    public void showMenu(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.CHOOSE, botUtils.createMenuButtons(user));
        userService.changeUserState(user, TgState.CHOOSING_MENU);
    }

    @Override
    public void handleMainMenu(TelegramUser user, String text) {
        if (text.equals(OrderType.CONTAINER.getText())) {
            showServiceMenu(user);
        } else if (text.equals(OrderType.AVIA.getText())) {
            messageService.sendWithButton(user, BotMessages.AVIA_NOT_SUPPORTED, botUtils.createBackButton(user));
            userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
        } else if (text.equals(BotMessages.MY_ORDERS.getMessage(user))) {
            showUserOrders(user);
        } else if (text.equals(BotMessages.PROFILE.getMessage(user))) {
            showProfileMenu(user);
        } else if (text.equals(BotMessages.ABOUT_US.getMessage(user))) {
            sendCompanyLocation(user);
        } else {
            resendMenuButtons(user);
        }
    }

    private void showProfileMenu(TelegramUser user) {
        String message = user.formatProfileMessage(BotMessages.PROFILE_MESSAGE.getMessage(user));
        messageService.sendWithButton(user, message, botUtils.createProfileButtons(user));
        userService.changeUserState(user, TgState.CHOOSING_PROFILE_MENU);
    }

    private void showUserOrders(TelegramUser user) {
        List<Order> orders = orderService.getUserOrders(user.getId());
        Collections.reverse(orders);
        if (orders.isEmpty()) {
            messageService.sendWithButton(user, BotMessages.YOU_HAVE_NOT_ORDER, botUtils.createBackButton(user));
        } else {
            sendOrders(user, orders);
        }
        userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
    }

    private void sendOrders(TelegramUser user, List<Order> orders) {
        Keyboard backButton = botUtils.createBackButton(user);
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            String text = orderService.getOrderMessage(order, order.getUser().getLang());
            boolean isLastOrder = (i == orders.size() - 1);
            sendOrderMessage(user, order, text, isLastOrder ? backButton : null);
        }
    }

    private void sendOrderMessage(TelegramUser user, Order order, String text, Keyboard buttons) {
        boolean isOfficeService = orderService.isOfficeService(order);
        if (buttons != null) {
            if (isOfficeService) {
                messageService.sendPhotoWithButton(user, text, buttons);
            } else {
                messageService.sendWithButton(user, text, buttons);
            }
        } else {
            if (isOfficeService) {
                messageService.sendPhoto(user, text);
            } else {
                messageService.sendMessage(user, text);
            }
        }
    }

    private void showServiceMenu(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.SERVICE_MENU_MESSAGE, botUtils.createServiceButtons(user));
        userService.editOrderType(user, OrderType.CONTAINER.getText());
        userService.changeUserState(user, TgState.CHOOSING_SERVICE);
    }

    private void resendMenuButtons(TelegramUser user) {
        messageService.sendMessage(user, BotMessages.CHOOSE_FROM_MENU);
        showMenu(user);
    }

    private void handleServiceTypeSelection(String text, TelegramUser user) {
        ServiceType serviceType = ServiceType.getFromText(text);
        if (serviceType != null) {
            handleValidServiceType(serviceType, user);
        } else {
            handleInvalidServiceType(user);
        }
    }

    private void handleValidServiceType(ServiceType serviceType, TelegramUser user) {
        if (userService.doesHavePendingOrder(user)) {
            sendAlreadyOrderedMessage(user);
        } else {
            userService.editServiceType(user, serviceType.getMessage(user));
            if (serviceType == ServiceType.SEND_FROM_HOME) {
                sendCitiesButtons(user);
            } else if (serviceType == ServiceType.SEND_TO_OFFICE) {
                askPackagePhoto(user);
            }
        }
    }

    private void handleInvalidServiceType(TelegramUser user) {
        messageService.sendMessage(user, BotMessages.CHOOSE_FROM_MENU);
        showServiceMenu(user);
    }

    @Override
    public void handleServiceMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.BACK.getMessage(user))) {
            showMenu(user);
        } else {
            handleServiceTypeSelection(text, user);
        }
    }

    private void askPackagePhoto(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.SEND_PHOTO, botUtils.createBackButton(user));
        userService.changeUserState(user, TgState.SENDING_PHOTO);
    }

    private void sendCompanyLocation(TelegramUser user) {
        String message = companyDetails.getFormattedDetails(BotMessages.COMPANY_DETAILS.getMessage(user));
        messageService.sendMessage(user, message);
        messageService.sendLocation(user, companyDetails.getLatitude(), companyDetails.getLongitude());
        userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
    }

    private void sendAlreadyOrderedMessage(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.YOU_HAVE_ORDERED, botUtils.createBackButton(user));
        userService.changeUserState(user, TgState.GOING_BACK_TO_SERVICE_MENU);
    }

    private void sendCitiesButtons(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.CHOOSE_CITY, botUtils.createCitiesButtons(user));
        userService.changeUserState(user, TgState.CHOOSING_CITY);
    }

    @Override
    public void handleBackToServiceMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.BACK.getMessage(user))) {
            showServiceMenu(user);
        } else {
            messageService.sendWithButton(user, BotMessages.CLICK_BACK_BUTTON, botUtils.createBackButton(user));
        }
    }

    @Override
    public void handleCityMenu(TelegramUser user, String text) {
        String cityName = text.substring(5);
        if (cityName.equals(BotMessages.BACK.getMessage(user))) {
            showServiceMenu(user);
        } else if (cityUtil.contains(cityName)) {
            userService.editSelectedCity(user, cityName);
            sendOrderDetails(user);
            userService.changeUserState(user, TgState.SUBMITTING_ORDER);
        } else {
            messageService.sendMessage(user, BotMessages.CHOOSE_FROM_CITY);
            sendCitiesButtons(user);
        }
    }

    public void sendOrderDetails(TelegramUser user) {
        String message = orderService.getRawOrderMessage(user);
        messageService.sendWithButton(user, message, botUtils.createSubmitButton(user));
    }

    @Override
    public void handleOrder(TelegramUser user, String text) {
        ServiceType serviceType = ServiceType.getFromText(user.getServiceType());
        if (text.equals(BotMessages.BACK.getMessage(user))) {
            switch (Objects.requireNonNull(serviceType)) {
                case SEND_FROM_HOME -> sendCitiesButtons(user);
                case SEND_TO_OFFICE -> askPackagePhoto(user);
            }
        } else if (text.equals(BotMessages.SUBMIT.getMessage(user))) {
            handleSubmitOrder(user, serviceType);
        } else {
            messageService.sendMessage(user, BotMessages.CHOOSE_FROM_MENU);
            sendOrderDetails(user);
        }
    }

    private void handleSubmitOrder(TelegramUser user, ServiceType serviceType) {
        Order order = orderService.createAndSaveOrder(user);
        String message = BotMessages.ORDER_CREATED.getMessage(user).formatted(order.getOrderNumber());
        messageService.sendWithButton(user, message, botUtils.createBackButton(user));
        switch (Objects.requireNonNull(serviceType)) {
            case SEND_FROM_HOME -> groupService.sendHomeOrder(order, orderService.getOrderMessage(order, user.getLang()));
            case SEND_TO_OFFICE -> groupService.sendOfficeOrder(order, orderService.getOrderMessage(order, user.getLang()));
        }
        userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
    }

    @Override
    public void handleBackToMainMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.BACK.getMessage(user))) {
            showMenu(user);
        } else {
            messageService.sendWithButton(user, BotMessages.CLICK_BACK_BUTTON, botUtils.createBackButton(user));
        }
    }

    @Override
    public void handleProfileMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.CHANGE_NAME.getMessage(user))) {
            askFirstnameProfile(user);
        } else if (text.equals(BotMessages.CHANGE_NUMBER.getMessage(user))) {
            askPhoneNumberProfile(user);
        } else if (text.equals(BotMessages.CHANGE_LANG.getMessage(user))) {
            askLang(user);
        } else if (text.equals(BotMessages.BACK.getMessage(user))) {
            showMenu(user);
        } else {
            messageService.sendWithButton(user, BotMessages.CHOOSE_FROM_MENU, botUtils.createProfileButtons(user));
        }
    }

    private void askFirstnameProfile(TelegramUser user) {
        messageService.sendMessage(user, BotMessages.ENTER_YOUR_FIRST_NAME);
        userService.changeUserState(user, TgState.CHANGING_FIRST_NAME);
    }

    @Override
    public void getFirstNameAskGoBack(TelegramUser user, String text) {
        if (ValidationUtil.checkFirstName(text)) {
            userService.editFirstName(user, text);
            sendBackButtonWithSuccess(user);
        } else {
            messageService.sendMessage(user, BotMessages.INVALID_FIRST_NAME);
            askFirstnameProfile(user);
        }
    }

    private void askPhoneNumberProfile(TelegramUser user) {
        messageService.sendMessage(user, BotMessages.ENTER_YOUR_PHONE_NUMBER);
        userService.changeUserState(user, TgState.CHANGING_PHONE_NUMBER);
    }

    @Override
    public void getPhoneNumberAskGoBack(TelegramUser user, String text) {
        if (ValidationUtil.checkPhoneNumber(text)) {
            userService.editPhoneNumber(user, text);
            sendBackButtonWithSuccess(user);
        } else {
            messageService.sendMessage(user, BotMessages.INVALID_PHONE_NUMBER);
            askPhoneNumberProfile(user);
        }
    }

    private void sendBackButtonWithSuccess(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.SUCCESSFULLY_CHANGED, botUtils.createBackButton(user));
        userService.changeUserState(user, TgState.GOING_BACK_TO_PROFILE_MENU);
    }

    @Override
    public void handleBackToProfileMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.BACK.getMessage(user))) {
            showProfileMenu(user);
        } else {
            messageService.sendWithButton(user, BotMessages.CLICK_BACK_BUTTON, botUtils.createBackButton(user));
        }
    }

    @Override
    public void handleOrderActions(String text) {
        String[] split = text.split("_");
        UUID orderId = UUID.fromString(split[1]);
        String action = split[2];
        if (BotConstants.CONFIRM_ORDER.equals(action)) {
            Order order = orderService.editStatus(orderId, OrderStatus.COMPLETED);
            groupService.editOrderMessage(order);
        } else if (BotConstants.REJECT_ORDER.equals(action)) {
            Order order = orderService.editStatus(orderId, OrderStatus.FAILED);
            groupService.editOrderMessage(order);
        }
    }

    @Override
    public void handlePhotoMessages(TelegramUser user, PhotoSize[] photoSizes, String text) {
        if (Objects.equals(text, BotMessages.BACK.getMessage(user))) {
            showServiceMenu(user);
        } else if (photoSizes != null && photoSizes.length > 0) {
            String photoFilePath = photoService.getFilePath(photoSizes);
            userService.editPhotoFilePath(user, photoFilePath);
            String rawOrderMessage = orderService.getRawOrderMessage(user);
            messageService.sendPhotoWithButton(user, rawOrderMessage, botUtils.createSubmitButton(user));
            userService.changeUserState(user, TgState.SUBMITTING_ORDER);
        } else {
            messageService.sendMessage(user, BotMessages.INVALID_PHOTO);
            askPackagePhoto(user);
        }
    }

}
