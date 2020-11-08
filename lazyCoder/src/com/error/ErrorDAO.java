package com.error;

import java.sql.SQLException;
import java.util.List;

public interface ErrorDAO {
	public int insertBoard(ErrorDTO dto, String mode) throws SQLException;
	public int updateOrderNo(int groupNum, int orderNo) throws SQLException;
	public int updateBoard(ErrorDTO dto) throws SQLException;
	public int deleteBoard(int boardNum, String userId) throws SQLException;
	public int updateHitCount(int boardNum) throws SQLException;
	
	public int dataCount(String category);
	public int dataCount(String condition, String keyword,String category);
	
	public List<ErrorDTO> listBoard(int offset, int rows,String category);
	public List<ErrorDTO> listBoard(int offset, int rows, String condition, String keyword,String category);
		
	public ErrorDTO readBoard(int boardNum);
	public ErrorDTO preReadBoard(int groupNum, int orderNo, String condition, String keyword);
	public ErrorDTO nextReadBoard(int groupNum, int orderNo, String condition, String keyword);
}
