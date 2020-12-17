package kr.mem.model;
//1. import
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class MemberDAO {
	private Connection conn; 		 //연결 객체(db하기위해 연결 필요)
	private PreparedStatement ps;    //sql문 전송위함
	private ResultSet rs;   		 //sql결과의 집합을 담아서 처리하는
	
	//특정db에 맞춰놓으면 유지보수 어려움
	//컴파일시점이 아닌(->드라이버 미리 만들어놔야함) 실행시 메모리를 올려주는 동적로딩..
	// 초기화 블록 
		//(어떤 객체 실행시 무조건 '한 번' 실행 됨 <- 그렇기에 동적로딩도 이 안에 적어 한 번만 로딩되게 함)
	static {
		try {				// DriverManager
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) { 	//catch에서 예외처리 해줌 
			e.printStackTrace();
		}
	}
	
	
	public Connection getConnect() {	//2.연결객체 만드는 작업
		String url="jdbc:oracle:thin:@127.0.0.1:1521:XE"; //@뒤로는 바뀔 수 있음
		String user="hr";
		String password="hr";
	
		try {
			//driverManager로 db와 연결시도! 그리고 변수에 connection정보 담아줌
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return conn;
	}
	
	
	public int memberInsert(MemberVO vo) {
		conn = getConnect();
		// MyBatis 프레임워크 == sql 분리해서 쓸 수 있음
		String SQL = "insert into tblMem values (seq_num.nextval, ?, ?, ?, ?, ?)"; //parameter가 있는 sql(=미완성 sql)
		int cnt = -1; //-1(실패)
		try {
			//속도 빠르게하기위해 sql날려서 먼저 컴파일 함(?는 컴파일대상아님_ 추후 파라미터 셋팅만 해주면 됨)
			ps = conn.prepareStatement(SQL);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getPhone());
			ps.setString(3, vo.getAddr());
			ps.setDouble(4, vo.getLat());
			ps.setDouble(5, vo.getLng());
			cnt = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return cnt;
	}

	
	public ArrayList<MemberVO> memberAllList() {
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		conn = getConnect();
		String SQL="select * from tblMem order by num desc";
		try {
			ps=conn.prepareStatement(SQL);
			rs=ps.executeQuery(); //rs는 결국 커서의 역할 (레코드있으면 가지고 오고, 없으면 빠져나와야함)
			while(rs.next()) {		//가져와서 arrayList에 담아줘야함
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String addr = rs.getString("addr");
				Double lat = rs.getDouble("lat");
				Double lng = rs.getDouble("lng");
				MemberVO vo = new MemberVO(num, name, phone, addr, lat, lng);
				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return list;
		
	}

	public int memberDelete(int num){
		conn = getConnect();
		String SQL="delete from tblMem where num=?";
		int cnt=-1;
		try {
			ps=conn.prepareStatement(SQL);
			ps.setInt(1, num);
			cnt = ps.executeUpdate(); //1
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return cnt;
	}
	
	public MemberVO memberContent(int num) {
		MemberVO vo = null; //선택한사람(num)없으면 그냥 null이 넘어가면 됨.
		conn=getConnect();
		String SQL = "select * from tblMem where num=?";
		try {
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if (rs.next()) {
				num = rs.getInt("num");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String addr = rs.getString("addr");
				Double lat = rs.getDouble("lat");
				Double lng = rs.getDouble("lng");
				vo = new MemberVO(num, name, phone, addr, lat, lng);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return vo;
		
				
	}
	
	public int memberUpdate(MemberVO vo) {
		conn = getConnect();
		String SQL = "update tblMem set phone=?, addr=? where num=?";
		int cnt = -1;
		try {
			ps = conn.prepareStatement(SQL);
			ps.setString(1, vo.getPhone());
			ps.setString(2, vo.getAddr());
			ps.setInt(3, vo.getNum());
			cnt = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		} 
		return cnt;
		
	}
	

	//자원 낭비(메모리누수) 방지를 위한 메소드 생성
	public void dbClose() {
		try {
			if (rs!=null) 
				rs.close();
			if (ps!=null) 
				ps.close();
			if(conn!=null) 
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
