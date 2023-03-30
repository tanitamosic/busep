package securityproject.security;


import securityproject.model.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import securityproject.service.UserService;

@Service
@Configurable
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    PasswordEncoder pe;
    @Autowired
    UserService userService;

    /**
     * Attempts to authenticate the passed {@link Authentication} object, returning a
     * fully populated <code>Authentication</code> object (including granted authorities)
     * if successful.
     * <p>
     * An <code>AuthenticationManager</code> must honour the following contract concerning
     * exceptions:
     * <ul>
     * <li>A {@link DisabledException} must be thrown if an account is disabled and the
     * <code>AuthenticationManager</code> can test for this state.</li>
     * <li>A {@link LockedException} must be thrown if an account is locked and the
     * <code>AuthenticationManager</code> can test for account locking.</li>
     * <li>A {@link BadCredentialsException} must be thrown if incorrect credentials are
     * presented. Whilst the above exceptions are optional, an
     * <code>AuthenticationManager</code> must <B>always</B> test credentials.</li>
     * </ul>
     * Exceptions should be tested for and if applicable thrown in the order expressed
     * above (i.e. if an account is disabled or locked, the authentication request is
     * immediately rejected and the credentials testing process is not performed). This
     * prevents credentials being tested against disabled or locked accounts.
     *
     * @param authentication the authentication request object
     * @return a fully authenticated object including credentials
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String enteredUsername = (String) authentication.getPrincipal();
        String enteredPassword = (String) authentication.getCredentials();

        Authentication auth = null;
        MyUserDetails details = (MyUserDetails) userService.loadUserByUsername(enteredUsername);

        if (null != details) {
            String encodedPassword = details.getPassword();
            if (pe.matches(enteredPassword, encodedPassword)) {
                auth = new TokenBasedAuthentication(details);
            }
        }
        if (null == auth) {
            throw new BadCredentialsException("Username and password do not match.");
        }
        return auth;
    }
}