package kr.mem.pojo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.mem.model.MemberDAO;

public class MemberDeleteController implements Controller{

	@Override
	public String requestHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String cpath = request.getContextPath();
		
		int num = Integer.parseInt(request.getParameter("num"));
        MemberDAO dao = new MemberDAO();
        int cnt = dao.memberDelete(num);
		String page = null;
		if(cnt>0) {
			page="redirect:"+cpath+"/list.do";	//context¸í: /mvc1
		} else {
			throw new ServletException("error");
		}
		return page;
	}

}
