package com.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.know.KnowBoardDAO;
import com.know.KnowBoardDTO;
import com.know.KnowBoardImpl;
import com.recruit.RecruitDAO;
import com.recruit.RecruitDAOImpl;
import com.recruit.RecruitDTO;
import com.util.MyServlet;

@WebServlet("/main.do")
public class MainServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		if(uri.indexOf("main.do")!=-1) {
			//forward(req, resp, "/WEB-INF/views/main/main.jsp");
			recruitList(req, resp);
			//knowList(req, resp);
		}
	}
	protected void recruitList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RecruitDAO dao = new RecruitDAOImpl();
		KnowBoardDAO dao1=new KnowBoardImpl();
		String cp = req.getContextPath();
		
//		String page = req.getParameter("page");
//		int current_page = 1;
//		if(page!=null) {
//			current_page = Integer.parseInt(page);
//		}
//		
//		int dataCount=dao.dataCount();
//	
//		
//		int rows=3;
//		int total_page=util.pageCount(rows, dataCount);
//		if(current_page>total_page)
//			current_page = total_page;
//		
//		int offset=(current_page-1)*rows;
//		if(offset<0) {
//			offset=0;
//		}
		
		List<RecruitDTO> list;
		List<KnowBoardDTO> list1;
		
		list = dao.listRecruit(0,3);
		list1 = dao1.listKnowBoard(0, 3);
		
		
		
//		String listUrl = cp+"/bbs_recruit/list.do";
		String articleUrl = cp+"/bbs_recruit/article.do?page=1";
		String articleUrl1 = cp+"/bbs_know/article.do?page=1";
//		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("list1", list1);
//		req.setAttribute("dataCount", dataCount);
//		req.setAttribute("total_page", total_page);
//		req.setAttribute("page", current_page);
//		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("articleUrl1", articleUrl1);
	
	
		
		
		forward(req,resp,"/WEB-INF/views/main/main.jsp");

	}
	
	/*
	 * protected void knowList(HttpServletRequest req, HttpServletResponse resp)
	 * throws ServletException, IOException {
	 * 
	 * KnowBoardDAO dao=new KnowBoardImpl(); MyUtil util=new MyUtil(); String
	 * cp=req.getContextPath();
	 * 
	 * String page=req.getParameter("page"); int current_page=1; if(page!=null) {
	 * current_page=Integer.parseInt(page); }
	 * 
	 * int dataCount=dao.dataCount();
	 * 
	 * 
	 * int rows=3; int total_page=util.pageCount(rows, dataCount);
	 * if(current_page>total_page) current_page=total_page;
	 * 
	 * int offset=(current_page-1)*rows;
	 * 
	 * if(offset<0) offset=0;
	 * 
	 * List<KnowBoardDTO> list=dao.listKnowBoard(offset, rows);
	 * 
	 * String listUrl=cp+"/bbs_know/list.do"; String
	 * articleUrl=cp+"/bbs_know/article.do?page="+current_page; String
	 * paging=util.paging(current_page, total_page, listUrl);
	 * 
	 * req.setAttribute("list1", list); req.setAttribute("dataCount", dataCount);
	 * req.setAttribute("total_page", total_page); req.setAttribute("page",
	 * current_page); req.setAttribute("paging", paging);
	 * req.setAttribute("articleUrl1", articleUrl);
	 * 
	 * forward(req, resp, "/WEB-INF/views/main/main.jsp");
	 * 
	 * }
	 */
}