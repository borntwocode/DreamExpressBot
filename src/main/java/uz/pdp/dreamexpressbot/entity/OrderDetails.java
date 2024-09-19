package uz.pdp.dreamexpressbot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.dreamexpressbot.entity.enums.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String userFirstName;

    private String userAddress;

    private String userPhoneNumber;

    @OneToOne
    private Photo photo;

}
