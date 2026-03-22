package microsoftAI.KrishiMitra.krishiApp.service;

import microsoftAI.KrishiMitra.krishiApp.dto.AvailableEquipmentDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.BookingConfirmationDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.BookingRequestDTO;

import java.util.List;

public interface EquipmentService {

    // Search dynamically by type (e.g., "Drone", "Tractor")
    List<AvailableEquipmentDTO> searchAvailableEquipment(String equipmentType);

    // The core transactional logic for locking in a rental
    BookingConfirmationDTO bookEquipment(BookingRequestDTO request);
}