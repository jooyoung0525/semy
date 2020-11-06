package com.recruit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.util.DBConn;

public class RecruitDAOImpl implements RecruitDAO {
   private Connection conn = DBConn.getConnection();
   
   @Override
   public int insertRecruit(RecruitDTO dto) throws SQLException {
      int result = 0;
      PreparedStatement pstmt = null;
      String sql;
      
      try {
         sql = " INSERT INTO work(subject, content, userId , imageFilename, register_date, hitCount) "
               + " VALUES(?, ?, ?, ?, SYSDATE ,0) ";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1,dto.getSubject());
         pstmt.setString(2,dto.getContent());
         pstmt.setString(3,dto.getUserId());
         pstmt.setString(4,dto.getImageFilename());
         
         result = pstmt.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
         throw e;
      }finally {
         if(pstmt != null) {
            try {
               pstmt.close();
            } catch (Exception e2) {
            }
         }
      }
      return result;
   }

   @Override
   public int updateRecruit(RecruitDTO dto) throws SQLException {
	      int result = 0;
	      PreparedStatement pstmt = null;
	      String sql;
	      
	      try {
	         sql = " UPDATE photo SET subject = ?, content=? ,imageFilename=? WHERE num=? ";
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1,dto.getSubject());
	         pstmt.setString(2,dto.getContent());
	         pstmt.setString(3,dto.getImageFilename());
	         pstmt.setInt(4, dto.getNum());
	         
	         result = pstmt.executeUpdate();
	      } catch (SQLException e) {
	         e.printStackTrace();
	         throw e;
	      }finally {
	         if(pstmt != null) {
	            try {
	               pstmt.close();
	            } catch (Exception e2) {
	            }
	         }
	      }
	      return result;
   }

   @Override
   public int deleteRecruit(int num) throws SQLException {
	      int result = 0;
	      PreparedStatement pstmt = null;
	      String sql;
	      
	      try {
	         sql = " DELETE FROM work WHERE num=?";
	         
	         pstmt = conn.prepareStatement(sql);
	 
	         pstmt.setInt(1, num);
	         
	         result = pstmt.executeUpdate();
	      } catch (SQLException e) {
	         e.printStackTrace();
	         throw e;
	      }finally {
	         if(pstmt != null) {
	            try {
	               pstmt.close();
	            } catch (Exception e2) {
	            }
	         }
	      }
	      return result;
   }

   @Override
   public int dataCount() {
      int result = 0;
      PreparedStatement pstmt = null;
      ResultSet rs =null;
      String sql;
      
      try {
         sql="SELECT COUNT(*) FROM work";
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         if(rs.next()) {
            result = rs.getInt(1);
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }finally {
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
   public List<RecruitDTO> listRecruit(int offset, int rows) {
        List<RecruitDTO> list=new ArrayList<RecruitDTO>();
         PreparedStatement pstmt=null;
         ResultSet rs=null;
         String sql;
         
         try {
            //이름 가져와야해서 조인해야함
            sql="SELECT num, p.userId, userName, subject, imageFilename"
                  + "  FROM work p"
                  + "  JOIN member1 m ON p.userId=m.userId"
                  + "  ORDER BY num DESC"
                  + "  OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
            
            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, rows);
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
               RecruitDTO dto = new RecruitDTO();
               dto.setNum(rs.getInt("num"));
               dto.setUserId(rs.getString("userId"));
               dto.setUserName(rs.getString("userName"));
               dto.setSubject(rs.getString("subject"));
               dto.setImageFilename(rs.getString("imageFilename"));
               
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
   public RecruitDTO readRecruit(int num) {
      RecruitDTO dto = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      String sql;
      
      try {
          sql="SELECT num, p.userId, userName, subject, imageFilename, content, register_date "
                     + "  FROM work p"
                     + "  JOIN member1 m ON p.userId=m.userId"
                     + "  WHERE num = ?";
          pstmt=conn.prepareStatement(sql);
          pstmt.setInt(1, num);
          rs= pstmt.executeQuery();
          
          if(rs.next()) {
             dto= new RecruitDTO();
             dto.setNum(rs.getInt("num"));
             dto.setUserId(rs.getString("userId"));
             dto.setUserName(rs.getString("userName"));
             dto.setSubject(rs.getString("subject"));
             dto.setContent(rs.getString("content"));
             dto.setImageFilename(rs.getString("imageFilename"));
             dto.setCreated(rs.getString("register_date"));
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
           
       return dto;
   }
}