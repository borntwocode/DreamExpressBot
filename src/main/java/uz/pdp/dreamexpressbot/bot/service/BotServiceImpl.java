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
    private final RegionUtil regionUtil;
    private final OrderService orderService;
    private final GroupService groupService;
    private final PhotoService photoService;
    private final ChannelService channelService;
    private final FaqUtil faqUtil;

    @Override
    public void onStartCommand(TelegramUser user) {
        if (!user.isRegistered()) {
            askLang(user);
        } else {
            showMenu(user);
        }
    }

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
        messageService.sendContact(user, BotMessages.ENTER_YOUR_PHONE_NUMBER);
        userService.changeUserState(user, TgState.ENTERING_PHONE_NUMBER);
    }



    @Override
    public void getContactAndShowMenu(TelegramUser user, String phoneNumber) {
        if (ValidationUtil.checkPhoneNumber(phoneNumber)) {
            userService.editPhoneNumber(user, phoneNumber);
            // Check if user's language is null and set a default if necessary
            if (user.getLang() == null) {
                userService.editLang(user, Lang.UZ); // assuming Lang.DEFAULT is your default language
            }
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
        if (text.equals(OrderType.TAXI.getText())) {
            showServiceMenu(user);
        } else if (text.equals(OrderType.MAIL.getText())) {
            messageService.sendWithButton(user, BotMessages.AVIA_NOT_SUPPORTED, botUtils.createBackButton(user));
            userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
        } else if (text.equals(OrderType.YOL_YOLAKAY.getText())){
            messageService.sendWithButton(user, BotMessages.AVIA_NOT_SUPPORTED, botUtils.createBackButton(user));
            userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
        } else if (text.equals(BotMessages.MY_ORDERS.getMessage(user))) {
            showUserOrders(user);
        } else if (text.equals(BotMessages.PROFILE.getMessage(user))) {
            showProfileMenu(user);
        } else if (text.equals(BotMessages.ABOUT_US.getMessage(user))) {
            sendCompanyLocation(user);
        } else if (text.equals(BotMessages.FAQ.getMessage(user))) {
            sendFaqButtons(user);
        } else {
            resendMenuButtons(user);
        }
    }


    private void sendFaqButtons(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.SELECT_FAQ, botUtils.createFAQButtons(user));
        userService.changeUserState(user, TgState.SELECTING_FAQ);
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
            Location location = order.getOrderDetails().getLocation();
            boolean isLastOrder = (i == orders.size() - 1);
            Integer messageId = sendOrderMessage(user, order, text, isLastOrder ? backButton : null);
            if(location != null) messageService.sendLocation(user.getChatId(), location, messageId);
        }
    }

    private Integer sendOrderMessage(TelegramUser user, Order order, String text, Keyboard buttons) {
        Integer messageId = 0;
        boolean isOfficeService = orderService.isOfficeService(order);
        if (buttons != null) {
            if (isOfficeService) {
                messageService.sendPhotoWithButton(user, text, buttons);
            } else {
                messageId = messageService.sendWithButton(user, text, buttons);
            }
        } else {
            if (isOfficeService) {
                messageService.sendPhoto(user, text);
            } else {
                messageId = messageService.sendMessage(user, text);
            }
        }
        return messageId;
    }

    private void showServiceMenu(TelegramUser user) {
        userService.editOrderType(user, OrderType.TAXI.getText());
        messageService.sendMessage(user, BotMessages.SERVICE_MENU);
//        messageService.sendMessage(user, BotMessages.LOAD_WEIGHT.getMessage(user));
//        userService.changeUserState(user, TgState.GOING_BACK_TO_lOAD_WEIGHT);

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
            if (serviceType == ServiceType.TAXI) {
                sendCitiesButtons(user);
            } else if (serviceType == ServiceType.MAIL) {
                askPackagePhoto(user);
            } else if (serviceType == ServiceType.YOL_YOLAKAY) {
                sendCitiesButtons(user);
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
        } else if (regionUtil.contains(cityName)) {
            userService.editSelectedCity(user, cityName);
            messageService.sendWithButton(user, BotMessages.SEND_LOCATION, botUtils.createLocationButton(user));
            userService.changeUserState(user, TgState.SENDING_LOCATION);
        } else {
            messageService.sendMessage(user, BotMessages.CHOOSE_FROM_CITY);
            sendCitiesButtons(user);
        }
    }

    public Integer sendOrderDetails(TelegramUser user) {
        String message = orderService.getRawOrderMessage(user);
        return messageService.sendWithButton(user, message, botUtils.createSubmitButton(user));
    }

    @Override
    public void handleOrder(TelegramUser user, String text) {
        ServiceType serviceType = ServiceType.getFromText(user.getServiceType());
        if (text.equals(BotMessages.BACK.getMessage(user))) {
            switch (Objects.requireNonNull(serviceType)) {
                case TAXI, YOL_YOLAKAY -> sendCitiesButtons(user);
                case MAIL -> askPackagePhoto(user);
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
            case TAXI, YOL_YOLAKAY ->
                    groupService.sendHomeOrder(order, orderService.getOrderMessage(order, user.getLang()));
            case MAIL ->
                    groupService.sendOfficeOrder(order, orderService.getOrderMessage(order, user.getLang()));
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

    @Override
    public boolean isSubscribed(Long userId) {
        return channelService.isUserSubscribed(userId);
    }

    @Override
    public void promptToFollow(TelegramUser user) {
        messageService.sendWithButton(user, BotMessages.FOLLOW_CHANNEL.getText(), botUtils.createFollowChannelButtons(user));
    }

    @Override
    public void handleSubscription(TelegramUser user, String text) {
        if (isSubscribed(user.getUserId())) {
            onStartCommand(user);
        } else {
            messageService.sendMessage(user, BotMessages.NOT_FOLLOWED.getText());
        }
    }

    @Override
    public void handleQuestionAskGoBack(TelegramUser user, String question) {
        if (faqUtil.containsQuestion(user, question)) {
            String answer = faqUtil.getAnswer(user, question);
            messageService.sendWithButton(user, answer, botUtils.createBackButton(user));
            userService.changeUserState(user, TgState.GOING_BACK_TO_QUESTION_MENU);
        } else if (question.equals(BotMessages.BACK.getMessage(user))) {
            showMenu(user);
        } else {
            messageService.sendMessage(user, BotMessages.SELECT_FROM_FAQ);
            sendFaqButtons(user);
        }
    }

    @Override
    public void handleBackToFAQMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.BACK.getMessage(user))) {
            sendFaqButtons(user);
        } else {
            messageService.sendWithButton(user, BotMessages.CLICK_BACK_BUTTON, botUtils.createBackButton(user));
        }
    }
    @Override
    public void handleLocationMessages(TelegramUser user, com.pengrad.telegrambot.model.Location location) {
        Location customLocation = Location.builder()
                .latitude(location.latitude())
                .longitude(location.longitude())
                .build();
        userService.editLocation(user, customLocation);
        Integer messageId = sendOrderDetails(user);
        messageService.sendLocation(user.getChatId(), customLocation, messageId);
        userService.changeUserState(user, TgState.SUBMITTING_ORDER);
    }
    @Override
    public void handleBackToLoadWeight(TelegramUser user, String text) {
        String[] numbersArray = text.trim().split("\\s+");
        int weight = 0;
        for (String number : numbersArray) {
            try {
                int num = Integer.parseInt(number);
                weight += num;
            } catch (NumberFormatException e) {
                messageService.sendMessage(user, BotMessages.INVALID_LOAD_WEIGHT);
                return;
            }
        }
        if (weight > 24) {
            orderService.saveOrderDatailsWeight(user, weight);
            messageService.sendMessage(user, BotMessages.CONFIRM.getMessage(user));
            messageService.sendWithButton(user, BotMessages.SERVICE_MENU_MESSAGE, botUtils.createServiceButtons(user));
            userService.changeUserState(user, TgState.CHOOSING_SERVICE);
//        } else {
//            orderService.saveOrderDatailsWeight(user, weight);
//            messageService.sendMessage(user, BotMessages.REJECT_WEIGHT.getMessage(user));
//            messageService.sendWithButton(user, BotMessages.SERVICE_MENU_MESSAGE, botUtils.createServiceOnlyOfficeButtons(user));
//            userService.changeUserState(user, TgState.CHOOSING_SERVICE);
//        }
        }
    }


}
