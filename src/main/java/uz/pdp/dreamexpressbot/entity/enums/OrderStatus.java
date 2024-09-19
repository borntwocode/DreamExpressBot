package uz.pdp.dreamexpressbot.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uz.pdp.dreamexpressbot.entity.TelegramUser;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    PENDING("⏳ Kutilmoqda", "⏳ В ожидании"),
    FAILED("❌ Muvaffaqiyatsiz", "❌ Неудачный"),
    COMPLETED("✅ Bajarildi", "✅ Завершен");

    private final String uz;
    private final String ru;

    public String getMessage(TelegramUser user) {
        String languageCode = user.getLang().getText();
        if (languageCode != null && languageCode.equalsIgnoreCase(Lang.RU.getText())) {
            return ru;
        }
        return uz;
    }

    public String getMessage(Lang lang) {
        if (lang == Lang.RU) {
            return ru;
        }
        return uz;
    }

}
