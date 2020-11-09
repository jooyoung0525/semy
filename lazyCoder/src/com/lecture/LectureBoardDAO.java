package com.lecture;

import java.sql.SQLException;
import java.util.List;

public interface LectureBoardDAO {
	public int insertBoard(LectureBoardDTO dto) throws SQLException;
	public int updateBoard(LectureBoardDTO dto) throws SQLException;
	public int deleteBoard(int num, String userId) throws SQLException;
	
	public int dataCount();
	public int dataCount(String condition, String keyword);
	
	public List<LectureBoardDTO> listLecture(int offset, int rows);
	public List<LectureBoardDTO> listLecture(int offset, int rows, String condition, String keyword);
	
	public int updateHitCount(int num) throws Exception;
	
	public LectureBoardDTO readLecture(int num);
	public LectureBoardDTO preReadLecture(int num, String condition, String keyword);
	public LectureBoardDTO nextReadLecture(int num, String condition, String keyword);
}
