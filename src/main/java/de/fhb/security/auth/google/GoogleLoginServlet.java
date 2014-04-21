package de.fhb.security.auth.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import de.fhb.security.auth.User;
import de.fhb.security.auth.UserConverter;
import de.fhb.security.auth.UserSessionBean;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GoogleLoginServlet", urlPatterns = {"/oauth2callback"})
/**
 * @see https://code.google.com/p/google-api-java-client/wiki/OAuth2
 */
public class GoogleLoginServlet extends HttpServlet {

  public static final String CODE_URL_PARAM_NAME = "code";
  public static final String ERROR_URL_PARAM_NAME = "error";
  public static final String URL_MAPPING = "/oauth2callback";

  @Inject
  private GoogleLoginBean googleLoginBean;

  @Inject
  private UserSessionBean userSessionBean;

  @Override
  /**
   * @see
   * https://developers.google.com/google-apps/tasks/oauth-authorization-callback-handler?hl=de
   */
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Getting the "error" URL parameter
    String[] error = req.getParameterValues(ERROR_URL_PARAM_NAME);

    // Checking if there was an error such as the user denied access
    if (error != null && error.length > 0) {
      resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "There was an error: \"" + error[0] + "\".");
      return;
    }
    // Getting the "code" URL parameter
    String[] code = req.getParameterValues(CODE_URL_PARAM_NAME);

    // Checking conditions on the "code" URL parameter
    if (code == null || code.length == 0) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The \"code\" URL parameter is missing");
    } else {
      GoogleTokenResponse tokenResponse = googleLoginBean.convertCodeToToken(code[0]);
      String accessToken = tokenResponse.getIdToken();
      System.out.println("Access Token: " + accessToken);

      GoogleUser gu = googleLoginBean.getUser(tokenResponse);
      System.out.println(gu.getFamilyName());
      System.out.println(gu.getGender());
      System.out.println(gu.isVerifiedEmail());

      User user = UserConverter.convertGoogleUser(gu);
      userSessionBean.setUser(user);

      // http://www.adam-bien.com/roller/abien/entry/does_cdi_injection_of_sessionscoped
      String redirectUrl = userSessionBean.getDeniedUrl();
      LOG.log(Level.INFO, "Redirecting to: {0}", redirectUrl);

      resp.sendRedirect(redirectUrl);
      // 1. Save login in LoginBean / Session
      // 2. Redirect to desired page...
    }
  }
  private static final Logger LOG = Logger.getLogger(GoogleLoginServlet.class.getName());

  /**
   * Construct the OAuth code callback handler URL.
   *
   * @param req the HttpRequest object
   * @return The constructed request's URL
   */
  public static String getOAuthCodeCallbackHandlerUrl(HttpServletRequest req) {
    String scheme = req.getScheme() + "://";
    String serverName = req.getServerName();
    String serverPort = (req.getServerPort() == 80) ? "" : ":" + req.getServerPort();
    String contextPath = req.getContextPath();
    String servletPath = URL_MAPPING;
    String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
    return scheme + serverName + serverPort + contextPath + servletPath + pathInfo;
  }
}
