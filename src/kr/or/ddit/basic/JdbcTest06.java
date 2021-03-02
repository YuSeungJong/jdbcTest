package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil;

/*
 	회원을 관리하는 프로그램 작성하기
 	(DB시스템의 MYMEMBER테이블 이용)
 	
 	- 처리조건)
 	1. 아래 메뉴의 기능을 모두 구현한다.(CRUD 구현하기)
 	2. '자료 추가'에서는 입력한 회원 ID가 중복되는지 여부를 검사해서 중복되면 다시 입력 받도록 한다.
 	3. '자료 삭제'는 회원 ID를 입력 받아 삭제한다.
 	4. '자료 수정'은 회원 ID를 제외한 전체 자료를 수정한다.
 	
 	
 	메뉴예시)
 		-- 작업 선택 --
 		1. 자료 추가					--> insert (C)
 		2. 자료 삭제					--> delete (D)
 		3. 자료 수정					--> update (U)
 		4. 전체 자료 출력				--> select (R)
 		0. 작업 끝.
 		--------------
 		작업 번호>>
 	
*/
public class JdbcTest06 {
	Scanner scan = new Scanner(System.in);
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public static void main(String[] args) {
		
		new JdbcTest06().start();
	}
	
	private void start() {
		while(true) {
			System.out.println("-------------------");
			System.out.println("-- 작업 선택 --");
			System.out.println("1. 자료 추가");
			System.out.println("2. 자료 삭제");
			System.out.println("3. 자료 수정");
			System.out.println("4. 전체 자료 출력");
			System.out.println("0. 작업끝");
			System.out.println("--------------------");
			System.out.print("작업 번호>>");
			int input = scan.nextInt();
			
			switch(input) {
			case 1:
				insert();
				break;
			case 2:
				delete();
				break;
			case 3:
				update();
				break;
			case 4:
				select();
				break;
			case 0:
				System.out.println("프로그램이 종료되었습니다.");
				System.exit(0);
				break;
			default :
				System.out.println("잘못 선택했습니다. 다시 입력하세요.");
				System.out.println();
			}
			
		}
	}

	private void select() {
		try {
			conn = DBUtil.getConnection();
			
			String sql = "SELECT * FROM MYMEMBER";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			System.out.println("아이디\t이름\t전화번호\t주소");
			while(rs.next()) {
				System.out.print(rs.getString("mem_id") + "\t"
						+ rs.getString("mem_name") + "\t"
						+ rs.getString("mem_tel") + "\t"
						+ rs.getString("mem_addr") + "\t");
				
				System.out.println();
			}
			
			
		} catch (SQLException e) {
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (stmt != null)try {stmt.close();} catch (SQLException e) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}
	}

	private void update() {
		try {
			conn = DBUtil.getConnection();
			System.out.println("변경할 아이디: ");
			String mem_id = scan.next();
			System.out.println("변경할 이름: ");
			String mem_name = scan.next();
			System.out.println("변경할 전화번호: ");
			String mem_tel = scan.next();
			System.out.println("변경할 주소: ");
			String mem_addr = scan.next();
			String sql = " update mymember set "
					+ " mem_name = ?"
					+ " ,mem_tel = ?"
					+ " ,mem_addr = ?"
					+ "where mem_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mem_name);
			pstmt.setString(2, mem_tel);
			pstmt.setString(3, mem_addr);
			pstmt.setString(4, mem_id);
			
			
			System.out.println("수정이 완료되었습니다.");
			
			
			
		} catch (SQLException e) {
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (stmt != null)try {stmt.close();} catch (SQLException e) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}
		
	}

	private void delete() {
		try {
			conn=DBUtil.getConnection();
			System.out.println("삭제할 아이디: ");
			String mem_id = scan.next();
			String sql = "delete from mymember where mem_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mem_id);
			pstmt.executeUpdate();
			
			
			System.out.println("삭제가 완료되었습니다.");
			
		} catch (SQLException e) {
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (stmt != null)try {stmt.close();} catch (SQLException e) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}	
	}

	private void insert() {
		try {
			conn=DBUtil.getConnection();
			String mem_id;
			int count = 0;
			do {
				System.out.println("ID입력");
				mem_id = scan.next();
				
				String sql1 = "select count(*) cnt from MYMEMBER where MEM_ID = ?";
				pstmt = conn.prepareStatement(sql1);
				pstmt.setString(1, mem_id);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					count = rs.getInt("cnt");
				}
				
				if(count>0) {
					System.out.println("입력한 ID " + mem_id + "는 이미 등록된 ID입니다.");
					System.out.println("다시 입력하세요.");
				}
				
			}while(count>0);
			
			System.out.println("이름: ");
			String mem_name = scan.next();
			
			System.out.println("전화번호: ");
			String mem_tel = scan.next();
			
			System.out.println("주소: ");
			String mem_addr = scan.next();
			
			String sql2 = "INSERT INTO MYMEMBER (mem_id, mem_name, mem_tel, mem_addr )"
					+ "VALUES (?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, mem_id);
			pstmt.setString(2, mem_name);
			pstmt.setString(3, mem_tel);
			pstmt.setString(4, mem_addr);
			
			pstmt.executeUpdate();
			
			
			System.out.println("추가가 완료되었습니다.");
			
		} catch (SQLException e) {
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException e) {}
			if (stmt != null)try {stmt.close();} catch (SQLException e) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException e) {}
			if (conn != null)try {conn.close();} catch (SQLException e) {}
		}
	}

}




























