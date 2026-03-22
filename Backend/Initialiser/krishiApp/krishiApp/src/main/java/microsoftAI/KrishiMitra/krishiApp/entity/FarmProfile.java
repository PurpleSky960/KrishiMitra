package microsoftAI.KrishiMitra.krishiApp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "farm_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    // Matches the UI Farm Details Card exactly
    @Column(nullable = false)
    private String cropType;

    @Column(nullable = false)
    private Double farmSizeAcres;

    @Column(nullable = false)
    private String soilType;

    // ADDED: The missing field from the UI
    @Column(nullable = false)
    private String irrigationType;

    // Kept this so the AI knows exactly where this specific plot is
    @Column(nullable = false)
    private String farmLocation;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}