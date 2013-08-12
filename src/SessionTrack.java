import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SessionTrack
 */
@WebServlet("/st")
public class SessionTrack extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 获得基于这个request的session,如果没有,则创建一个
		HttpSession session = request.getSession(true);
		// 2. 获得Session创建的时间
		Date createTime = new Date(session.getCreationTime());
		// 3. 获得这个网页最后访问的时间
		Date lastAccessTime = new Date(session.getLastAccessedTime());
		// 4. 获得SessionID[Session的标识符]
		String sessionID = session.getId();

		String title = null;
		Integer visitCount = 0;
		String userIDValue = null;
		final String VISIT_CT_KEY = "visitCount";
		final String USER_ID_KEY = "userID";

		// 5 检查是不是一个新的Session, 如果是的话, 表示是新的用户
		if (session.isNew()) {
			// 5.1 如果是新用户，设置标题为：欢迎
			title = "Welcome to my website";
			// 5.2 随机获得一个用户ID
			userIDValue = String.valueOf(UUID.randomUUID());
			// 5.3 把用户ID存入session中
			session.setAttribute(USER_ID_KEY, userIDValue);
		} else {
			// 5.1 如果是老用户，设置标题为：欢迎回来
			title = "Welcome Back to my website";
			// 5.2 从Session中获得用户访问次数，+1
			visitCount = (Integer) session.getAttribute(VISIT_CT_KEY) + 1;
			// 5.3 从Session中获得用户ID
			userIDValue = (String) session.getAttribute(USER_ID_KEY);
		}
		// 6. 记录用户访问次数
		session.setAttribute(VISIT_CT_KEY, visitCount);

		// Set response content type
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n" + "<h1 align=\"center\">"
				+ title + "</h1>\n" + "<h2 align=\"center\">Session Infomation</h2>\n" + "<table border=\"1\" align=\"center\">\n"
				+ "<tr bgcolor=\"#949494\">\n" + "  <th>Session info</th><th>value</th></tr>\n" + "<tr>\n" + "  <td>id</td>\n" + "  <td>" + sessionID
				+ "</td></tr>\n" + "<tr>\n" + "  <td>Creation Time</td>\n" + "  <td>" + createTime + "  </td></tr>\n" + "<tr>\n"
				+ "  <td>Time of Last Access</td>\n" + "  <td>" + lastAccessTime + "  </td></tr>\n" + "<tr>\n" + "  <td>User ID</td>\n" + "  <td>"
				+ userIDValue + "  </td></tr>\n" + "<tr>\n" + "  <td>Number of visits</td>\n" + "  <td>" + visitCount + "</td></tr>\n" + "</table>\n"
				+ "</body></html>");
	}
}
