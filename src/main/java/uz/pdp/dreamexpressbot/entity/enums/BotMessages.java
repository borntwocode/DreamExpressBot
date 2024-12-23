package uz.pdp.dreamexpressbot.entity.enums;

import lombok.Getter;
import uz.pdp.dreamexpressbot.entity.TelegramUser;

@Getter
public enum BotMessages {

    ENTER_YOUR_PHONE_NUMBER("\uD83D\uDCF1 Hurmatli mijoz, telefon raqamingizni kiriting:", "\uD83D\uDCF1 Уважаемый клиент, введите номер телефона:"),
    REGISTERED("\uD83D\uDC4C Ro'yxatdan muvaffaqiyatli o'tdingiz!", "\uD83D\uDC4C Вы успешно зарегистрировались!"),
    CHOOSE("\uD83D\uDCDC Menyudan birini tanlang:", "\uD83D\uDCDC Выберите один из пунктов меню:"),
    ENTER_YOUR_FIRST_NAME("\uD83D\uDC64 Ismingizni kiriting:", "\uD83D\uDC64 Введите ваше имя:"),
    CHOOSE_LANGUAGE("\uD83C\uDF0D Tilni tanlang | Выберите язык:"),

    MY_ORDERS("\uD83D\uDCE6 Mening buyurtmalarim", "\uD83D\uDCE6 Мои заказы"),
    PROFILE("\uD83D\uDCBC Profil", "\uD83D\uDCBC Профиль"),
    ABOUT_US("\uD83D\uDCBC Biz haqimizda", "\uD83D\uDCCB О нас"),
    FAQ("❓ Ko'p beriladigan savollar", "❓ Часто задаваемые вопросы"),

    CHOOSE_FROM_MENU("\uD83D\uDCCC Menyudan tanlang:", "\uD83D\uDCCC Выберите из меню:"),
    AVIA_NOT_SUPPORTED("✈️ Hurmatli mijoz, avia xizmati hozircha mavjud emas.", "✈️ Уважаемый клиент, авиасервис пока недоступен."),

    SERVICE_MENU_MESSAGE("""
       ❗ Agar yukingiz og'irligi:
       ▶️ 15 - 25 kg oralig'ida bo'lsa, faqatgina 🏢 ofisga yuborib berish xizmatidan foydalana olasiz.
       ▶️ 25 kg va undan ko'p bo'lsa, 🚚 uydan olib ketish va 🏢 ofisga yuborib berish xizmatlarimizdan  foydalana olasiz.
       """,
            """
            ❗ Если вес вашего груза:
            ▶️ Если он составляет от 15 до 25 кг, вы можете воспользоваться только услугой 🏢 отправки в офис.
            ▶️ Если вес 25 кг и более, вы можете воспользоваться услугами 🚚 забора груза с дома и 🏢 отправки в офис.
            """),

    INVALID_FIRST_NAME(
            "⚠ Ism noto‘g‘ri formatda. Ismingizni katta harf bilan boshlang va kamida 3 ta harf kiriting. Raqam va belgilar ishlatilmasin.",
            "⚠ Неправильный формат имени. Имя должно начинаться с заглавной буквы и содержать не менее 3 букв. Не используйте цифры или символы."
    ),
    INVALID_PHONE_NUMBER(
            "⚠ Telefon raqami noto‘g‘ri formatda. Faqat O‘zbek yoki Koreya raqamlarini kiriting.",
            "⚠ Неправильный формат номера телефона. Введите только узбекские или корейские номера."
    ),

    SELECT_FROM_FAQ("🤔 Savollardan birini tanlang.", "🤔 Выберите один из вопросов."),

    INVALID_PHOTO("⚠ Notog'ri format.", "⚠ Неправильный формат."),

    CLICK_BACK_BUTTON("⬅️ Orqaga tugmasini bosing", "⬅️ Назад нажмите"),
    BACK("⬅️ Orqaga", "⬅️ Назад"),
    SUBMIT("✅ Buyurtmani tasdiqlash", "✅ Подтвердить заказ"),

