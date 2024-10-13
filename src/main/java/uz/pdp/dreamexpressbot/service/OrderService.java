package uz.pdp.dreamexpressbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.entity.*;
import uz.pdp.dreamexpressbot.entity.enums.*;
import uz.pdp.dreamexpressbot.repo.OrderRepo;
import uz.pdp.dreamexpressbot.repo.TelegramUserRepo;
import uz.pdp.dreamexpressbot.util.DateTimeUtil;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final PhotoService photoService;
    private final TelegramUserRepo telegramUserRepo;

    public String getRawOrderMessage(TelegramUser user) {
        ServiceType serviceType = ServiceType.getFromText(user.getServiceType());
        switch (Objects.requireNonNull(serviceType)) {
            case SEND_FROM_HOME -> {
                String message = BotMessages.RAW_HOME_ORDER_DETAILS.getMessage(user);
                return user.formatHomeOrderMessage(message);
            }
            case SEND_TO_OFFICE -> {
                String message = BotMessages.RAW_OFFICE_ORDER_DETAILS.getMessage(user);
                return user.formatOfficeOrderMessage(message);
            }
        }
        throw new RuntimeException("Service type not supported");
    }

    public synchronized Order createAndSaveOrder(TelegramUser user) {
        ServiceType serviceType = ServiceType.getFromText(user.getServiceType());
        var orderDetails = OrderDetails.builder()
                .userFirstName(user.getFirstName())
                .userPhoneNumber(user.getPhoneNumber())
                .orderType(OrderType.getFromText(user.getOrderType()))
                .serviceType(serviceType)
                .load_weight(user.getLoad_weight())
                .build();
        if (serviceType == ServiceType.SEND_FROM_HOME) {
            orderDetails.setUserAddress(user.getSelectedCity());
            orderDetails.setLocation(
                    Location.builder()
                            .latitude(user.getLatitude())
                            .longitude(user.getLongitude())
                            .build()
            );
        } else if (serviceType == ServiceType.SEND_TO_OFFICE) {
            Photo photo = photoService.getPhoto(user.getPhotoFilePath());
            orderDetails.setPhoto(photo);
        }
        Order order = Order.builder()
                .orderDetails(orderDetails)
                .orderNumber(findNextOrderNumber())
                .user(user)
                .status(OrderStatus.PENDING)
                .lang(user.getLang())
                .build();
        return orderRepo.save(order);
    }

    public String getOrderMessage(Order order, Lang lang) {
        OrderDetails orderDetails = order.getOrderDetails();
        ServiceType serviceType = ServiceType.getFromText(order.getUser().getServiceType());
        if (serviceType == ServiceType.SEND_FROM_HOME) {
            String rawMessage = BotMessages.HOME_ORDER_DETAILS.getMessage(lang);
            return rawMessage.formatted(
                    order.getOrderNumber(),
                    orderDetails.getOrderType().getText(),
                    orderDetails.getServiceType().getMessage(lang),
                    orderDetails.getLoad_weight(),
                    order.getStatus().getMessage(lang),
                    DateTimeUtil.formatDate(order.getOrderDateTime()),
                    DateTimeUtil.formatTime(order.getOrderDateTime()),
                    orderDetails.getUserFirstName(),
                    orderDetails.getUserAddress(),
                    orderDetails.getUserPhoneNumber()
            );
        } else {
            String rawMessage = BotMessages.OFFICE_ORDER_DETAILS.getMessage(lang);
            return rawMessage.formatted(
                    order.getOrderNumber(),
                    orderDetails.getOrderType().getText(),
                    orderDetails.getServiceType().getMessage(lang),
                    order.getStatus().getMessage(lang),
                    orderDetails.getLoad_weight(),
                    DateTimeUtil.formatDate(order.getOrderDateTime()),
                    DateTimeUtil.formatTime(order.getOrderDateTime()),
                    orderDetails.getUserFirstName(),
                    orderDetails.getUserPhoneNumber()
            );
        }
    }

    private Long findNextOrderNumber() {
        Optional<Order> lastOrder = orderRepo.findLastOrder();
        return lastOrder.map(order -> order.getOrderNumber() + 1)
                .orElse(1000L);
    }

    public List<Order> getUserOrders(UUID userId) {
        return orderRepo.getLastFiveOrdersByUserId(userId);
    }

    public void editMessageId(Order order, Integer messageId) {
        order.setGroupMessageId(messageId);
        orderRepo.save(order);
    }

    public boolean isOfficeService(Order order) {
        OrderDetails orderDetails = order.getOrderDetails();
        ServiceType serviceType = orderDetails.getServiceType();
        return serviceType == ServiceType.SEND_TO_OFFICE;
    }

    public Order editStatus(UUID orderId, OrderStatus orderStatus) {
        return orderRepo.findById(orderId)
                .map(order -> {
                    order.setStatus(orderStatus);
                    return orderRepo.save(order);
                }).orElse(null);
    }

    public void saveOrderDatailsWeight(TelegramUser user, int weight) {
        user.setLoad_weight(String.valueOf(weight));
        telegramUserRepo.save(user);
    }
}
