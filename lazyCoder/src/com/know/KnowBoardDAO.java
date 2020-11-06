package com.know;

import java.sql.SQLException;
import java.util.List;


public interface KnowBoardDAO {
	   public int insertKnowBoard(KnowBoardDTO dto) throws SQLException;
	   public int updateKnowBoard(KnowBoardDTO dto) throws SQLException;
	   public int deleteKnowBoard(int num) throws SQLException;
	   
	   public int dataCount();
	   public List<KnowBoardDTO> listKnowBoard(int offset, int rows);
	   public KnowBoardDTO readKnowBoard(int num);
}
