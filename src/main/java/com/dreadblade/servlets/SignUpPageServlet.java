package com.dreadblade.servlets;

import com.dreadblade.entity.User;

import com.dreadblade.servlets.email.EmailSender;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet creates and authenticate new users
 * And adds the attribute to the session "current_user" which contains an object of the User class
 */
@WebServlet(name = "SignUpPageServlet")
public class SignUpPageServlet extends HttpServlet {
    private static final String SIGN_UP_PAGE_PATH = "/WEB-INF/view/sign_up_page.jsp";
    private static final String VERIFICATION_PAGE_PATH = "/WEB-INF/view/verification_page.jsp";
    private static final Logger log = Logger.getLogger(SignUpPageServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("user_name"), req.getParameter("first_name"), req.getParameter("last_name"),
                req.getParameter("email"), req.getParameter("password"));
        try {
            String messageContent =
                    "<div style=\"font-family: -apple-system,BlinkMacSystemFont," +
                            "'Segoe UI',Roboto,'Helvetica Neue',Arial,'Noto Sans',sans-serif,'Apple Color Emoji'," +
                            "'Segoe UI Emoji','Segoe UI Symbol','Noto Color Emoji';\"><p style=\"background-color: #0069d9;" +
                            "color:#fff; text-align: center;\">yourNotes</p><hr style=\"color:#0069d9;\">" +
                            "<p>Hello " + user.getFirstName() + "!</p><br>" +
                            "<p>Your verification code is " + user.hashCode() + "</p><hr style=\"color:#0069d9;\">" +
                            "<p>Please, do not reply on this message</p></div>";
            EmailSender.sendEmail(user.getEmail(), "Verification", messageContent);
            log.trace("Hashcode for user " + user.getUsername() + " has been sent on email " + user.getEmail());
            HttpSession session = req.getSession();
            session.setAttribute("user_temp", user);
            getServletContext().getRequestDispatcher(VERIFICATION_PAGE_PATH).forward(req, resp);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher(SIGN_UP_PAGE_PATH).forward(req, resp);
    }
}