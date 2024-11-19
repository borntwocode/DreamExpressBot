package uz.pdp.dreamexpressbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.dreamexpressbot.entity.Region;

import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID> {

}