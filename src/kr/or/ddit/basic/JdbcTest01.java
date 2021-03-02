package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// JDBC(Java DataBase Connectivity)라이브러리를 이용한 DB자료 처리하기

public class JdbcTest01 {
	/*
		데이터베이스 처리 순서 ==> JDBC라이브러리를 프로젝트에 등록한 후 
	1. 드라이버 로딩 ==> 라이브러리를 사용할 수 있게 메모리에 읽어 들이는 작업
		Class.forName("oracle.jdbc.driver.OracleDriver");	
	2. DB에 접속하기 ==> 접속이 성공하면 Connection객체가 반환된다.
		DriverManager.getConnection()메서드를 이용한다.
	3. 질의 ==> SQL문장을 DB서버로 보내서 결과를 얻어온다.
		(Connection객체를 이용해서 Statement객체 또는 PrepatedStatement객체를 구한 후
		이 두 객체 중 하나를 이용하여 작업을 수행한다.)
	4. 처리결과 ==> 질의 결과를 받아서 원하는 작업을 수행한다.
		1) SQL문이 select문일 경우에는 select한 결과가 ResultSet객체에 저장되어 반환된다.
		2) SQL문이 select문이 아닌 경우(insert문, update문, delete문 등)에는 정수값을 반환한다.
		 	(이 정수값은 SQL문이 실행에 성공한 레코드 수이다.)
 	5. 사용한 자원을 반납한다.  ==> 사용한 객체의 close()메서드 이용
		
	*/
	
	public static void main(String[] args) {
		// DB작업에 필요한 객체변수 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			// 1. 드라이버 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2. DB연결 ==> Connection객체 생성
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"yu",
					"java");
			
			// 3-1. 실행할 SQL문 작성
			String sql = "select * from lprod";
			
			// 3-2. Statement객체 생성 ==> Connection객체를 이용한다.
			stmt = conn.createStatement();
			
			// 4. SQL문을 DB서버로 전송해서 실행하고 결과를 얻어온다.
			//	  (지금은 실행할 SQL문이 select문이기 때문에 결과가 ResultSet객체에 저장되어 반환된다.)
			rs = stmt.executeQuery(sql);
			
			// 5. 결과 처리하기 ==> 한 레코드씩 화면에 출력하기
			// 		==> ResultSet에 저장된 데이터를 차례로 꺼내오려면 반복문과 next()메서드를 이용한다.
			System.out.println("== 처리 결과 출력 ==");
			/*
			 	rs.next() ==> ResultSet객체의 데이터를 가리키는 포인터를 다음 레코드 자리로 이동시키고
			 				    그 곳에 데이터가 있으면 true, 없으면 false를 반환한다.
			*/
			while(rs.next()) {
				// 포인터가 가리키는 곳의 데이터를 가져오는 방법
				// 형식1) rs.get자료형이름("컬럼명")
				// 형식2) rs.get자료형이름(컬럼번호)	==> 컬럼번호는 1부터 시작
				// 형식3) rs.get자료형이름("컬럼의 alias명")
				
				System.out.println("Lprod_id : " + rs.getInt("lprod_id"));
				System.out.println("Lprod_gu : " + rs.getString(2));
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

