    CHOOSE_CITY("\uD83D\uDCCD Siz turgan shaharni tanlang:", "\uD83D\uDCCD Выберите город, в котором вы находитесь:"),
    CHOOSE_FROM_CITY("\uD83D\uDDFE Shaharlardan birini tanlang:", "\uD83D\uDDFE Выберите один из городов:"),

    YOU_HAVE_ORDERED("""
        \uD83D\uDCE6 Hurmatli mijoz, siz buyurtma bergansiz. Iltimos, adminlar javobini kuting. "Mening buyurtmalarim" bo'limida buyurtmalaringizni kuzatishingiz mumkin.
        """, """
        \uD83D\uDCE6 Уважаемый клиент, вы уже сделали заказ. Пожалуйста, ожидайте ответ администраторов. Вы можете отслеживать свои заказы в разделе "Мои заказы".
        """),

    ORDER_CREATED("""
        \uD83D\uDCE6 Hurmatli mijoz, sizning %d raqamli buyurtmangiz qabul qilindi. Adminlar tez orada aloqaga chiqishadi.
        """, """
        \uD83D\uDCE6 Уважаемый клиент, ваш заказ под номером %d успешно принят. Администраторы скоро свяжутся с вами.
        """),

    YOU_HAVE_NOT_ORDER("⚠ Siz hech qanday buyurtma qilmagansiz.", "⚠ Вы еще не сделали заказ."),

    CHANGE_NAME("\uD83D\uDD8A Ismni o'zgartirish", "\uD83D\uDD8A Изменить имя"),
    CHANGE_NUMBER("\uD83D\uDDE3 Raqamni o'zgartirish", "\uD83D\uDDE3 Изменить номер"),
    CHANGE_LANG("\uD83C\uDF0D Tilni o'zgartirish", "\uD83C\uDF0D Изменить язык"),

    SEND_PHOTO("Yukingizni rasmini jo'nating", "otpravlyayte foto posilki"),

    SUCCESSFULLY_CHANGED("✅ Muvaffaqiyatli o'zgartirildi", "✅ Успешно изменено"),

    SELECT_FAQ("\uD83D\uDCAC Iltimos, savollardan birini tanlang", "\uD83D\uDCAC Пожалуйста, выберите один из вопросов"),

    RAW_HOME_ORDER_DETAILS("""
        📦 Buyurtma turi: %s
        ⚙️ Servis turi: %s
        ⚖️ Yuk og'irligi: %s
        
        👤 Buyurtmachi ma'lumotlari:
        📛 Ismi: %s
        🏡 Manzili: %s
        📞 Telefon raqami: %s
        """,
            """
            📦 Тип заказа: %s
            ⚙️ Тип сервиса: %s
            ⚖️ вес груза: %s
            
            👤 Информация о заказчике:
            📛 Имя: %s
            🏡 Адрес: %s
            📞 Номер телефона: %s
            """),

    RAW_OFFICE_ORDER_DETAILS("""
        📦 Buyurtma turi: %s
        ⚙️ Servis turi: %s
        ⚖️ Yuk og'irligi: %s
        
        👤 Buyurtmachi ma'lumotlari:
        📛 Ismi: %s
        📞 Telefon raqami: %s
        """,
            """
            📦 Тип заказа: %s
            ⚙️ Тип сервиса: %s
            ⚖️ вес груза: %s
            
            👤 Информация о заказчике:
            📛 Имя: %s
            📞 Номер телефона: %s
            """),


    PROFILE_MESSAGE("""
                        \uD83D\uDC64 Ism: %s
                        \uD83D\uDCF1 Raqam: %s
                        \uD83C\uDF0D Til: %s
                        """,
            """
                \uD83D\uDC64 Имя: %s
                \uD83D\uDCF1 Номер: %s
                \uD83C\uDF0D Язык: %s
                """),

