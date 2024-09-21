package uz.pdp.dreamexpressbot.util;

import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.entity.FAQ;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.BotMessages;
import java.util.*;

@Service
public class FaqUtil {

    private static final FaqUtil INSTANCE = new FaqUtil();
    private final List<FAQ> faqList;

    private FaqUtil() {
        this.faqList = Collections.unmodifiableList(initializeFAQs());
    }

    public static FaqUtil getInstance() {
        return INSTANCE;
    }

    private List<FAQ> initializeFAQs() {
        List<FAQ> tempFAQs = new ArrayList<>();

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("Manzilingiz qayerda joylashgan?")
                .questionRu("Где находится ваш адрес?")
                .answerUz("김해시 분성로335번길5 드림 익스프레스 010-2985-8641")
                .answerRu("Город Кимхэ, улица Бунсонро 335번길 5, Dream Express, 010-2985-8641")
                .build());

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("Sizlarda aviakargo ham bormi?")
                .questionRu("У вас есть авиаперевозки?")
                .answerUz("Uzr hozirda faqat konteyner kargo xizmati bor edi.")
                .answerRu("Извините, на данный момент доступны только услуги контейнерных перевозок.")
                .build());

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("Qancha muddatda yetib boradi yuklar O'zbekistonga?")
                .questionRu("За какое время грузы доходят до Узбекистана?")
                .answerUz("Konteyner yo'lga chiqqandan so’ng 30-45 ish kunida yetib boradi.")
                .answerRu("После отправки контейнера груз прибывает в течение 30-45 рабочих дней.")
                .build());

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("O'zbekistondan-Koreyaga yuk olib keltirsam bo'ladimi?")
                .questionRu("Можно ли доставить груз из Узбекистана в Корею?")
                .answerUz("Hozirda xizmatimiz Koreyadan-O'zbekistonga edi. (Tez orada bu xizmatni ham yo'lga qo'yamiz)")
                .answerRu("На данный момент наша услуга доступна только из Кореи в Узбекистан. (Скоро мы запустим и обратное направление)")
                .build());

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("O'zbekistondan boshqa davlatlatlarga ham yuk yuborsak bo'ladimi?")
                .questionRu("Можно ли отправить груз в другие страны из Узбекистана?")
                .answerUz("Hozirda faqat O'zbekistonga jo'natish xizmati bor(tez orada MDH davlatlari bo'ylab ham jo'natsak bo'ladi).")
                .answerRu("На данный момент доступна только отправка в Узбекистан (скоро будет доступна отправка в страны СНГ).")
                .build());

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("Telefonni o'zini yuborsam bo'ladimi?")
                .questionRu("Можно отправить только телефон?")
                .answerUz("Telefonni o'zini yuborib berish xizmatimiz yo'qku lekin yukingizni orasiga bitta joylasangiz bo'ladi (faqat narxi qimmat bo'lmagan).")
                .answerRu("Отдельная отправка телефона недоступна, но можно положить его среди других вещей (желательно, чтобы это был недорогой телефон).")
                .build());

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("O'zbekistondagi hisob raqamingizni to'lovni amalga oshirsam bo'ladimi?")
                .questionRu("Можно ли оплатить на ваш счет в Узбекистане?")
                .answerUz("Ha albatta agar sizda Koreydagai hisob raqamimizga to'lovni bajara olmasangiz quyidagi hisob raqamga amalga oshirsangiz bo'ladi,\n5614 6812 0714 8044\nTeshaboev Fakhriddin")
                .answerRu("Да, конечно, если вы не можете оплатить на наш корейский счет, можете сделать это на счет в Узбекистане:\n5614 6812 0714 8044\nТешабоев Фахриддин")
                .build());

        tempFAQs.add(FAQ.builder()
                .id(UUID.randomUUID())
                .questionUz("O'zbekistonda ham ofisilar bormi, qayerga murojaat qilsam bo'ladi?")
                .questionRu("Есть ли у вас офисы в Узбекистане и куда можно обратиться?")
                .answerUz("Ha albatta +998712070809 raqamiga aloqaga chiqsangiz bo'ladi(Eslatma! Yuqoridagi raqam faqat biz ogohlantirsakgina javob berishlari mumkin).")
                .answerRu("Да, конечно, свяжитесь по номеру +998712070809 (Примечание: этот номер отвечает только после нашего уведомления).")
                .build());

        return tempFAQs;
    }

    public String[][] getQuestionsAsMatrix(TelegramUser user) {
        int maxLengthPerRow = 60;
        List<String[]> tempMatrix = new ArrayList<>();
        for (FAQ faq : faqList) {
            String question = faq.getQuestion(user);
            if (question.length() <= maxLengthPerRow / 2) {
                if (!tempMatrix.isEmpty() && tempMatrix.get(tempMatrix.size() - 1).length == 1) {
                    String[] lastRow = tempMatrix.remove(tempMatrix.size() - 1);
                    tempMatrix.add(new String[]{lastRow[0], question});
                } else {
                    tempMatrix.add(new String[]{question});
                }
            } else {
                tempMatrix.add(new String[]{question});
            }
        }
        String[][] matrix = new String[tempMatrix.size() + 1][];
        tempMatrix.toArray(matrix);
        String backMessage = BotMessages.BACK.getMessage(user);
        matrix[tempMatrix.size()] = new String[]{backMessage};
        return matrix;
    }

    public boolean containsQuestion(TelegramUser user, String text) {
        return faqList.stream().anyMatch(faq -> text != null && text.equals(faq.getQuestion(user)));
    }

    public String getAnswer(TelegramUser user, String question) {
        return faqList.stream()
                .filter(faq -> faq.getQuestion(user).equals(question))
                .map(faq -> faq.getAnswer(user))
                .findFirst()
                .orElse("Answer not found");
    }

}
