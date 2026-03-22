package microsoftAI.KrishiMitra.krishiApp.repository;

import microsoftAI.KrishiMitra.krishiApp.entity.EquipmentBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentBookingRepository extends JpaRepository<EquipmentBooking, Long> {
    // Find all bookings for a specific piece of equipment
    List<EquipmentBooking> findByEquipmentId(Long equipmentId);

    // Find all bookings made by a specific farmer
    List<EquipmentBooking> findByRenterId(Long renterId);
}