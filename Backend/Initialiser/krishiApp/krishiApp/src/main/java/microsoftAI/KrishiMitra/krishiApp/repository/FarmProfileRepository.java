package microsoftAI.KrishiMitra.krishiApp.repository;

import microsoftAI.KrishiMitra.krishiApp.entity.FarmProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmProfileRepository extends JpaRepository<FarmProfile, Long> {
    // Find all farms owned by a specific user ID
    List<FarmProfile> findByUserId(Long userId);
}