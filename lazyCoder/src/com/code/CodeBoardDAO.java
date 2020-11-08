package com.code;

import java.sql.SQLException;
import java.util.List;

public interface CodeBoardDAO  {
   public int insertBoard(CodeBoardDTO dto) throws SQLException;
   public int updateBoard(CodeBoardDTO dto) throws SQLException;
   public int deleteBoard(int num, String userId) throws SQLException;
   
   public int dataCount(String category);
   public int dataCount(String condition, String keyword, String category);
   
   public List<CodeBoardDTO> listBoard(int offset, int rows,String category);
   public List<CodeBoardDTO> listBoard(int offset, int rows, String condition, String keyword, String category);
   
   public int updateHitCount(int num) throws SQLException;
   public CodeBoardDTO readBoard(int num);
   public CodeBoardDTO preReadBoard(int num, String condition, String keyword);
   public CodeBoardDTO nextReadBoard(int num, String condition, String keyword);
}