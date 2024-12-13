package org.example.groenfroebackend.repository;

import org.example.groenfroebackend.model.Course;
import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

    @Repository
    public interface CourseRepository extends JpaRepository<Course, Integer> {
        List<Course> findCoursesByJobTitle(JobTitle jobTitle);

        @Query("SELECT c FROM Course c JOIN c.users u WHERE u.id = :userId")
        List<Course> findCoursesByUserId(@Param("userId") int userId);

        @Query("SELECT u FROM User u JOIN u.courses c WHERE c.id = :courseId")
        List<User> findUsersByCourseId(@Param("courseId") int courseId);


        @Query("SELECT c FROM Course c WHERE " +
                "(LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
        Page<Course> searchCourse(@Param("keyword") String keyword,
                              Pageable pageable);
    }