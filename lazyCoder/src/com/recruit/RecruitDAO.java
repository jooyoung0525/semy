package com.recruit;

import java.sql.SQLException;
import java.util.List;

public interface RecruitDAO {
	public int insertRecruit(RecruitDTO dto) throws SQLException;
	public int updateRecruit(RecruitDTO dto) throws SQLException;
	public int deleteRecruit(int num) throws SQLException;
	
	public int dataCount();
	public List<RecruitDTO> listRecruit(int offset, int rows);
	public RecruitDTO readRecruit(int num);
}
