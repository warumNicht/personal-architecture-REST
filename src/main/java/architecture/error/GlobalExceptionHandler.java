package architecture.error;

import architecture.constants.ViewNames;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;


@ControllerAdvice
@Order(Integer.MIN_VALUE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BaseControllerException.class)
    public ResponseEntity controllerErrorHandler(HttpServletRequest req, BaseControllerException e) throws Exception {
//        if (AnnotationUtils.findAnnotation
//                (e.getClass(), ResponseStatus.class) != null){
//            System.out.println(e.getMessage());
//            throw e;
//        }

        return ResponseEntity.status(404).body(new HashMap<>() {{
            put("error", e.getMessage());
            put("timestamp", new Date());
        }});
    }

    @ExceptionHandler(value = RestException.class)
    protected ResponseEntity handleConflict(
            RestException ex, HttpServletRequest request) {
        return ResponseEntity.status(404).body(new HashMap<>() {{
            put("status", 404);
            put("error", ex.getMessage());
            put("timestamp", new Date());
        }});
    }

    //only for tests
    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView accessDeniedHandler(HttpServletRequest req, AccessDeniedException e) throws Exception {
        throw e;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise send error object
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>() {{
            put("error", e.getMessage());
            put("timestamp", new Date());
        }});
    }


}
