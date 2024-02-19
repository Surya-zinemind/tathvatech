package com.tathvatech.user.security.config;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.DeviceContextCreator;
import com.tathvatech.user.service.DeviceService;
import com.tathvatech.user.service.PlanSecurityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final DeviceService deviceService;
  private final AccountService accountService;
  private final DeviceContextCreator deviceContextCreator;
//  private final TokenRepository tokenRepository;
  private final HandlerExceptionResolver handlerExceptionResolver;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
//    if (request.getServletPath().contains("/api/v1/auth")) {
//      filterChain.doFilter(request, response);
//      return;
//    }
    final String authHeader = request.getHeader("Authorization");
    final String username;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    String jwt = authHeader.substring(7);
    username = jwtService.extractUsername(jwt);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (username != null && authentication == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        UserContext userContext= null;
        try {
            userContext = validateToken(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userContext,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
  private UserContext validateToken(String username) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    // Check if it was issued by the server and if it's not expired
    // Throw an Exception if the token is invalid
    if (!StringUtils.isNotBlank(username))
    {
      throw new AppException("MSG-Session expired");
    }
    User user = accountService.findUserByUserName(username);
    if (user == null)
    {
      throw new AppException("MSG-UserNotAuthorized");
    }
    if (!(User.STATUS_ACTIVE.equals(user.getStatus())))
    {
      throw new AppException("MSG-UserNotAuthorized");
    }
    UserContext context = deviceContextCreator.createContext(user);
    PlanSecurityManager sManager = (PlanSecurityManager) context.getSecurityManager();
    boolean hasAccess = sManager.checkAccess(PlanSecurityManager.DEVICE_ACCESS,
            new SecurityContext(null, null, null, null, null));
    if (!hasAccess)
      throw new AppException("Access denied");
    return context;

  }
}
