package securityproject.security.behaviors;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import securityproject.model.user.MyUserDetails;
import securityproject.service.UserService;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        MyUserDetails userDetails = (MyUserDetails) userService.loadUserByUsername(email);

        if (userDetails.getUser() != null) {
            if (userDetails.isEnabled() && userDetails.isAccountNonLocked()) {
                if (userDetails.getUser().getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(userDetails.getUser());
                } else {
                    userService.lock(userDetails.getUser());
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else if (!userDetails.isAccountNonLocked()) {
                if (userService.unlockWhenTimeExpired(userDetails.getUser())) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }

        }
//        response.sendRedirect("/login-failure?reason=" + "reason for failure");  // this is unsafe supposedly
        super.setDefaultFailureUrl("/login-failure");
        super.onAuthenticationFailure(request, response, exception);
    }

}
