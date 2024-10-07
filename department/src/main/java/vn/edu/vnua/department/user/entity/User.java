package vn.edu.vnua.department.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.domain.validation.ImportUserValidation;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.user.model.UserExcelData;
import vn.edu.vnua.department.userjointask.entity.UserTask;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(length = 100)
    private String id;

    @Column(name = "first_name", length = 200)
    private String firstName;

    @Column(name = "last_name", length = 200)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "degree_id")
    private MasterData degree;

    @Column(length = 200, unique = true)
    private String email;

    @Column(name = "phone_number", length = 200)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private String note;

    @Column(length = 200)
    private String password;

    @Column(name = "create_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "manage", length = 100)
    private String manage;

    @Column(name = "avatar", length = 2000)
    private String avatar;

    @Column(name = "is_lock", length = 2000)
    private Boolean isLock = false;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority(role.getId()));
            role.getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getId())));
        }
        return authorities;
    }

    public List<UserExcelData.ErrorDetail> validateInformationDetailError(List<UserExcelData.ErrorDetail> errorDetailList){
        if(!ImportUserValidation.validateUserId(id)) {
            errorDetailList.add(UserExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Mã người dùng không hợp lệ").build());
        }
        if(!ImportUserValidation.validateUserName(firstName)) {
            errorDetailList.add(UserExcelData.ErrorDetail.builder().columnIndex(1).errorMsg("Không được chứa ký tự đặc biệt").build());
        }
        if (!ImportUserValidation.validateUserName(lastName)) {
            errorDetailList.add(UserExcelData.ErrorDetail.builder().columnIndex(2).errorMsg("Không được chứa ký tự đặc biệt").build());
        }
        if (!ImportUserValidation.validateUserEmail(email)) {
            errorDetailList.add(UserExcelData.ErrorDetail.builder().columnIndex(4).errorMsg("Địa chỉ mail không hợp lệ").build());
        }
        if (!ImportUserValidation.validateUserPhoneNumber(phoneNumber)) {
            errorDetailList.add(UserExcelData.ErrorDetail.builder().columnIndex(5).errorMsg("SĐT không hợp lệ").build());
        }
        return errorDetailList;
    }

    @OneToMany(mappedBy = "instructor")
    private Collection<Intern> interns;

    @OneToMany(mappedBy = "createdBy")
    private Collection<Internship> createdInternships;

    @OneToMany(mappedBy = "modifiedBy")
    private Collection<Internship> modifiedInternships;

    @OneToMany(mappedBy = "teacher")
    private Collection<Teaching> teachingAssignments;

    @OneToMany(mappedBy = "lecturerTeach")
    private Collection<Exam> examsHRTeacher;

    @OneToMany(mappedBy = "proctor1")
    private Collection<Exam> examsProctor1;

    @OneToMany(mappedBy = "proctor2")
    private Collection<Exam> examsProctor2;

    @OneToMany(mappedBy = "marker1")
    private Collection<Exam> examsMarker1;

    @OneToMany(mappedBy = "marker2")
    private Collection<Exam> examsMarker2;

    @OneToMany(mappedBy = "picker")
    private Collection<Exam> examsPicker;

    @OneToMany(mappedBy = "printer")
    private Collection<Exam> examsPrinter;

    @OneToMany(mappedBy = "questionTaker")
    private Collection<Exam> questionTakers;

    @OneToMany(mappedBy = "examTaker")
    private Collection<Exam> examTakers;

    @OneToMany(mappedBy = "examGiver")
    private Collection<Exam> examGivers;

    @OneToMany(mappedBy = "pointGiver")
    private Collection<Exam> pointGivers;

    @OneToMany(mappedBy = "createdBy")
    private Collection<Teaching> createdTeachingAssignments;

    @OneToMany(mappedBy = "modifiedBy")
    private Collection<Teaching> modifiedTeachingAssignments;

    @OneToMany(mappedBy = "createdBy")
    private Collection<Exam> createdExamAssignments;

    @OneToMany(mappedBy = "modifiedBy")
    private Collection<Exam> modifiedExamAssignments;

    @OneToMany(mappedBy = "createdBy")
    private Collection<Faculty> createdFaculties;

    @OneToMany(mappedBy = "modifiedBy")
    private Collection<Faculty> modifiedFaculties;

    @OneToMany(mappedBy = "createdBy")
    private Collection<Department> createdDepartments;

    @OneToMany(mappedBy = "modifiedBy")
    private Collection<Department> modifiedDepartments;

    @OneToMany(mappedBy = "createdBy")
    private Collection<AClass> createdClasses;

    @OneToMany(mappedBy = "modifiedBy")
    private Collection<AClass> modifiedClasses;

    @OneToMany(mappedBy = "createdBy")
    private Collection<Subject> createdSubjects;

    @OneToMany(mappedBy = "modifiedBy")
    private Collection<Subject> modifiedSubjects;

    @OneToMany(mappedBy = "createdBy")
    private Collection<Project> createdProjects;

    @OneToMany(mappedBy = "user")
    private Collection<UserTask> tasksJoined;
}
