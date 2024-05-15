package vn.edu.vnua.department.masterdata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.user.entity.User;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "master_data")
public class MasterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, unique = true)
    private String name;

    @Column(length = 200)
    private String type;

    @OneToMany(mappedBy = "type")
    private Collection<Intern> internTopicTypes;

    @OneToMany(mappedBy = "form")
    private Collection<Exam> examForms;

    @OneToMany(mappedBy = "schoolYear")
    private Collection<Exam> examSchoolYears;

    @OneToMany(mappedBy = "schoolYear")
    private Collection<Intern> internSchoolYears;

    @OneToMany(mappedBy = "schoolYear")
    private Collection<Teaching> teachingSchoolYears;

    @OneToMany(mappedBy = "degree")
    private Collection<User> userDegrees;
}
