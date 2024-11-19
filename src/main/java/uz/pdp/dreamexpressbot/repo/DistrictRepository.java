package uz.pdp.dreamexpressbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.dreamexpressbot.entity.District;

import java.util.UUID;

public interface DistrictRepository extends JpaRepository<District, UUID> {
}