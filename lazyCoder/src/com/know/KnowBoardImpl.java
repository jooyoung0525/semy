package com.know;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class KnowBoardImpl implements KnowBoardDAO  {
	private Connection conn=DBConn.getConnection();
	
	
	@Override
	public int insertKnowBoard(KnowBoardDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="INSERT INTO book(userId, bookName, bookInfo, content, imageFilename, rating, hitCount, register_date)"
					+ "VALUES(?, ?, ?, ?, ?, ?, 0, SYSDATE)";
		
			 pstmt=conn.prepareStatement(sql);
			 pstmt.setString(1, dto.getUserId());
			 pstmt.setString(2, dto.getBookName());
			 pstmt.setString(3, dto.getBookInfo());
	         pstmt.setString(4, dto.getContent());
	         pstmt.setString(5, dto.getImageFilename());
	         pstmt.setInt(6, dto.getRating());
		
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
	public int updateKnowBoard(KnowBoardDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		try {
			sql="UPDATE book SET bookname=?, content=?, bookinfo=?, rating=?, imageFilename=? WHERE num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getBookName());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getBookInfo());
			pstmt.setInt(4, dto.getRating());
			pstmt.setString(5, dto.getImageFilename());
			pstmt.setInt(6, dto.getNum());
			
			
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
	public int deleteKnowBoard(int num) throws SQLException {
		 int result=0;
			PreparedStatement pstmt=null;
			String sql;
			
			try {
				sql="DELETE FROM book WHERE num =?";
				
				pstmt=conn.prepareStatement(sql);
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
	public int dataCount() {
		 int result=0;
	      PreparedStatement pstmt=null;
	      ResultSet rs=null;
	      String sql;
	      try {
	         sql="SELECT COUNT(*) FROM book";
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
	            } catch (Exception e2) {
	            }
	         }
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
	public List<KnowBoardDTO> listKnowBoard(int offset, int rows) {
		List<KnowBoardDTO> list=new ArrayList<KnowBoardDTO>();
	    PreparedStatement pstmt=null;
	    ResultSet rs=null;
	    String sql;
	    
	    try {
			sql="SELECT num, k.userId, m.userName, bookName, bookInfo, imageFilename, rating, memberClass "
					+" FROM book k"
					+" JOIN member1 m ON k.userId= m.userId"
					+ " ORDER BY num DESC "
		            + " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
			  pstmt=conn.prepareStatement(sql);
		      pstmt.setInt(1, offset);
		      pstmt.setInt(2, rows);
		     rs=pstmt.executeQuery();
		     
		     
		     while(rs.next()) {
		    	 KnowBoardDTO dto=new KnowBoardDTO();
		            dto.setNum(rs.getInt("num"));
		            dto.setUserId(rs.getString("userId"));
		            dto.setBookName(rs.getString("bookName"));
		            dto.setBookInfo(rs.getString("bookInfo"));
		            dto.setRating(rs.getInt("rating"));
		            dto.setImageFilename(rs.getString("imageFilename"));
		            dto.setUserName(rs.getString("userName"));
		            dto.setMemberClass(rs.getInt("memberClass"));
		            list.add(dto);
		            }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	         if(rs!=null) {
	            try {
	               rs.close();
	            } catch (Exception e2) {
	            }
	         }
	         if(pstmt!=null) {
	            try {
	               pstmt.close();
	            } catch (Exception e2) {
	            }
	         }
	      }
	      return list;
	}

	@Override
	public KnowBoardDTO readKnowBoard(int num) {
		KnowBoardDTO dto=null;
	      PreparedStatement pstmt=null;
	      ResultSet rs=null;
	      String sql;
	      
	      try {
	    	  sql="SELECT num, k.userId, m.userName, bookName,content,bookInfo,rating, imageFilename, register_date "
	    		  +	" FROM book k"
	    		  +	" JOIN member1 m ON k.userId= m.userId"
	              + " WHERE num = ? ";
	         
	         pstmt=conn.prepareStatement(sql);
	         pstmt.setInt(1, num);
	         rs=pstmt.executeQuery();
	         
	         if(rs.next()) {
	            dto=new KnowBoardDTO();
	            dto.setNum(rs.getInt("num"));
	            dto.setUserId(rs.getString("userId"));
	            dto.setBookName(rs.getString("bookName"));
	            dto.setUserName(rs.getString("userName"));
	            dto.setBookInfo(rs.getString("bookInfo"));
	            dto.setContent(rs.getString("content"));
	            dto.setRating(rs.getInt("rating"));
	            dto.setImageFilename(rs.getString("imageFilename"));
	            dto.setRegister_date(rs.getString("register_date")); 

	            }
	         } catch (SQLException e) {
	            e.printStackTrace();
	         } finally {
	         if(rs!=null) {
	            try {
	               rs.close();
	            } catch (Exception e2) {
	            }
	         }
	         if(pstmt!=null) {
	            try {
	               pstmt.close();
	            } catch (Exception e2) {
	            }
	         }
	      }
	      return dto;
	   }
	}
