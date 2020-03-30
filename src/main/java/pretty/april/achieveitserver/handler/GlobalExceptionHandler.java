package pretty.april.achieveitserver.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.enums.ErrorCode;
import pretty.april.achieveitserver.exception.IllegalStateException;
import pretty.april.achieveitserver.exception.UserNotFoundException;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public Response<?> handleUserNotFoundException(HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseUtils.errorResponse(ErrorCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public Response<?> handleIllegalStateException() {
        return ResponseUtils.errorResponse(ErrorCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseUtils.errorResponseWithMessage(ErrorCode.INVALID_ARGUMENT, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response<?> handlerIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseUtils.errorResponseWithMessage(ErrorCode.INVALID_ARGUMENT, e.getMessage());
    }
}
