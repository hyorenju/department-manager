package vn.edu.vnua.department.intern.entity;

import lombok.Data;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.user.entity.UserBasicDTO;
import vn.edu.vnua.department.user.entity.UserDTO;

@Data
public class InternDTO {
    private Long id;
    private String name;
    private MasterDataDTO type;
    private MasterDataDTO schoolYear;
    private Byte term;
    private UserDTO instructor;
    private String outlineFile;
    private String progressFile;
    private String finalFile;
    private String status;
    private String note;
    private Boolean isLock;
}
