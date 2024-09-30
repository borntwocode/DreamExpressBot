package uz.pdp.dreamexpressbot.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import uz.pdp.dreamexpressbot.entity.enums.Lang;
import uz.pdp.dreamexpressbot.entity.enums.OrderType;
import uz.pdp.dreamexpressbot.entity.enums.ServiceType;
import uz.pdp.dreamexpressbot.entity.enums.TgState;
import uz.pdp.dreamexpressbot.repo.TelegramUserRepo;
import uz.pdp.dreamexpressbot.util.CityUtil;
import uz.pdp.dreamexpressbot.util.FaqUtil;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    private final TelegramUserRepo telegramUserRepo;

    @Override
    public void run(String... args) throws Exception {

        CityUtil cityUtil = CityUtil.getInstance();
        FaqUtil faqUtil = FaqUtil.getInstance();

        if(ddl.equals("create")){
//            test();
        }

    }

    private void test() {
        TelegramUser user = new TelegramUser();
        user.setLang(Lang.UZ);
        user.setFirstName("Asadbek");
        user.setPhoneNumber("+998930649246");
        user.setChatId(2083435737L);
        user.setUserId(2083435737L);
        user.setState(TgState.START);
        telegramUserRepo.save(user);
    }

}
