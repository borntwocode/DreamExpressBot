package uz.pdp.dreamexpressbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.dreamexpressbot.entity.enums.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long orderNumber;

    @ManyToOne
    private TelegramUser user;

    @OneToOne(cascade = CascadeType.PERSIST)
    private OrderDetails orderDetails;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Integer groupMessageId;

    @Enumerated(EnumType.STRING)
    private Lang lang;

    @CreationTimestamp
    private LocalDateTime orderDateTime;

}
