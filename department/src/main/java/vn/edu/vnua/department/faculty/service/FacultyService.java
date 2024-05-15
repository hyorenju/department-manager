package vn.edu.vnua.department.faculty.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.request.CreateFacultyRequest;
import vn.edu.vnua.department.faculty.request.GetFacultyListRequest;
import vn.edu.vnua.department.faculty.request.UpdateFacultyRequest;

import java.util.List;

public interface FacultyService {
    Page<Faculty> getFacultyList(GetFacultyListRequest request);
    List<Faculty> getFacultySelection();
    Faculty createFaculty(CreateFacultyRequest request);
    Faculty updateFaculty(String id, UpdateFacultyRequest request);
    Faculty deleteFaculty(String id);
}
