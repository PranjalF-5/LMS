package controller;

import com.pranjal.learning_management_system.dto.CourseResponseDTO;
import com.pranjal.learning_management_system.utils.CustomUserDetails;
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

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CourseService courseService;
    private final UserService userService;

    @PutMapping("/courses/{courseId}/status")
    public ResponseEntity<CourseResponseDTO> updateCourseStatus(
            @PathVariable Long courseId,
            @RequestParam CourseStatus status,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User admin = customUserDetails.getUser();
        Course updatedCourse = courseService.updateCourseStatus(courseId, status, admin);
        CourseResponseDTO response = CourseResponseDTO.builder()
                .courseId(updatedCourse.getCourseId())
                .name(updatedCourse.getName())
                .description(updatedCourse.getDescription())
                .status(updatedCourse.getStatus())
                .instructorId(updatedCourse.getInstructor().getUserId())
                .instructorName(updatedCourse.getInstructor().getUsername())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
