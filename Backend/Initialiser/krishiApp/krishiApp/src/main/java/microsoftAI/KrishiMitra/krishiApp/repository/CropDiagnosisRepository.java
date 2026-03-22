package microsoftAI.KrishiMitra.krishiApp.repository;

import microsoftAI.KrishiMitra.krishiApp.entity.CropDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropDiagnosisRepository extends JpaRepository<CropDiagnosis, Long> {
    List<CropDiagnosis> findByUserIdOrderByDiagnosisDateDesc(Long userId);
}