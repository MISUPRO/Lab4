import model.UserEntity;
import model.AdminEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Login", urlPatterns = "/Login")
public class Login extends HttpServlet {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Проверяем, существует ли пользователь
            UserEntity user = (UserEntity) session.createQuery("FROM UserEntity WHERE name = :name")
                    .setParameter("name", name)
                    .uniqueResult();

            AdminEntity admin = (AdminEntity) session.createQuery("FROM AdminEntity WHERE name = :name")
                    .setParameter("name", name)
                    .uniqueResult();

            /*
            if (user == null) {
                // Пользователь не найден
                response.sendRedirect("index.jsp?error=User not found");
                return;
            }*/

            if (admin != null && admin.getPassword().equals(pass)) {

                request.getSession().setAttribute("userName", admin.getName());
                request.getSession().setAttribute("role", "admin");
                response.sendRedirect("admin.jsp");

            } else if (user != null && user.getPassword().equals(pass)) {

                request.getSession().setAttribute("userName", user.getName());
                request.getSession().setAttribute("role", "user");
                response.sendRedirect("welcome.jsp");

            } else {
                response.sendRedirect("index.jsp?error=Invalid credentials");
            }

            /*
            // Проверяем пароль
            if (user.getPassword().equals(pass)) {
                // После успешного входа добавь в сессию имя пользователя
                request.getSession().setAttribute("userName", user.getName());
                request.getSession().setAttribute("role", user.getRole());
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect("admin.jsp");
                } else {
                    response.sendRedirect("welcome.jsp");
                }
            } else {
                response.sendRedirect("index.jsp?error=Invalid password");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=Server error");
        }
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
}
