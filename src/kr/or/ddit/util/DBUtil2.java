package kr.or.ddit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// JDBC드라이버를 로딩하고, Connection객체를 생성하여 반환하는 메서드로 구성된 class

// Properties객체를 이용하여 처리하기
public class DBUtil2 {
	static Properties prop;
	
	static {
		prop = new Properties();
		
		File f = new File("res/dbinfo.properties");
		FileInputStream fin = null;
		
		try {
			fin = new FileInputStream(f);
			prop.load(fin);
			
//			Class.forName(prop.getProperty("driver"));
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패~~~");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("파일 입출력 오류~~~");
			System.out.println("드라이버 로딩 실패~~~");
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
//			return DriverManager.getConnection(
//					"jdbc:oracle:thin:@localhost:1521:xe",
//					"yu", "java");
			return DriverManager.getConnection(
					prop.getProperty("url"),
					prop.getProperty("user"),
					prop.getProperty("pass"));		
		} catch (SQLException e) {
			System.out.println("DB시스템 연결 실패!!");
			return null;
		}
	}
}

























