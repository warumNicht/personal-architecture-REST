package architecture.error;

import architecture.constants.ViewNames;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
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
    public ModelAndView controllerErrorHandler(HttpServletRequest req, BaseControllerException e) throws Exception {
//        if (AnnotationUtils.findAnnotation
//                (e.getClass(), ResponseStatus.class) != null){
//            System.out.println(e.getMessage());
//            throw e;
//        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(ViewNames.CONTROLLER_ERROR);
        return mav;
    }

    @ExceptionHandler(value = RestException.class)
    protected ResponseEntity<Object> handleConflict(
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
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.setViewName(ViewNames.DEFAULT_ERROR);
        return mav;
    }


}
