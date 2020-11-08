package com.lecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class LectureBoardDAO {
private Connection conn=DBConn.getConnection();
	
	public int insertLecture(LectureBoardDTO dto) throws SQLException {
		int result=0;
		StringBuilder sb=new StringBuilder();
		PreparedStatement pstmt=null;
		
		try {
			sb.append("INSERT INTO lecture ");
			sb.append(" (num, userId, subject, content, saveFilename, originalFilename, filesize)");
			sb.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
			
			pstmt=conn.prepareStatement(sb.toString());
			 pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getSaveFilename());
			pstmt.setString(6, dto.getOriginalFilename());
			pstmt.setLong(7, dto.getFileSize());

			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM lecture";
			pstmt=conn.prepareStatement(sql);
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
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}		
		}
		
		return result;
	}
	
	// 검색 에서 전체의 개수
    public int dataCount(String condition, String keyword) {
        int result=0;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	if(condition.equalsIgnoreCase("created")) {
        		keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
        		sql="SELECT NVL(COUNT(*), 0) FROM lecture n JOIN member1 m ON n.userId=m.userId WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ";
        	} else {
        		sql="SELECT NVL(COUNT(*), 0) FROM lecture n JOIN member1 m ON n.userId=m.userId WHERE  INSTR(" + condition + ", ?) >= 1 ";
        	}
        	
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1, keyword);

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
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
        
        return result;
    }
	
    // 게시물 리스트
	public List<LectureBoardDTO> listLecture(int offset, int rows) {
		List<LectureBoardDTO> list=new ArrayList<LectureBoardDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT num, n.userId, userName, subject, saveFilename, ");
			sb.append("       hitCount, created  ");
			sb.append(" FROM lecture n JOIN member1 m ON n.userId=m.userId  ");
			sb.append(" ORDER BY num DESC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				LectureBoardDTO dto=new LectureBoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setSaveFilename(rs.getString("saveFilename"));
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
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return list;
	}
	
	// 검색에서 리스트
    public List<LectureBoardDTO> listLecture(int offset, int rows, String condition, String keyword) {
        List<LectureBoardDTO> list=new ArrayList<LectureBoardDTO>();

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb=new StringBuilder();

        try {
			sb.append("SELECT num, n.userId, userName, subject, saveFilename, ");
			sb.append("       hitCount, created  ");
			sb.append(" FROM lecture n JOIN member1 m ON n.userId=m.userId  ");
			if(condition.equalsIgnoreCase("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ");
			} else {
				sb.append(" WHERE INSTR(" + condition + ", ?) >= 1  ");
			}
			sb.append(" ORDER BY num DESC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
            
			pstmt=conn.prepareStatement(sb.toString());
            
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
            
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
            	LectureBoardDTO dto=new LectureBoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created")); // yyyy-MM-dd HH:mm:ss
                
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
        
        return list;
    }
    
    
	public LectureBoardDTO readLecture(int num) {
		LectureBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		sql = "SELECT num, n.userId, userName, subject, content, saveFilename,originalFilename, filesize, hitCount, created ";
		sql+= "  FROM lecture n JOIN member1 m ON n.userId=m.userId WHERE num = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if( rs.next()) {
				dto = new LectureBoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getLong("filesize"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return dto;
	}
	
    // 이전글
    public LectureBoardDTO preReadLecture(int num, String condition, String keyword) {
    	LectureBoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb=new StringBuilder();

        try {
            if(keyword.length() != 0) {
                sb.append("SELECT num, subject FROM lecture n JOIN member1 m ON n.userId=m.userId  ");
                if(condition.equalsIgnoreCase("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                	sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                } else {
                	sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1)  ");
                }
                sb.append("         AND (num > ? )  ");
                sb.append(" ORDER BY num ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, keyword);
                pstmt.setInt(2, num);
			} else {
                sb.append("SELECT num, subject FROM lecture n JOIN member1 m ON n.userId=m.userId  ");                
                sb.append(" WHERE num > ?  ");
                sb.append(" ORDER BY num ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new LectureBoardDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
    
        return dto;
    }

    // 다음글
    public LectureBoardDTO nextReadLecture(int num, String condition, String keyword) {
    	LectureBoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb=new StringBuilder();

        try {
            if(keyword.length() != 0) {
                sb.append("SELECT num, subject FROM lecture n JOIN member1 m ON n.userId=m.userId  ");
                if(condition.equalsIgnoreCase("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                	sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                } else {
                	sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1)  ");
                }
                sb.append("         AND (num < ? )  ");
                sb.append(" ORDER BY num DESC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, keyword);
                pstmt.setInt(2, num);
			} else {
                sb.append("SELECT num, subject FROM lecture n JOIN member1 m ON n.userId=m.userId  ");
                sb.append(" WHERE num < ?  ");
                sb.append(" ORDER BY num DESC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new LectureBoardDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
    
        return dto;
    }

	public int updateHitCount(int num) throws SQLException {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE lecture SET hitCount=hitCount+1 WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int updateLecture(LectureBoardDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE lecture SET subject=?, content=?, saveFilename=?, originalFilename=?, filesize=? ";
			sql+= " WHERE num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getSaveFilename());
			pstmt.setString(4, dto.getOriginalFilename());
			pstmt.setLong(5, dto.getFileSize());
			pstmt.setInt(6, dto.getNum());
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int deleteLecture(int num) throws SQLException {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="DELETE FROM lecture WHERE num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	public int deleteLectureList(int[] nums) throws SQLException{
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "DELETE FROM lecture WHERE num IN (";
			for(int i=0; i<nums.length; i++) {
				sql += "?,";
			}
			sql = sql.substring(0, sql.length()-1) + ")";
			
			pstmt=conn.prepareStatement(sql);
			for(int i=0; i<nums.length; i++) {
				pstmt.setInt(i+1, nums[i]);
			}
			
			result = pstmt.executeUpdate();
			
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
}
