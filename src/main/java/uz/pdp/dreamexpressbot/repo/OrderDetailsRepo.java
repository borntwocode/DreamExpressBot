package uz.pdp.dreamexpressbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.dreamexpressbot.entity.OrderDetails;
import java.util.UUID;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, UUID> {
}