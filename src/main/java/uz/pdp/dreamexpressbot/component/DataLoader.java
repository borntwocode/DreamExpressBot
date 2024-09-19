package uz.pdp.dreamexpressbot.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.dreamexpressbot.util.CityUtil;

@Component
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        CityUtil cityUtil = CityUtil.getInstance();

    }

}
