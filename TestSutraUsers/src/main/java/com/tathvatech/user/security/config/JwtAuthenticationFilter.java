package com.tathvatech.user.security.config;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.DeviceService;
import com.tathvatech.user.service.PlanSecurityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
      if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        UserContext userContext = validateToken(token);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        SecurityContextHolder.setContext();
      }
    }
    filterChain.doFilter(request, response);
  }
  private UserContext validateToken(long userPk) throws Exception
  {
    // Check if it was issued by the server and if it's not expired
    // Throw an Exception if the token is invalid
    if (userPk ==0)
    {
      throw new AppException("MSG-Session expired");
    }
    User user = accountService.getUser(userPk);
    if (user == null)
    {
      throw new AppException("MSG-UserNotAuthorized");
    }
    if (!(User.STATUS_ACTIVE.equals(user.getStatus())))
    {
      throw new AppException("MSG-UserNotAuthorized");
    }
    UserContext context = DeviceContextCreator.createContext(user);
    PlanSecurityManager sManager = (PlanSecurityManager) context.getSecurityManager();
    boolean hasAccess = sManager.checkAccess(PlanSecurityManager.DEVICE_ACCESS,
            new com.tathvatech.ts.core.SecurityContext(null, null, null, null, null));
    if (!hasAccess)
      throw new AppException("Access denied");
    return context;

  }
}
