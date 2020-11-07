package com.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.recruit.RecruitDAO;
import com.recruit.RecruitDAOImpl;
import com.recruit.RecruitDTO;
import com.util.MyServlet;
import com.util.MyUtil;

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
		}
	}
	protected void recruitList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RecruitDAO dao = new RecruitDAOImpl();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		
		int dataCount=dao.dataCount();
	
		
		int rows=3;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page = total_page;
		
		int offset=(current_page-1)*rows;
		if(offset<0) {
			offset=0;
		}
		
		List<RecruitDTO> list;
		
		list = dao.listRecruit(offset,rows);
	
		
		
		
		String listUrl = cp+"/bbs_recruit/list.do";
		String articleUrl = cp+"/bbs_recruit/article.do?page="+current_page;
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);
	
	
		
		
		forward(req,resp,"/WEB-INF/views/main/main.jsp");

	}
}
