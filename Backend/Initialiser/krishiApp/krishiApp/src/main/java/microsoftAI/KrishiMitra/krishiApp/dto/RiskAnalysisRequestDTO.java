package microsoftAI.KrishiMitra.krishiApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAnalysisRequestDTO {
    private Long userId;
    private List<CoordinateDTO> coordinates;
}