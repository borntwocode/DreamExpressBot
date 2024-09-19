package uz.pdp.dreamexpressbot.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uz.pdp.dreamexpressbot.entity.TelegramUser;

@Getter
@AllArgsConstructor
public enum ServiceType {

    SEND_FROM_HOME("\uD83C\uDFE0 Uydan zakaz berish", "\uD83C\uDFE0 Заказать из дома"),
    SEND_TO_OFFICE("\uD83D\uDCC5 Office ga yuborish", "\uD83D\uDCC5 Отправить в офис");

    private final String uz;
    private final String ru;

    public static ServiceType getFromText(String text) {
        if (text.equals(SEND_FROM_HOME.getRu()) || text.equals(SEND_FROM_HOME.getUz())) {
            return SEND_FROM_HOME;
        } else if (text.equals(SEND_TO_OFFICE.getRu()) || text.equals(SEND_TO_OFFICE.getUz())) {
            return SEND_TO_OFFICE;
        } else {
            return null;
        }
    }

    public String getMessage(TelegramUser user) {
        String languageCode = user.getLang().getText();
        if (languageCode != null && languageCode.equalsIgnoreCase(Lang.RU.getText())) {
            return ru;
        }
        return uz;
    }

    public Object getMessage(Lang lang) {
        if (lang == Lang.RU) {
            return ru;
        }
        return uz;
    }

}
