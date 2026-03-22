package microsoftAI.KrishiMitra.krishiApp.repository;

import microsoftAI.KrishiMitra.krishiApp.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // Find available equipment of a specific type
    List<Equipment> findByEquipmentTypeAndIsActiveTrue(String equipmentType);
}