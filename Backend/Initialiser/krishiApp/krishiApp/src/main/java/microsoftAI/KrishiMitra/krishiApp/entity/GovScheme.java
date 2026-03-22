package microsoftAI.KrishiMitra.krishiApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gov_schemes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GovScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String schemeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String targetCrop = "ALL"; // "Paddy", "Tomato", or "ALL"

    // Cutoff acreage for eligibility (e.g., small farmers < 5 acres)
    private Double maxLandAcres;

    private Double payoutAmount;
}