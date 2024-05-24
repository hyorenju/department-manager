package vn.edu.vnua.department.faculty.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.repository.FacultyRepository;
import vn.edu.vnua.department.faculty.request.CreateFacultyRequest;
import vn.edu.vnua.department.faculty.request.GetFacultyListRequest;
import vn.edu.vnua.department.faculty.request.UpdateFacultyRequest;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Faculty> getFacultyList(GetFacultyListRequest request) {
        return facultyRepository.findAll(PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by("createdAt").descending()));
    }

    @Override
    public List<Faculty> getFacultySelection() {
        return facultyRepository.findAll(Sort.by("name").ascending());
    }

    @Override
    public Faculty createFaculty(CreateFacultyRequest request) {
        if (facultyRepository.existsById(request.getId())) {
            throw new RuntimeException(Constants.FacultyConstant.FACULTY_ALREADY_EXIST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        return facultyRepository.saveAndFlush(
                Faculty.builder()
                        .id(request.getId())
                        .name(request.getName())
                        .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                        .createdBy(createdBy)
                        .build()
        );
    }

    @Override
    public Faculty updateFaculty(String id, UpdateFacultyRequest request) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());

        faculty.setName(request.getName());
        faculty.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        faculty.setModifiedBy(modifiedBy);

        return facultyRepository.saveAndFlush(faculty);
    }

    @Override
    public Faculty deleteFaculty(String id) {
        try{

        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));
        facultyRepository.delete(faculty);
        return faculty;
        } catch (Exception e) {
            throw new RuntimeException(Constants.FacultyConstant.CANNOT_DELETE);
        }
    }
}
