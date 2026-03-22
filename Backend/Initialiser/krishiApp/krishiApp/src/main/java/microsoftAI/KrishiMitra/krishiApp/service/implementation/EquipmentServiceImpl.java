package microsoftAI.KrishiMitra.krishiApp.service.implementation;

import microsoftAI.KrishiMitra.krishiApp.dto.AvailableEquipmentDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.BookingConfirmationDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.BookingRequestDTO;
import microsoftAI.KrishiMitra.krishiApp.entity.Equipment;
import microsoftAI.KrishiMitra.krishiApp.entity.EquipmentBooking;
import microsoftAI.KrishiMitra.krishiApp.entity.User;
import microsoftAI.KrishiMitra.krishiApp.exception.ResourceNotFoundException;
import microsoftAI.KrishiMitra.krishiApp.repository.EquipmentBookingRepository;
import microsoftAI.KrishiMitra.krishiApp.repository.EquipmentRepository;
import microsoftAI.KrishiMitra.krishiApp.repository.UserRepository;
import microsoftAI.KrishiMitra.krishiApp.service.EquipmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentBookingRepository bookingRepository;
    private final UserRepository userRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository,
                                EquipmentBookingRepository bookingRepository,
                                UserRepository userRepository) {
        this.equipmentRepository = equipmentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AvailableEquipmentDTO> searchAvailableEquipment(String equipmentType) {
        List<Equipment> activeEquipment = equipmentRepository.findByEquipmentTypeAndIsActiveTrue(equipmentType);

        // Map the heavy Entities to lightweight DTOs for the frontend
        return activeEquipment.stream().map(eq -> AvailableEquipmentDTO.builder()
                .id(eq.getId())
                .equipmentType(eq.getEquipmentType())
                .modelName(eq.getModelName())
                .ratePerHour(eq.getRatePerHour())
                .location(eq.getLocation())
                .ownerName(eq.getOwner().getFullName())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional // Crucial: Ensures the entire booking succeeds, or it rolls back completely
    public BookingConfirmationDTO bookEquipment(BookingRequestDTO request) {

        // 1. Fetch the Renter and Equipment, throwing our custom 404 if missing
        User renter = userRepository.findById(request.getRenterId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getRenterId()));

        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + request.getEquipmentId()));

        // 2. Check if the owner marked it inactive
        if (!equipment.getIsActive()) {
            throw new IllegalStateException("This equipment is currently not available for rent.");
        }

        // 3. Calculate the cost based on hours rented
        long hours = Duration.between(request.getStartDatetime(), request.getEndDatetime()).toHours();
        if (hours <= 0) throw new IllegalArgumentException("End time must be after start time.");
        double totalCost = hours * equipment.getRatePerHour();

        // 4. Create the Booking Ledger Entry
        EquipmentBooking booking = EquipmentBooking.builder()
                .equipment(equipment)
                .renter(renter)
                .startDatetime(request.getStartDatetime())
                .endDatetime(request.getEndDatetime())
                .totalCost(totalCost)
                .status("CONFIRMED")
                .build();

        // 5. Save the booking.
        // * MAGIC INCOMING: If someone else modified this equipment while this method was running,
        // * Hibernate's Optimistic Locking will throw an exception right here, and our GlobalExceptionHandler
        // * will catch it and return a 409 Conflict JSON to the frontend!
        bookingRepository.save(booking);

        return BookingConfirmationDTO.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .totalCost(totalCost)
                .message("Successfully booked " + equipment.getModelName() + " for " + hours + " hours.")
                .build();
    }
}