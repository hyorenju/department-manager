package vn.edu.vnua.department.student.entity;

import lombok.Data;
import vn.edu.vnua.department.intern.entity.InternBasicDTO;
import vn.edu.vnua.department.intern.entity.InternDTO;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;

@Data
public class StudentDTO {
    private Long id;
    private String studentId;
    private String name;
    private String phoneNumber;
    private String company;
    private InternBasicDTO intern;
    private String note;
}
