package microsoftAI.KrishiMitra.krishiApp.repository;

import microsoftAI.KrishiMitra.krishiApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA magic: We just name the method correctly, and it writes the SQL!
    Optional<User> findByPhoneNumber(String phoneNumber);
}