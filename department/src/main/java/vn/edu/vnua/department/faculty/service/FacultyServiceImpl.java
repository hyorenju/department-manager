package vn.edu.vnua.department.faculty.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.repository.FacultyRepository;
import vn.edu.vnua.department.faculty.request.CreateFacultyRequest;
import vn.edu.vnua.department.faculty.request.GetFacultyListRequest;
import vn.edu.vnua.department.faculty.request.UpdateFacultyRequest;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    @Override
    public Page<Faculty> getFacultyList(GetFacultyListRequest request) {
        return facultyRepository.findAll(PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Faculty createFaculty(CreateFacultyRequest request) {
        if (facultyRepository.existsById(request.getId())) {
            throw new RuntimeException(Constants.FacultyConstant.FACULTY_ALREADY_EXIST);
        }
        return facultyRepository.saveAndFlush(
                Faculty.builder()
                        .id(request.getId())
                        .name(request.getName())
                        .build()
        );
    }

    @Override
    public Faculty updateFaculty(String id, UpdateFacultyRequest request) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));
        faculty.setName(request.getName());
        return facultyRepository.saveAndFlush(faculty);
    }

    @Override
    public Faculty deleteFaculty(String id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));
        facultyRepository.delete(faculty);
        return faculty;
    }
}
