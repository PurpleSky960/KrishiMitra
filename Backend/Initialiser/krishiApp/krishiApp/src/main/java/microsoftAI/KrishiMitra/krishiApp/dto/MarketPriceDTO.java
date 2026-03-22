package microsoftAI.KrishiMitra.krishiApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // This adds Getters, Setters, toString, equals, and hashCode automatically
@NoArgsConstructor
@AllArgsConstructor
public class MarketPriceDTO {
    private String cropName;
    private String mandiLocation = "Udupi Mandi";
    private Double pricePerQuintal;
    private Double trendPercentage;
}