package vn.edu.vnua.department.intern.entity;

import lombok.Data;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.user.entity.UserBasicDTO;

@Data
public class InternBasicDTO {
    private Long id;
    private String name;
}
