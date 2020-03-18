package pretty.april.achieveitserver.utils;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.enums.ErrorCode;

public class ResponseUtils {

    public static Response<?> successResponse() {
        return errorResponse(ErrorCode.SUCCESS);
    }

    public static <T> Response<T> successResponse(T data) {
        return new Response<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static Response<?> errorResponse(ErrorCode statusEnum) {
        return new Response<>(statusEnum.getCode(), statusEnum.getMessage(), null);
    }

    public static Response<?> errorResponseWithMessage(ErrorCode statusEnum, String message) {
        return new Response<>(statusEnum.getCode(), message, null);
    }
}
