package org.example.groenfroebackend.controller;

import org.example.groenfroebackend.model.Course;
import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.security.UserPrincipal;
import org.example.groenfroebackend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/search")
    public ResponseEntity<Page<Course>> searchNews(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Course> results = courseService.searchCourse(keyword, page, size);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/coursefeed")
    public ResponseEntity<List<Map<String, Object>>> getRoleCourses(@AuthenticationPrincipal UserPrincipal userPrincipal){
        JobTitle jobTitle = userPrincipal.getJobTitle();

        List<Course> jobTitleCourses = courseService.findCourseByRole(JobTitle.valueOf(jobTitle.name()));

        List<Map<String, Object>> foundCourses = jobTitleCourses.stream()
                .distinct()
                .map(course -> {
                    Map<String, Object> CourseDetails = new HashMap<>();
                    CourseDetails.put("title", course.getTitle());
                    CourseDetails.put("description", course.getDescription());
                    CourseDetails.put("date", course.getDate());
                    CourseDetails.put("startTime", course.getStartTime());
                    CourseDetails.put("endTime", course.getEndTime());
                    CourseDetails.put("location", course.getLocation());
                    return CourseDetails;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(foundCourses);
    }

    @PostMapping("/{courseId}/register/")
    public ResponseEntity<String> registerUser(@PathVariable int courseId,@AuthenticationPrincipal UserPrincipal userPrincipal ){
        courseService.registerUserToCourse(userPrincipal.getId(), courseId);
        return ResponseEntity.ok("User registered for course");
    }

    @DeleteMapping("/{courseId}/unregister/{userId}")
    public ResponseEntity<String> unregisterUser(@PathVariable int courseId, @PathVariable int userId){
        courseService.unregisterUserFromCourse(userId, courseId);
        return ResponseEntity.ok("User unregistered for course");
    }

}
