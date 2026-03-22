package microsoftAI.KrishiMitra.krishiApp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The farmer who owns this equipment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @ToString.Exclude
    private User owner;

    @Column(nullable = false)
    private String equipmentType; // e.g., "Tractor", "Harvester"

    @Column(nullable = false)
    private String modelName; // e.g., "Mahindra Tractor"

    @Column(nullable = false)
    private Double ratePerHour;

    @Column(nullable = false)
    private String location; // Physical location of the tool

    @Column(nullable = false)
    private Boolean isActive = true; // Owner can toggle this off if it's broken or unavailable

    // Bidirectional link: A piece of equipment has a history of bookings
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<EquipmentBooking> bookings = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}