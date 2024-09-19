package uz.pdp.dreamexpressbot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class City {

    private UUID id;
    private String name;

}
