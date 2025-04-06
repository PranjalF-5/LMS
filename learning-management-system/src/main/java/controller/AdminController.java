package controller;

import com.pranjal.learning_management_system.dto.CourseResponseDTO;
import com.pranjal.learning_management_system.utils.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import models.Course;
import models.CourseStatus;
import models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import service.CourseService;
import service.UserService;

/**
 * Controller class for handling administrative operations.
 * All endpoints are secured and require ADMIN role.
 * Provides functionality for course status management and user administration.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    // Service dependencies for handling business logic
    private final CourseService courseService;
    private final UserService userService;

    /**
     * Updates the status of a course.
     * 
     * @param courseId The ID of the course to update
     * @param status The new status to set for the course
     * @param customUserDetails The authenticated admin's details
     * @return ResponseEntity containing the updated course details
     * @throws RuntimeException if the course is not found or status update fails
     */
    @PutMapping("/courses/{courseId}/status")
    public ResponseEntity<CourseResponseDTO> updateCourseStatus(
            @PathVariable Long courseId,
            @RequestParam CourseStatus status,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        // Get the admin entity from the authenticated user details
        User admin = customUserDetails.getUser();
        
        // Update the course status using the service layer
        Course updatedCourse = courseService.updateCourseStatus(courseId, status, admin);
        
        // Build and return the response DTO
        CourseResponseDTO response = CourseResponseDTO.builder()
                .courseId(updatedCourse.getCourseId())
                .name(updatedCourse.getName())
                .description(updatedCourse.getDescription())
                .status(updatedCourse.getStatus())
                .instructorId(updatedCourse.getInstructor().getUserId())
                .instructorName(updatedCourse.getInstructor().getUsername())
                .build();
        
        // Return the course details with 200 OK status
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a user from the system.
     * 
     * @param userId The ID of the user to delete
     * @return ResponseEntity with a success message
     * @throws RuntimeException if the user is not found or deletion fails
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        // Delete the user using the service layer
        userService.deleteUser(userId);
        
        // Return success message with 200 OK status
        return ResponseEntity.ok("User deleted successfully");
    }
}
