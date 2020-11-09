package com.bbs_best;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class BoardDAOImpl implements BoardDAO{
	private Connection conn = DBConn.getConnection();

	@Override
	public int insertBoard(BoardDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		try {
			sql="INSERT INTO best(userId,subject,content,hitCount,created) "
					+ " VALUES(?,?,?,0,SYSDATE)";
			
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			
			result=pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		return result;
	}

	@Override
	public List<BoardDTO> listBoard(int offset, int rows) {
		List<BoardDTO> list=new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		StringBuilder sb =new StringBuilder();
		
		try {
			sb.append("SELECT b.num, userName, subject, hitCount, ");
			sb.append("       created ");
			sb.append(" FROM best b ");
			sb.append(" JOIN free f  ON b.userId = f.userId ");
			sb.append( "WHERE hitCount >=3");
			sb.append(" ORDER BY num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				BoardDTO dto=new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
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
	public List<BoardDTO> listBoard(int offset, int rows, String condition, String keyword) {
		List<BoardDTO> list=new ArrayList<BoardDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT num, userName, subject, hitCount, ");
			sb.append("       created ");
			sb.append(" FROM best b ");
			sb.append(" JOIN free f ON b.userId = f.userId ");
		
			if(condition.equals("created")) {
				keyword=keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append("  WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			} else if(condition.equals("all")) {
				sb.append("  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else {
				sb.append("  WHERE INSTR("+condition+", ?) >= 1 ");
			}
			
			sb.append(" ORDER BY num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			pstmt=conn.prepareStatement(sb.toString());
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, rows);
			} else {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, rows);
			}
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				BoardDTO dto=new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
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
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) cnt FROM best b"
					+" JOIN free f  ON b.userId = f.userId";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	@Override
	public int dataCount(String condition, String keyword) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM best b  "
				 + " JOIN free f ON b.userId = f.userId ";
			
			if(condition.equals("created")) {
				keyword=keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql+=" WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
			} else if(condition.equals("all")){
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else { // subject, content, userName
				sql+=" WHERE INSTR("+condition+", ?) >= 1";
			}
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
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
}
