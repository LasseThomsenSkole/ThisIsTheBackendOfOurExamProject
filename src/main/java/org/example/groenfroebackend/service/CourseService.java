package org.example.groenfroebackend.service;

import org.example.groenfroebackend.model.Course;
import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.model.User;
import org.example.groenfroebackend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.example.groenfroebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;


    public List<Course> findCourseByRole(JobTitle jobTitle){
        return courseRepository.findCoursesByJobTitle(jobTitle);
    }
    public Page<Course> searchCourse(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        if (keyword == null || keyword.trim().isEmpty()) { // if keyword is null or empty return all courses
            return courseRepository.findAll(pageable);
        }

        return courseRepository.searchCourse(keyword, pageable);
    }

    public void registerUserToCourse(int userId, int courseId){
        User user = userRepository.findById(userId).orElseThrow(() ->new RuntimeException());
        Course course = courseRepository.findById(courseId).orElseThrow(() ->new RuntimeException());
        course.getUsers().add(user);
        courseRepository.save(course);
    }

    public void unregisterUserFromCourse(int userId, int courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(()->new RuntimeException());
        course.getUsers().remove(userId);
        courseRepository.save(course);
    }

    public List<Course> findCoursesByUserId(int userId){
        return courseRepository.findCoursesByUserId(userId);
    }

    public List<User> findUsersByCourseId(int courseId){
        return courseRepository.findUsersByCourseId(courseId);
    }
}