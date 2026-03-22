package microsoftAI.KrishiMitra.krishiApp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "crop_diagnoses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropDiagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private String imagePath; // Where it's saved locally

    // The fields coming back from the AI Model
    private String crop;
    private String disease;
    private Double confidence;

    @Column(columnDefinition = "TEXT")
    private String visualSymptoms;

    @Column(columnDefinition = "TEXT")
    private String likelyCause;

    @Column(columnDefinition = "TEXT")
    private String icarTreatment;

    private String treatmentProduct;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime diagnosisDate;
}