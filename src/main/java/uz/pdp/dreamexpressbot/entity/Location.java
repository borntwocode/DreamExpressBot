package uz.pdp.dreamexpressbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Location extends com.pengrad.telegrambot.model.Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Float latitude;

    private Float longitude;

}
