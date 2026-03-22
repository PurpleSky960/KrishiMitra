package microsoftAI.KrishiMitra.krishiApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "market_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cropName;

    @Column(nullable = false)
    private String mandiLocation;

    @Column(nullable = false)
    private Double pricePerQuintal;

    @Column(nullable = false)
    private LocalDate recordedDate;

    // Positive or negative percentage compared to yesterday
    private Double trendPercentage;
}