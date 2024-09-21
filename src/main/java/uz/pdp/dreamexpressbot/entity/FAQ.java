package uz.pdp.dreamexpressbot.entity;

import lombok.*;
import uz.pdp.dreamexpressbot.entity.enums.Lang;
import java.util.UUID;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQ {

    private UUID id;

    private String questionUz;

    private String questionRu;

    private String answerUz;

    private String answerRu;

    public String getQuestion(TelegramUser user) {
        if(user.getLang() == Lang.RU){
            return questionRu;
        }
        return questionUz;
    }

    public String getAnswer(TelegramUser user) {
        if(user.getLang() == Lang.RU){
            return answerRu;
        }
        return answerUz;
    }

}
