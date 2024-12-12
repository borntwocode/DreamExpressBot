package uz.pdp.dreamexpressbot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.entity.City;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Service
public class CityUtil {

    private static final CityUtil INSTANCE = new CityUtil();
    private final List<City> cities;

    private CityUtil() {
        this.cities = Collections.unmodifiableList(initializeRegions());
    }

    public static CityUtil getInstance() {
        return INSTANCE;
    }

    private List<City> initializeRegions() {
        List<City> tempCities = new ArrayList<>();
        tempCities.add(new City(UUID.randomUUID(), "Seoul"));
        tempCities.add(new City(UUID.randomUUID(), "Sejong"));
        tempCities.add(new City(UUID.randomUUID(), "Busan"));
        tempCities.add(new City(UUID.randomUUID(), "Incheon"));
        tempCities.add(new City(UUID.randomUUID(), "Daegu"));
        tempCities.add(new City(UUID.randomUUID(), "Daejeon"));
        tempCities.add(new City(UUID.randomUUID(), "Gwangju"));
        tempCities.add(new City(UUID.randomUUID(), "Ulsan"));
        tempCities.add(new City(UUID.randomUUID(), "Gyeonggi-do"));
        tempCities.add(new City(UUID.randomUUID(), "Gangwon-do"));
        tempCities.add(new City(UUID.randomUUID(), "Chungcheongbuk-do"));
        tempCities.add(new City(UUID.randomUUID(), "Chungcheongnam-do"));
        tempCities.add(new City(UUID.randomUUID(), "Jeollabuk-do"));
        tempCities.add(new City(UUID.randomUUID(), "Jeollanam-do"));
        tempCities.add(new City(UUID.randomUUID(), "Gyeongsangbuk-do"));
        tempCities.add(new City(UUID.randomUUID(), "Gyeongsangnam-do"));
        tempCities.add(new City(UUID.randomUUID(), "Jeju-do"));
        return tempCities;
    }

    public InlineKeyboardMarkup getCityNameMarkup() {
        var keyboardMarkup = new InlineKeyboardMarkup();
        for (int i = 0; i < 10; i+=2) {
            String cityName1 = cities.get(i).getName();
            String cityName2 = cities.get(i+1).getName();
            keyboardMarkup.addRow(
                    new InlineKeyboardButton(cityName1).callbackData("CITY_" + cityName1),
                    new InlineKeyboardButton(cityName2).callbackData("CITY_" + cityName2)
            );
        }
        for (int i = 11; i < 17; i++) {
            String cityName = cities.get(i).getName();
            keyboardMarkup.addRow(
                    new InlineKeyboardButton(cityName).callbackData("CITY_" + cityName)
            );
        }
        return keyboardMarkup;
    }

    public boolean contains(String cityName) {
        return cities.stream().anyMatch(city -> city.getName().equals(cityName));
    }

}
