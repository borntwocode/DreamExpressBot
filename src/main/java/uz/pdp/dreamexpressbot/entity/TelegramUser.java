package uz.pdp.dreamexpressbot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.dreamexpressbot.entity.enums.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TelegramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long chatId;

    private Long userId;

    private String phoneNumber;

    private String firstName;

    private String username;

    private String orderType;

    private String serviceType;

    private String selectedCity;

    private String photoFilePath;

    @Enumerated(EnumType.STRING)
    private Lang lang;

    private TgState state;

    public boolean isRegistered() {
        return lang != null && phoneNumber != null && firstName != null;
    }

    public String formatOfficeOrderMessage(String message) {
        return message.formatted(orderType, serviceType, firstName, phoneNumber);
    }

    public String formatProfileMessage(String message) {
        return message.formatted(firstName, phoneNumber, lang.getText());
    }

    public String formatHomeOrderMessage(String message) {
        return message.formatted(orderType, serviceType, firstName, selectedCity, phoneNumber);
    }

}
