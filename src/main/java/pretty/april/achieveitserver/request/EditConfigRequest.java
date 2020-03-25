package pretty.april.achieveitserver.request;

import lombok.Data;
import lombok.Getter;

@Data
public class EditConfigRequest {
    private String mail;
    private String gitRepoAddress;
    private Integer virtualMachineSpace;
    private Boolean isFileServerDirConfirmed;
}
