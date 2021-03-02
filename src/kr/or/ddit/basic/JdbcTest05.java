package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil;

/*
	LPROD테이블에 새로운 데이터 추가하기
	
	추가할 데이터 중 Lprod_gu와 Lprod_nm은 직접 입력 받아서 처리하는데
	입력받은 Lprod_gu가 이미 등록되어 있으면 다시 입력받아서 처리한다.
	그리고, Lprod_id값은 현재의 LPROD_ID값중 제일큰값보다 1증가
*/
public class JdbcTest05 {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//
//			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yu", "java");
			conn = DBUtil.getConnection();
			
			String gu;

			while (true) {
				System.out.print("LPROD_GU : ");
				gu = scan.nextLine();
				
				String sql = " select count(*) a from lprod where lprod_gu =  ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, gu);
				rs = pstmt.executeQuery();
				
				rs.next();
				if (rs.getInt("a") == 1) {
					System.out.println("중복됩니다.");
				} else
					break;
			}
			System.out.print("LPROD_NM : ");
			String nm = scan.nextLine();
			
			String sql = "select max(lprod_id) maxNo from lprod ";
			
			sql = " insert into lprod " + " (lprod_id, lprod_gu, lprod_nm) "
					+ " values ( (SELECT NVL(MAX(LPROD_ID),0) +1 FROM LPROD), ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, gu);
			pstmt.setString(2, nm);

			pstmt.executeUpdate();
			System.out.println("DB추가 성공!!");

		} catch (SQLException e) {
			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (stmt != null)try {stmt.close();} catch (SQLException e) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}
	}

}
