package kr.mem.pojo;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.mem.model.MemberDAO;
import kr.mem.model.MemberVO;

public class MemberListController implements Controller{

	@Override
	public String requestHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();
		ArrayList<MemberVO> list = dao.memberAllList();
		request.setAttribute("list", list);  //객체바인딩
		return "member/memberList.jsp"; //view.. forwarding페이지 알려줌 (앞에/WEB-INF/views/지움)

	}
	
}
