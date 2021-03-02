package kr.or.ddit.board.service;

import java.util.List;

import kr.or.ddit.board.dao.IJdbcBoardDao;
import kr.or.ddit.board.dao.JdbcBoardDaoImpl;
import kr.or.ddit.board.vo.JDBCBoardVO;

public class JdbcBoardServiceImpl implements IJdbcBoardService {
	private IJdbcBoardDao dao;
	private static JdbcBoardServiceImpl service;
	private JdbcBoardServiceImpl() {
		dao = JdbcBoardDaoImpl.getInstance();
	}
	
	public static JdbcBoardServiceImpl getInstance() {
		if(service == null) service = new JdbcBoardServiceImpl();
		return service;
	}
	
	@Override
	public int insertBoard(JDBCBoardVO boardVo) {
		return dao.insertBoard(boardVo);
	}
	@Override
	public int deleteBoard(int boardNo) {
		return dao.deleteBoard(boardNo);
	}
	@Override
	public int updateBoard(JDBCBoardVO boardVo) {
		return dao.updateBoard(boardVo);
	}
	@Override
	public List<JDBCBoardVO> getAllBoardList() {
		return dao.getAllBoardList();
	}
	@Override
	public JDBCBoardVO getBoard(int boardNo) {
		int cnt = setCountIncrement(boardNo);	// 조회수를 증가시킨다.
		if(cnt==0) {	// 조회수 증가가 실패했을 때
			return null;
		}
		return dao.getBoard(boardNo);
	}
	@Override
	public List<JDBCBoardVO> getSearchBoardList(String title) {
		return dao.getSearchBoardList(title);
	}
	@Override
	public int setCountIncrement(int boardNo) {
		return dao.setCountIncrement(boardNo);
	}
}
