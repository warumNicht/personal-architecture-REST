package architecture.web.controllers;

import architecture.constants.ViewNames;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccessController {
    @GetMapping("/unauthorized")
    public String unauthorized(HttpServletRequest req, Model model) {
        AccessDeniedException accessDeniedException = (AccessDeniedException) req.getSession()
                .getAttribute("accessDeniedException");
        model.addAttribute("exception", accessDeniedException);
        return ViewNames.UNAUTHORIZED;
    }
}
