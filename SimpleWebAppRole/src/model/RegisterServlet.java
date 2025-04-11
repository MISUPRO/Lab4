package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Register", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");
        String mail = request.getParameter("mail");

        // роль задаётся по умолчанию
        String role = "user";

        if (name == null || pass == null || mail == null || name.isEmpty() || pass.isEmpty() || mail.isEmpty()) {
            response.sendRedirect("register.jsp?error=Please fill all fields");
            return;
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Проверка на уникальность имени
            Long count = (Long) session.createQuery("SELECT COUNT(*) FROM UserEntity WHERE name = :name")//("FROM UserEntity WHERE name = :name")
                    .setParameter("name", name)
                    .uniqueResult();

            if (count != null && count > 0) {
                session.getTransaction().rollback();
                response.sendRedirect("register.jsp?error=User already exists");
                return;
            }

            // Создаём нового пользователя
            UserEntity user = new UserEntity();
            user.setName(name);
            user.setPassword(pass);
            user.setEmail(mail);
            user.setRole(role); // устанавливаем "user" по умолчанию

            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=Registration failed");
            return;
        }

        response.sendRedirect("index.jsp");
    }

    @Override
    public void destroy() {
        try {
            sessionFactory.close(); // Закрытие SessionFactory при завершении сервлета
            // Останавливаем поток очистки MySQL
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}