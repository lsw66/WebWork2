package control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vo.User;
import dao.User_loginDao;

@WebServlet(urlPatterns = "/servlet/LoginController.do")

public class LoginController extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");//��ֹ����
		
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		String vcode=request.getParameter("vcode");
		String auto=request.getParameter("auto");
		
		
		HttpSession session=request.getSession();
		String rcode=(String)session.getAttribute("verityCode");
		
		
//		System.out.println("�Լ������"+vcode);
//		System.out.println("ϵͳ���ɵ�"+rcode);
		
		//ת�������url-pattern
		String Path="";
		
		User_loginDao userDao=new User_loginDao();
		User login=null;
		try {
			login = userDao.getUser(userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(!vcode.equalsIgnoreCase(rcode)){
			request.setAttribute("info", "��֤�벻��ȷ");
			Path="/error.jsp";
		}
		else{
			
			if(login == null){
				request.setAttribute("info", "��������û���������");
				Path="/error.jsp";
			}
			else{
				if(!login.getPassword().equals(password)){
					request.setAttribute("info", "��������û��������벻ƥ��");
					Path="/error.jsp";
				}
				else{
					if("on".equalsIgnoreCase(auto)){
						Cookie cookie=new Cookie("username",userName);
						cookie.setPath("/");
						cookie.setMaxAge(60*60*12*7);
						response.addCookie(cookie);
					}
					session.setAttribute("currentUser", login.getChrName());
					Path="/main.jsp";
						}
					}
				
			}
		RequestDispatcher rd=request.getRequestDispatcher(Path);
		rd.forward(request, response);
	}

}
