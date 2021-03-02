import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil;

public class JdbcBoard {
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	private Scanner scan = new Scanner(System.in);
	public static void main(String[] args) {
		new JdbcBoard().start();
	}
	
	private void start() {
		while(true) {
			int choice = displayBoard();
			switch(choice) {
			case 1:
				insertBoard();
				break;
			case 2:
				selectBoart();
				break;
			case 3:
				searchBoard();
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
			default:
				System.out.println("잘못 선택했습니다.");
			}
		}
	}
	private void searchBoard() {
		System.out.println("----------------------------");
		System.out.print("검색할 제목 입력:");
		String searchTitle = scan.nextLine();
		
		if(searchTitle.equals("")) {
			displayBoard();
		}
		
		searchTitle = "%"+searchTitle+"%";
		
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT * "
					+ "from JDBC_BOARD "
					+ "WHERE BOARD_TITLE LIKE ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchTitle);
			rs = pstmt.executeQuery();
			
			
			
			while(rs.next()) {
				int boardNo = rs.getInt("board_no");
				String title = rs.getString("board_title");
				String writer = rs.getString("board_writer");
				int cnt = rs.getInt("board_cnt");
				
				System.out.println("NO\t제목\t작성자\t조회수");
				System.out.println(boardNo + "\t" + title + "\t"
						+ writer + "\t" + cnt);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();}catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException e) {}
			if(conn!=null) try {conn.close();}catch(SQLException e) {}
		}
	}

	private void selectBoart() {
		System.out.print("보기를 원하는 게시물 번호 입력 >>");
		int boardNo = Integer.parseInt(scan.nextLine());
		
		System.out.println(boardNo + "번글 내용");
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT board_title, board_writer, board_date, board_content, board_cnt "
					+ "FROM JDBC_BOARD "
					+ "WHERE board_no = ? ";
			String sql1 = " UPDATE JDBC_BOARD SET "
					+ " BOARD_CNT = BOARD_CNT+1 ";
			pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String title = rs.getString("board_title");
				String writer = rs.getString("board_writer");
				String content = rs.getString("board_content");
				String date = rs.getString("board_date");
				int cnt = rs.getInt("board_cnt");
				
				System.out.println("-제 목 : " + title);
				System.out.println("-작성자 : " + writer);
				System.out.println("-내용 : " + content);
				System.out.println("-작성일 : " + date);
				System.out.println("-조회수 : " + cnt);
				System.out.println("----------------------------");
				
			}
			
			System.out.println("메뉴 : 1.수정\t2.삭제\t3.리스트로 가기");
			System.out.print("작업선택>>");
			int input = scan.nextInt();
			switch(input) {
			case 1:
				updateBoard(boardNo);
				break;
			case 2:
				deleteBoard(boardNo);
				break;
			case 3:
				return;
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();}catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException e) {}
			if(conn!=null) try {conn.close();}catch(SQLException e) {}
		}
	}
	
	

	private void deleteBoard(int boardNo) {
		System.out.println("--------------------");
		try {
			conn = DBUtil.getConnection();
			String sql = " delete jdbc_board where board_no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				System.out.println(boardNo + "번글이 삭제되었습니다.");
			}else {
				System.out.println("삭제 작업 실패~~");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) try {pstmt.close();}catch(SQLException e) {}
			if(conn!=null) try {conn.close();}catch(SQLException e) {}
		}
	}

	private void updateBoard(int boardNo) {
		System.out.println("-------------------");
		System.out.print("-제 목 : ");
		String title = scan.nextLine();
		System.out.print("-내 용 : ");
		String content = scan.nextLine();
		
		try {
			conn = DBUtil.getConnection();
			String sql = " update jdbc_board set board_title = ?, "
					+ " board_content = ? "
					+ " WHERE board_no=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, boardNo);
			
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				System.out.println(boardNo + "번글이 수정되었습니다.");
			}else {
				System.out.println("수정 작업 실패~~");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) try {pstmt.close();}catch(SQLException e) {}
			if(conn!=null) try {conn.close();}catch(SQLException e) {}
		}
	}

	private void insertBoard() {
		System.out.println("-----------------------------");
		System.out.print("제목 : ");
		String title = scan.nextLine();
		System.out.print("작성자 : ");
		String writer = scan.nextLine();

		System.out.print("내용 : ");
		String content = scan.nextLine();
		
		try {
			conn = DBUtil.getConnection();
			String sql = "INSERT INTO JDBC_BOARD (board_no, board_title, board_writer, board_date, board_cnt, board_content) "
					+ "VALUES(board_seq.nextval,?, ?, SYSDATE, 0, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, writer);
			pstmt.setString(3, content);
			
			int result = pstmt.executeUpdate();
			
			if(result>0) {
				System.out.println("새글이 추가되었습니다.");
			}else {
				System.out.println("새글 추가가 실패하였습니다.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) try {pstmt.close();}catch(SQLException e) {}
			if(conn!=null) try {conn.close();}catch(SQLException e) {}
		}
		
	}

	private int displayBoard() {
		System.out.println("--------------------------------");
		System.out.println("No\t제목\t작성자\t조회수");
		System.out.println("--------------------------------");
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "SELECT * FROM JDBC_BOARD";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int boardNo = rs.getInt("board_no");
				String title = rs.getString("board_title");
				String writer = rs.getString("board_writer");
				int cnt = rs.getInt("board_cnt");
				
				System.out.println(boardNo + "\t" 
						+ title + "\t"
						+ writer + "\t"
						+ cnt);
			}
			System.out.println("--------------------------------");
			System.out.println("메뉴 : 1. 새글작성\t2. 게시글보기\t3. 검색\t0.작업끝");
			System.out.print("작업선택>>");
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();}catch(SQLException e) {}
			if(stmt!=null) try {stmt.close();}catch(SQLException e) {}
			if(conn!=null) try {conn.close();}catch(SQLException e) {}
		}
		return Integer.parseInt(scan.nextLine());
	}
	
	private int getMemberCount(String memId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int count = 0;	// 회원ID의 개수가 저장될 변수
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select count(*) cnt from mymember where mem_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt("cnt");
			}
			
			
		} catch (SQLException e) {
			count = 0;
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();}catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException e) {}
			if(conn!=null) try {conn.close();}catch(SQLException e) {}
		}
		
		return count;
	}
	

}











