package pretty.april.achieveitserver.enums;

public enum ErrorCode {
    SUCCESS(1000, "Success"),
    INVALID_USERNAME_PASSWORD(1001, "Either username or password is invalid."),
    AUTHENTICATION_FAILED(1002, "Authentication failed. You must provide a valid auth."),
    UNAUTHORIZED(1003, "You have no authorization to proceed."),
    INVALID_TOKEN(1004, "Your token is invalid."),
    SYSTEM_ERROR(1005, "System error.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
