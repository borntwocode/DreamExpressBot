package uz.pdp.dreamexpressbot.company;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "company")
public class CompanyDetails {

    private String phoneNumber;
    private String email;
    private String address;

    private Float latitude;
    private Float longitude;

    public String getFormattedDetails(String text) {
        return text.formatted(phoneNumber, email, address);
    }

}
