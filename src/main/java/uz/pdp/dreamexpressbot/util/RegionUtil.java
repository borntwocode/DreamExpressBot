package uz.pdp.dreamexpressbot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.entity.Region;


import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Service
public class RegionUtil {


    private static final RegionUtil INSTANCE = new RegionUtil();
    private final List<Region> cities;


    private RegionUtil() {
        this.cities = Collections.unmodifiableList(initializeRegions());
    }

    public static RegionUtil getInstance() {
        return INSTANCE;
    }

    private List<Region> initializeRegions() {
        List<Region> tempCities = new ArrayList<>();
        tempCities.add(Region.builder().name("Toshkent").build());
        tempCities.add(Region.builder().name("Andijon").build());
        tempCities.add(Region.builder().name("Namangan").build());
        tempCities.add(Region.builder().name("Farg'ona").build());
        tempCities.add(Region.builder().name("Samarqand").build());
        tempCities.add(Region.builder().name("Surxandaryo").build());
        tempCities.add(Region.builder().name("Qashqadaryo").build());
        tempCities.add(Region.builder().name("Sirdaryo").build());
        tempCities.add(Region.builder().name("Jizzax").build());
        tempCities.add(Region.builder().name("Buxoro").build());
        tempCities.add(Region.builder().name("Xorazm").build());
        tempCities.add(Region.builder().name("Navoiy").build());
        tempCities.add(Region.builder().name("Qoraqalpoqiston").build());
        return tempCities;
    }




    public InlineKeyboardMarkup getRegionNameMarkup() {
        var keyboardMarkup = new InlineKeyboardMarkup();
        for (int i = 0; i < 10; i+=2) {
            String cityName1 = cities.get(i).getName();
            String cityName2 = cities.get(i+1).getName();
            keyboardMarkup.addRow(
                    new InlineKeyboardButton(cityName1).callbackData("CITY_" + cityName1),
                    new InlineKeyboardButton(cityName2).callbackData("CITY_" + cityName2)
            );
        }
        return keyboardMarkup;
    }

    public boolean contains(String cityName) {
        return cities.stream().anyMatch(city -> city.getName().equals(cityName));
    }

}