    FOLLOW_CHANNEL("Botdan foydalanish uchun kanalga obuna bo'ling \uD83D\uDCE2"),

    FOLLOW("📲 A'zo bo'lish", "\uD83D\uDCF2 Подписаться"),

    CHECK("🔍 Tekshirish", "🔍 Проверить"),

    NOT_FOLLOWED("❗ Kerakli kanalga obuna bo'lmagansiz."),

    HOME_ORDER_DETAILS("""
        📦 Buyurtma ma'lumotlari:
        🔢 Raqami: %s
        📦 Turi: %s
        ⚙️ Servis turi: %s
        ⚖️ Yuk og'irligi: %s
        📊 Holati: %s
        📅 Sanasi: %s
        ⏱️ Vaqti: %s
        
        👤 Buyurtmachi ma'lumotlari:
        🧑‍ Ismi: %s
        🗺️ Manzili: %s
        📱 Telefon raqami: %s
        """,
            """
            📦 Информация о заказе:
            🔢 Номер: %s
            📦 Тип: %s
            ⚙️ Тип сервиса: %s
            ⚖️ вес груза: %s
            📊 Статус: %s
            📅 Дата: %s
            ⏱️ Время: %s
            
            👤 Информация о заказчике:
            🧑‍ Имя: %s
            🗺️ Адрес: %s
            📱 Номер телефона: %s
            """),

    OFFICE_ORDER_DETAILS("""
        📦 Buyurtma ma'lumotlari:
        🔢 Raqami: %s
        📦 Turi: %s
        ⚙️ Servis turi: %s
        ⚖️ Yuk og'irligi: %s
        📊 Holati: %s
        📅 Sanasi: %s
        ⏱️ Vaqti: %s
        
        👤 Buyurtmachi ma'lumotlari:
        🧑‍ Ismi: %s
        📱 Telefon raqami: %s
        """,
            """
            📦 Информация о заказе:
            🔢 Номер: %s
            📦 Тип: %s
            ⚙️ Тип сервиса: %s
            ⚖️ вес груза: %s
            📊 Статус: %s
            📅 Дата: %s
            ⏱️ Время: %s
            
            👤 Информация о заказчике:
            🧑‍ Имя: %s
            📱 Номер телефона: %s
            """),

    COMPANY_DETAILS("""
        Kompaniya Ma'lumotlari:
        📞 Telefon: %s
        ✉️ Email   : %s
        🗺️ Manzil  : %s
        """, """
        Информация о компании:
        📞 Телефон: %s
        ✉️ Электронная почта: %s
        🗺️ Адрес  : %s
        """),

    SEND_LOCATION("Iltimos, joylashuvingizni yuboring 😊", "Пожалуйста, отправьте своё местоположение 📍"),

    SHARE_LOCATION("Joylashuvni ulashish 📍", "Отправить местоположение 📍"),
    LOAD_WEIGHT("Iltimos yukingingiz og'irligini kiriting(taxminiy bo'lsa ham) ⚖\uFE0F", "Пожалуйста, укажите вес вашего груза (даже приблизительный) ⚖\uFE0F " ),
    CONFIRM("Siz bizning xizmatimizdan foydalanishingiz mumkin ✅ ", "Вы можете воспользоваться нашим сервисом ✅"),
    REJECT_WEIGHT("Sizning yukingiz kam‼️ va buni siz officega yuborishingiz mumkin ✅", "у вас меньше багажа, и вы можете отправить его в офис‼️" ),
    INVALID_LOAD_WEIGHT("Iltimos, faqat haqiqiy raqamlarni kiriting.", "«Пожалуйста, вводите только допустимые числа».");


    private String uz;
    private String ru;
    private String text;

    BotMessages(String uz, String ru) {
        this.uz = uz;
        this.ru = ru;
    }

    BotMessages(String text) {
        this.text = text;
    }

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
