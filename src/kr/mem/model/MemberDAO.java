package kr.mem.model;
//1. import
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class MemberDAO {
	private Connection conn; 		 //���� ��ü(db�ϱ����� ���� �ʿ�)
	private PreparedStatement ps;    //sql�� ��������
	private ResultSet rs;   		 //sql����� ������ ��Ƽ� ó���ϴ�
	
	//Ư��db�� ��������� �������� �����
	//�����Ͻ����� �ƴ�(->����̹� �̸� ����������) ����� �޸𸮸� �÷��ִ� �����ε�..
	// �ʱ�ȭ ���� 
		//(� ��ü ����� ������ '�� ��' ���� �� <- �׷��⿡ �����ε��� �� �ȿ� ���� �� ���� �ε��ǰ� ��)
	static {
		try {				// DriverManager
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) { 	//catch���� ����ó�� ���� 
			e.printStackTrace();
		}
	}
	
	
	public Connection getConnect() {	//2.���ᰴü ����� �۾�
		String url="jdbc:oracle:thin:@127.0.0.1:1521:XE"; //@�ڷδ� �ٲ� �� ����
		String user="hr";
		String password="hr";
	
		try {
			//driverManager�� db�� ����õ�! �׸��� ������ connection���� �����
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return conn;
	}
	
	
	public int memberInsert(MemberVO vo) {
		conn = getConnect();
		// MyBatis �����ӿ�ũ == sql �и��ؼ� �� �� ����
		String SQL = "insert into tblMem values (seq_num.nextval, ?, ?, ?, ?, ?)"; //parameter�� �ִ� sql(=�̿ϼ� sql)
		int cnt = -1; //-1(����)
		try {
			//�ӵ� �������ϱ����� sql������ ���� ������ ��(?�� �����ϴ��ƴ�_ ���� �Ķ���� ���ø� ���ָ� ��)
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
			rs=ps.executeQuery(); //rs�� �ᱹ Ŀ���� ���� (���ڵ������� ������ ����, ������ �������;���)
			while(rs.next()) {		//�����ͼ� arrayList�� ��������
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
		MemberVO vo = null; //�����ѻ��(num)������ �׳� null�� �Ѿ�� ��.
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
	

	//�ڿ� ����(�޸𸮴���) ������ ���� �޼ҵ� ����
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