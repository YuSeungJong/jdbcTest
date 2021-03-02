package kr.or.ddit.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.or.ddit.board.vo.JDBCBoardVO;
import kr.or.ddit.util.DBUtil3;

public class JdbcBoardDaoImpl implements IJdbcBoardDao{
	//싱글톤
	private static JdbcBoardDaoImpl dao; //1번
	private JdbcBoardDaoImpl() { }       //2번
	public static JdbcBoardDaoImpl getInstance() {   //3번
		if(dao == null) dao = new JdbcBoardDaoImpl();
		return dao;
	}
	
	//DB작업에 필요한 객체 변수 선언
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//사용한 자원을 반납하는 메소드
	private void disconnection() {
		if(rs != null) try {rs.close();} catch(SQLException e) { }
		if(pstmt != null) try {pstmt.close();} catch(SQLException e) { }
		if(stmt != null) try {stmt.close();} catch(SQLException e) { }
		if(conn != null) try {conn.close();} catch(SQLException e) { }
	}
	
	@Override
	public int insertBoard(JDBCBoardVO boardVo) {
		int cnt = 0;
		try {
			conn = DBUtil3.getConnection();
			
			String sql = "INSERT INTO jdbc_board(board_no, board_title, board_writer, board_date, board_cnt, board_content) "
						+ "	VALUES (board_seq.nextval, ?, ?, SYSDATE, 0, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardVo.getBoard_title());
			pstmt.setString(2, boardVo.getBoard_writer());
			pstmt.setString(3, boardVo.getBoard_content());
			
		    cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			cnt = 0;
			e.printStackTrace();
		}finally {
			disconnection(); 
		}
		return cnt;
	}

	@Override
	public int deleteBoard(int boardNo) {
		int cnt = 0;
		try {
			conn = DBUtil3.getConnection();;
			String sql = "DELETE FROM jdbc_board WHERE board_no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			cnt = pstmt.executeUpdate();
		}catch(SQLException e) {
			cnt = 0;
			e.printStackTrace();
		}finally {
			disconnection(); 
		}
		return cnt;
	}

	@Override
	public int updateBoard(JDBCBoardVO boardVo) {
		int cnt = 0;
		try {
			conn = DBUtil3.getConnection();;
			String sql = "UPDATE jdbc_board SET board_title = ?, board_date = SYSDATE, board_content = ?"
						+ "	WHERE board_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardVo.getBoard_title());
			pstmt.setString(2, boardVo.getBoard_content());
			pstmt.setInt(3, boardVo.getBoard_no());
			
			cnt = pstmt.executeUpdate();
		 
		}catch(SQLException e) {
			cnt = 0;
			e.printStackTrace();
		}finally {
			disconnection(); 
		}
		return cnt;
	}

	@Override
	public List<JDBCBoardVO> getAllBoardList() {
		List<JDBCBoardVO> boardList = null;
		try {
			conn = DBUtil3.getConnection();
			String sql = "SELECT board_no, board_title, board_writer, to_char(board_date, 'YYYY-MM-DD') board_date, board_cnt, board_content"
					+ " FROM jdbc_board "
					+ " ORDER BY board_no DESC";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			boardList = new ArrayList<>();
			while(rs.next()) {
				JDBCBoardVO boardVo = new JDBCBoardVO();
				boardVo.setBoard_no(rs.getInt("board_no"));
				boardVo.setBoard_title(rs.getString("board_title"));
				boardVo.setBoard_writer(rs.getString("board_writer"));
				boardVo.setBoard_date(rs.getString("board_date"));
				boardVo.setBoard_cnt(rs.getInt("board_cnt"));
				boardVo.setBoard_content(rs.getString("board_content"));
				
				boardList.add(boardVo);
				
			}
		} catch (SQLException e) {
			boardList = null;
			e.printStackTrace();
		}finally {
			disconnection();
		}
		return boardList;
	}

	@Override
	public JDBCBoardVO getBoard(int boardNo) {
		JDBCBoardVO boardVo = null;
		try {
			conn = DBUtil3.getConnection();
			
			String sql = "SELECT board_no, board_title, board_writer, to_char(board_date, 'YYYY-MM-DD') board_date, board_cnt, board_content"
					+ " FROM jdbc_board "
					+ " WHERE board_no = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				boardVo = new JDBCBoardVO();
				
				boardVo.setBoard_no(rs.getInt("board_no"));
				boardVo.setBoard_title(rs.getString("board_title"));
				boardVo.setBoard_writer(rs.getString("board_writer"));
				boardVo.setBoard_date(rs.getString("board_date"));
				boardVo.setBoard_cnt(rs.getInt("board_cnt"));
				boardVo.setBoard_content(rs.getString("board_content"));				
				
			}
		}catch(SQLException e) {
			boardVo = null;
			e.printStackTrace();
		}finally {
			disconnection();
		}
		return boardVo;
	}

	@Override
	public List<JDBCBoardVO> getSearchBoardList(String title) {
		List<JDBCBoardVO> boardList = null;
		
		try {
			conn = DBUtil3.getConnection();
			String sql = "SELECT board_no, board_title, board_writer, to_char(board_date, 'YYYY-MM-DD') board_date, board_cnt, board_content"
					+ " FROM jdbc_board "
					+ " WHERE board_title like '%' || ? || '%' "
					+ " ORDER BY board_no DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			
			rs = pstmt.executeQuery();
			
			boardList = new ArrayList<>();
			while(rs.next()) {
				JDBCBoardVO boardVo = new JDBCBoardVO();
				
				boardVo.setBoard_no(rs.getInt("board_no"));
				boardVo.setBoard_title(rs.getString("board_title"));
				boardVo.setBoard_writer(rs.getString("board_writer"));
				boardVo.setBoard_date(rs.getString("board_date"));
				boardVo.setBoard_cnt(rs.getInt("board_cnt"));
				boardVo.setBoard_content(rs.getString("board_content"));
				
				boardList.add(boardVo);
			}
			
		} catch (SQLException e) {
			boardList = null;
			e.printStackTrace();
		}finally {
			disconnection();
		}
		return boardList;
	}

	@Override
	public int setCountIncrement(int boardNo) {
		int cnt = 0;
		try {
			conn = DBUtil3.getConnection();
			
			String sql = "UPDATE jdbc_board SET board_cnt = board_cnt + 1 "
					+ " WHERE board_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			cnt = 0;
			e.printStackTrace();
		}finally {
			disconnection();
		}
		return cnt;
	}
	
}
