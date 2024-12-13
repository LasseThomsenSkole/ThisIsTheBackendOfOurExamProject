package org.example.groenfroebackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.groenfroebackend.model.Enums.JobTitle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobTitle jobTitle;

    @ManyToMany
    @JoinTable(name="user_course", joinColumns = @JoinColumn(name="course_id"), inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> users = new ArrayList<>();
}
