package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberDTO {
    private Integer userId;
    private String username;
    private String realName;
    private String email;
    private String department;
    private String phoneNumber;
    private Integer leaderId;
    private String leaderUsername;
    private String leaderRealName;
    private List<String> roles;
    private Float workingHours;
}
