package pretty.april.achieveitserver.dto;

import lombok.Data;

@Data
public class ConfigDTO {
    private String gitRepoAddress;
    private String fileServerDir;
    private String mail;
    private Integer virtualMachineSpace;
    private Boolean isFileServerDirConfirmed;
    private Boolean isMailConfirmed;
}
