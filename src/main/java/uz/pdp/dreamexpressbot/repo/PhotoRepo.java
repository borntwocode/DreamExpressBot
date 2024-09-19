package uz.pdp.dreamexpressbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.dreamexpressbot.entity.Photo;
import java.util.UUID;

public interface PhotoRepo extends JpaRepository<Photo, UUID> {

    @Query(value = """
            select p.content from photo p
            join order_details od on p.id = od.photo_id
            join orders o on od.id = o.order_details_id
            where o.id = :orderId
            """, nativeQuery = true)
    byte[] findPhotoContentByOrderId(UUID orderId);

}
