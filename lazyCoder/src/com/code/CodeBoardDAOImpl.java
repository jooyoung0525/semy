package com.code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class CodeBoardDAOImpl implements CodeBoardDAO {
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertBoard(CodeBoardDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="INSERT INTO code(userId, subject, content, hitCount, register_date,category , url ) " 
				+ " VALUES (?, ?, ?, 0, SYSDATE,?, ?)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getUrl());
			
			result=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		
		return result;
	}

	@Override
	public int updateBoard(CodeBoardDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="UPDATE code SET subject=?, content=?, category=?, url=? WHERE num=? AND userId=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getCategory());
			pstmt.setString(4, dto.getUrl());
			pstmt.setInt(5, dto.getNum());
			pstmt.setString(6, dto.getUserId());
			
			result=pstmt.executeUpdate();
			
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}

	@Override
	public int deleteBoard(int num, String userId) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			if(userId.equals("admin"))
				sql="DELETE FROM code WHERE num=?";
			else
				sql="DELETE FROM code WHERE num=? AND userId=?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			if(! userId.equals("admin")) {
				pstmt.setString(2, userId);
			}
			
			result=pstmt.executeUpdate();
			
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}

	@Override
	public int dataCount(String category) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) cnt FROM code b "
				+" JOIN member1 m ON b.userId = m.userId "
				+ "  WHERE category=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, category);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
	}

	@Override
	public int dataCount(String condition, String keyword, String category) {
		// 검색에서의 데이터 개수
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM code b  "
				 + " JOIN member1 m ON b.userId = m.userId ";
			
			if(condition.equals("register_date")) {
				keyword=keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql+=" WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
			} else if(condition.equals("all")){
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else { // subject, content
				sql+=" WHERE INSTR("+condition+", ?) >= 1";
			}
			sql+="  AND category=?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
				pstmt.setString(3, category);
			}else {
				pstmt.setString(2, category);
			}
			
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
	}

	@Override
	public List<CodeBoardDTO> listBoard(int offset, int rows,String category) {
		List<CodeBoardDTO> list=new ArrayList<CodeBoardDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT num, subject, hitCount, category, b.userId, url, ");
			sb.append("       register_date ");
			sb.append(" FROM code b ");
			sb.append(" JOIN member1 m ON b.userId = m.userId ");
			sb.append(" WHERE category=? ");
			sb.append(" ORDER BY num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, category);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				CodeBoardDTO dto=new CodeBoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setCategory(rs.getString("category"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setUrl(rs.getString("url"));
				dto.setRegister_date(rs.getString("register_date"));
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return list;
	}

	@Override
	public List<CodeBoardDTO> listBoard(int offset, int rows, String condition, String keyword, String category) {
		List<CodeBoardDTO> list=new ArrayList<CodeBoardDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT num, subject, hitCount,category, b.userId, url,  ");
			sb.append("       register_date ");
			sb.append(" FROM code b ");
			sb.append(" JOIN member1 m ON b.userId = m.userId ");
			
			if(condition.equals("register_date")) {
				keyword=keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append("  WHERE TO_CHAR(register_date, 'YYYYMMDD') = ? ");
			} else if(condition.equals("all")) {
				sb.append("  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else {
				sb.append("  WHERE INSTR("+condition+", ?) >= 1 ");
			}
			sb.append("  AND category=? ");
			sb.append(" ORDER BY num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setString(3, category);
				pstmt.setInt(4, offset);
				pstmt.setInt(5, rows);
			} else {
				pstmt.setString(1, keyword);
				pstmt.setString(2, category);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, rows);
			}
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				CodeBoardDTO dto=new CodeBoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCategory(rs.getString("category"));
				dto.setUrl(rs.getString("url"));
				dto.setRegister_date(rs.getString("register_date"));
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return list;
	}
	
	@Override
	public CodeBoardDTO readBoard(int num) {
		CodeBoardDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT num, b.userId, subject, content, hitCount, register_date, category , url "
				+"  FROM code b JOIN member1 m ON b.userId = m.userId WHERE num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto = new CodeBoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setUrl(rs.getString("url"));
				dto.setRegister_date(rs.getString("register_date"));
				dto.setCategory(rs.getString("category"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return dto;
	}

	@Override
	public int updateHitCount(int num) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		// 글보기에서 조회수 증가
		try {
			sql = "UPDATE code SET hitCount=hitCount+1 WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
	}

	@Override
	public CodeBoardDTO preReadBoard(int num, String condition, String keyword) {
    // 이전글
        CodeBoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
        		sb.append("SELECT num, subject FROM code b JOIN member1 m ON b.userId = m.userId ");
                if(condition.equals("all")) {
                	sb.append("  WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
                } else if(condition.equals("register_date")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(register_date, 'YYYYMMDD') = ?) ");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
                }
                sb.append("            AND (num > ? ) ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                   	pstmt.setInt(3, num);
                } else {
                    pstmt.setString(1, keyword);
                   	pstmt.setInt(2, num);
                }
            } else {
                sb.append("SELECT num, subject FROM code ");
                sb.append(" WHERE num > ? ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new CodeBoardDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}
    
        return dto;
    }

	@Override
	public CodeBoardDTO nextReadBoard(int num, String condition, String keyword) {
		// 다음글
        CodeBoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT num, subject FROM code b JOIN member1 m ON b.userId = m.userId ");
                if(condition.equals("all")) {
                	sb.append("  WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
                } else if(condition.equals("register_date")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(register_date, 'YYYYMMDD') = ?) ");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
                }
                sb.append("          AND (num < ? ) ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                   	pstmt.setInt(3, num);
                } else {
                    pstmt.setString(1, keyword);
                   	pstmt.setInt(2, num);
                }
            } else {
                sb.append("SELECT num, subject FROM code ");
                sb.append(" WHERE num < ? ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new CodeBoardDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}

        return dto;
    }

}
