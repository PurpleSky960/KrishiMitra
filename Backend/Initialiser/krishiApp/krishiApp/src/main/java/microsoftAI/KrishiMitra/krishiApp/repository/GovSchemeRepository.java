package microsoftAI.KrishiMitra.krishiApp.repository;

import microsoftAI.KrishiMitra.krishiApp.entity.GovScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GovSchemeRepository extends JpaRepository<GovScheme, Long> {
    // We don't need custom SQL here; JpaRepository's findAll() is enough
    // since we'll do the complex matching in the Service layer.
}