package uz.pdp.dreamexpressbot.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uz.pdp.dreamexpressbot.entity.TelegramUser;

@Getter
@AllArgsConstructor
public enum ServiceType {
//
//    SEND_FROM_HOME("\uD83C\uDFE0 Uydan olib ketish", "\uD83C\uDFE0 Забрать из дома"),
//    SEND_TO_OFFICE("\uD83D\uDCC5 Officega yuborib berish", "\uD83D\uDCC5 Отправить в офис"),
    TAXI("Taxi buyurtma berish "," Заказать такси" ),
    MAIL("Pochta bor", " Есть почтовое отделение "),
    YOL_YOLAKAY("Yo'l yolakay", " по пути ");

    private final String uz;
    private final String ru;

    public static ServiceType getFromText(String text) {
        if (text.equals(TAXI.getRu()) || text.equals(TAXI.getUz())) {
            return TAXI;
        } else if (text.equals(MAIL.getRu()) || text.equals(MAIL.getUz())) {
            return MAIL;
        } else if (text.equals(YOL_YOLAKAY.getRu()) || text.equals(YOL_YOLAKAY.getUz())) {
            return YOL_YOLAKAY;
        }
        return TAXI;
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
