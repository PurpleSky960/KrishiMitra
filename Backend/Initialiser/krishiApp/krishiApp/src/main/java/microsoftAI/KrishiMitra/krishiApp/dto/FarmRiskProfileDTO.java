package microsoftAI.KrishiMitra.krishiApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmRiskProfileDTO {
    private int overallRiskScore;
    private int cropScore;
    private int weatherScore;
    private int marketScore;
    private int pestScore;
    private int soilScore;
    private List<ThreatDTO> immediateThreats;
    private List<ActionDTO> protectiveActions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThreatDTO {
        private String title;
        private String severity; // "HIGH", "MEDIUM", "LOW"
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionDTO {
        private String actionText;
    }
}