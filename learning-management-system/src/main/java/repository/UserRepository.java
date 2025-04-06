package repository;

import models.User;
import models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (for login)
    Optional<User> findByUsername(String username);

    // Find user by email (for login/password reset)
    Optional<User> findByEmail(String email);

    // Check if email is already registered
    boolean existsByEmail(String email);

    // Check if username is already registered
    boolean existsByUsername(String username);

    // Find users by role (e.g., "STUDENT")
    List<User> findByRole(Role role);
}