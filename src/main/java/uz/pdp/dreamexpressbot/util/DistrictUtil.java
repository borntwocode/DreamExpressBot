package uz.pdp.dreamexpressbot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.pdp.dreamexpressbot.entity.District;
import uz.pdp.dreamexpressbot.entity.Region;
import uz.pdp.dreamexpressbot.repo.DistrictRepository;
import uz.pdp.dreamexpressbot.repo.RegionRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class DistrictUtil {
    private static DistrictUtil INSTANCE;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private DistrictRepository districtRepository;

    private List<Region> regions;

    private DistrictUtil() {
        this.regions = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        if (regionRepository.count() == 0) {
            initializeRegionsAndDistricts();
        }
        regions = regionRepository.findAll();
        INSTANCE = this;
    }

    public static DistrictUtil getInstance() {
        return INSTANCE;
    }

    private void initializeRegionsAndDistricts() {
        List<Region> tempRegions = new ArrayList<>();

        Region toshkent = new Region(UUID.randomUUID(), "Toshkent", new ArrayList<>());
        Region andijon = new Region(UUID.randomUUID(), "Andijon", new ArrayList<>());

        // Add districts for Toshkent
        List<District> toshkentDistricts = List.of(
                new District(UUID.randomUUID(), "Chilonzor", toshkent),
                new District(UUID.randomUUID(), "Yunusobod", toshkent),
                new District(UUID.randomUUID(), "Mirobod", toshkent)
        );

        // Add districts for Andijon
        List<District> andijonDistricts = List.of(
                new District(UUID.randomUUID(), "Andijon shahri", andijon),
                new District(UUID.randomUUID(), "Asaka", andijon),
                new District(UUID.randomUUID(), "Xo'jaobod", andijon)
        );

        toshkent.setDistricts(toshkentDistricts);
        andijon.setDistricts(andijonDistricts);

        tempRegions.add(toshkent);
        tempRegions.add(andijon);

        regionRepository.saveAll(tempRegions);
    }

    public List<Region> getRegions() {
        return Collections.unmodifiableList(regions);
    }

    public InlineKeyboardMarkup getRegionMarkup() {
        var keyboardMarkup = new InlineKeyboardMarkup();
        for (Region region : regions) {
            String regionName = region.getName();
            keyboardMarkup.addRow(
                    new InlineKeyboardButton(regionName).callbackData("REGION_" + regionName)
            );
        }
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getDistrictMarkup(String regionName) {
        Region region = regions.stream()
                .filter(r -> r.getName().equals(regionName))
                .findFirst()
                .orElse(null);

        if (region == null) {
            return null; // or return an empty keyboard
        }

        var keyboardMarkup = new InlineKeyboardMarkup();
        for (District district : region.getDistricts()) {
            String districtName = district.getName();
            keyboardMarkup.addRow(
                    new InlineKeyboardButton(districtName).callbackData("DISTRICT_" + districtName)
            );
        }
        return keyboardMarkup;
    }
}
