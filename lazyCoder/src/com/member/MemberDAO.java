package com.member;

import java.sql.SQLException;

public interface MemberDAO {

	public int insertMember(MemberDTO dto) throws SQLException;
	
	public MemberDTO readMember(String userId);
	
	
	
	
	
}
