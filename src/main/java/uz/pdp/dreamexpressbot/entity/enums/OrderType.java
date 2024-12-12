package uz.pdp.dreamexpressbot.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {

    CONTAINER("\uD83D\uDEA2 Container"),
    AVIA("\u2708\uFE0F Avia");

    private final String text;

    public static OrderType getFromText(String text) {
        if (text.equals(CONTAINER.getText())) {
            return CONTAINER;
        } else if (text.equals(AVIA.getText())) {
            return AVIA;
        } else {
            return null;
        }
    }

}
