package uz.pdp.dreamexpressbot.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Lang {

    UZ("\uD83C\uDDFA\uD83C\uDDFF UZ"),
    RU("\uD83C\uDDF7\uD83C\uDDFA RU");

    private final String text;

    public static Lang getLang(String text) {
        if (text.equals(Lang.RU.getText())) {
            return Lang.RU;
        } else if (text.equals(Lang.UZ.getText())) {
            return Lang.UZ;
        }
        return null;
    }

}
