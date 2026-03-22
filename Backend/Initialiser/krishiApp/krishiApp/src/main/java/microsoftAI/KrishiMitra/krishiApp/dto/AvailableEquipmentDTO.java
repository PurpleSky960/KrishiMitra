package microsoftAI.KrishiMitra.krishiApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableEquipmentDTO {
    private Long id;
    private String equipmentType; // Can be "Tractor", "Harvester", "Fertilizer Spreader", etc.
    private String modelName;
    private Double ratePerHour;
    private String location;
    private String ownerName;
}