package pretty.april.achieveitserver.request;

import lombok.Data;

@Data
public class AddProjectConfigRequest {

    private String fileServerDir;
    private String mail;
    private Boolean isFileServerDirConfirmed;
    private Boolean isMailConfirmed;
}
