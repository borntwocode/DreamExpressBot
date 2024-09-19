package uz.pdp.dreamexpressbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.dreamexpressbot.entity.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID> {

    @Query(value = "select * from orders o order by order_number desc limit 1", nativeQuery = true)
    Optional<Order> findLastOrder();

    @Query(value = """
                    select * from orders o where status = 'PENDING' and user_id = :userId limit 1
                    """, nativeQuery = true)
    Optional<Order> findPendingOrderByUserId(UUID userId);

    @Query(value = """
                select * from orders o where user_id = :userId order by order_date_time desc limit 5
                """, nativeQuery = true)
    List<Order> getLastFiveOrdersByUserId(UUID userId);

}