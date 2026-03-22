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
public class DashboardSummaryDTO {
    private String farmerName;
    private String location;
    private int eligibleSchemesCount;
    private List<MarketPriceDTO> topMarketPrices;

    // We'll mock this for now until we build the actual AI Risk engine
    private String urgentAction;
}