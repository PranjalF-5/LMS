package repository;

import models.ApprovalRequest;
import models.ApprovalStatus;
import models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {

    // Find approval requests by course
    List<ApprovalRequest> findByCourse(Course course);

    // Find approval requests by status (e.g., "APPROVED")
    List<ApprovalRequest> findByStatus(ApprovalStatus status);
}