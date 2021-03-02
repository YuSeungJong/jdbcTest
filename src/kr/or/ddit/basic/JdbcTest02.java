package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// 문제1) 사용자로부터 Lprod_id값을 입력받아 입력한 값보다 Lprod_id값이 큰 자료들을 출력하시오.
 
public class JdbcTest02 {
	
	
	public static void main(String[] args) {
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "yu";
		String password = "java";
		
		Scanner scan = new Scanner(System.in);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		System.out.print("숫자입력");
		int input = Integer.parseInt(scan.nextLine());
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, password);
			
			String sql = "select * from lprod where lprod_id > " + input;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			System.out.println("처리결과 출력");
			
			
				while(rs.next()) {
	
					System.out.println("Lprod_id : " + rs.getInt("lprod_id"));
					System.out.println("Lprod_gu : " + rs.getString("lprod_gu"));
					System.out.println("Lprod_nm : " + rs.getString("lprod_nm"));
					System.out.println("--------------------------------------------");
					
				}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// 6. 사용했던 자원 반납하기
			if(rs!=null)try {rs.close(); }catch(SQLException e) {}
			if(stmt!=null)try {stmt.close(); }catch(SQLException e) {}
			if(conn!=null)try {conn.close(); }catch(SQLException e) {}
		}
		
	}

}
