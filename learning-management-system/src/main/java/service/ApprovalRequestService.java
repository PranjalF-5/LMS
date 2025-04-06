package service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import models.ApprovalRequest;
import models.ApprovalStatus;
import models.Course;
import models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ApprovalRequestRepository;
import repository.CourseRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class ApprovalRequestService {

    private final ApprovalRequestRepository approvalRequestRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public ApprovalRequest createApprovalRequest(Long courseId, User admin) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        ApprovalRequest request = ApprovalRequest.builder()
                .admin(admin)
                .course(course)
                .status(ApprovalStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();

        return approvalRequestRepository.save(request);
    }

    public List<ApprovalRequest> getApprovalRequestsByStatus(ApprovalStatus status) {
        return approvalRequestRepository.findByStatus(status);
    }
}
