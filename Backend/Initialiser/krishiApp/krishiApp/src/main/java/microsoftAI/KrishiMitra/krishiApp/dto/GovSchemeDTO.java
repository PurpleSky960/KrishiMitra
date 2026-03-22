package microsoftAI.KrishiMitra.krishiApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GovSchemeDTO {
    private Long id;
    private String schemeName;
    private String description;
    private Double payoutAmount;
    private String matchReason; // Tells the UI exactly WHY they are eligible (e.g., "Matched for Paddy < 5 acres")
}