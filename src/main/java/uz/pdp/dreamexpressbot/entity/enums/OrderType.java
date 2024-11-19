package uz.pdp.dreamexpressbot.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {


    TAXI("Taxi buyurtma berish"),
    MAIL("Pochta bor"),
    YOL_YOLAKAY("Yo'l yolakay");

    private final String text;

    public static OrderType getFromText(String text) {
        if (text.equals(TAXI.getText())) {
            return TAXI;
        } else if (text.equals(MAIL.getText())) {
            return MAIL;
        } else if (text.equals(YOL_YOLAKAY.getText()))
            return YOL_YOLAKAY;
        else {
            return TAXI;
        }
    }

}
