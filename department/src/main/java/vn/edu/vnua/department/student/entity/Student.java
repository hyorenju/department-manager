package vn.edu.vnua.department.student.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.intern.entity.Intern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", length = 100)
    private String studentId;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "class_id", length = 100)
    private String classId;

    @Column(name = "phone_number", length = 200)
    private String phoneNumber;

    @Column(length = 200)
    private String company;

    @ManyToOne
    @JoinColumn(name = "intern_id")
    private Intern intern;

    @Column
    private String note;
}

