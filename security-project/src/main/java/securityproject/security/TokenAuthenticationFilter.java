package securityproject.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import securityproject.model.user.MyUserDetails;
import securityproject.model.webtokens.JWTBlacklist;
import securityproject.service.UserService;
import securityproject.util.TokenUtils;

// Filter koji ce presretati SVAKI zahtev klijenta ka serveru
// (sem nad putanjama navedenim u WebSecurityConfig.configure(WebSecurity web))
// Filter proverava da li JWT token postoji u Authorization header-u u zahtevu koji stize od klijenta
// Ukoliko token postoji, proverava se da li je validan. Ukoliko je sve u redu, postavlja se autentifikacija
// u SecurityContext holder kako bi podaci o korisniku bili dostupni u ostalim delovima aplikacije gde su neophodni
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private TokenUtils tokenUtils;

    private UserService userDetailsService;

    protected final Log LOGGER = LogFactory.getLog(getClass());

    public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
        this.tokenUtils = tokenHelper;
        this.userDetailsService = (UserService) userDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String username;
        String authToken = tokenUtils.getToken(request);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String token = httpServletRequest.getHeader("Authorization");

            if(token != null){
                if(token.startsWith("Bearer ")){
                    token = token.substring(7);
                }
            }
            if (authToken != null) {
                username = tokenUtils.getUsernameFromToken(authToken);
                if (username != null) {
                    MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
                    System.out.println("filter: "+ userDetails.getUsername());

                    if (tokenUtils.validateToken(authToken, userDetails)
                            && !JWTBlacklist.isTokenBlacklisted(authToken)
                            //&& tokenUtils.verifyToken(authToken, userDetails.getUsername())
                    ) {
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }

        } catch (ExpiredJwtException ex) {
            LOGGER.debug("Token expired!");
        }

        chain.doFilter(request, response);
    }

}
