package pretty.april.achieveitserver.model;

import lombok.Data;

@Data
public class MemberDetails {
    private Integer userId;
    private String username;
    private String realName;
    private String email;
    private String department;
    private String phoneNumber;
    private Integer leaderId;
    private String leaderName;
}
