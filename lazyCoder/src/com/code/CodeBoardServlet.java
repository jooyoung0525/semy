package com.code;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyUtil;

@WebServlet("/code/*")
public class CodeBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd=req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { // 로그인이 되어 있지 않으면
			String cp=req.getContextPath();
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		String uri=req.getRequestURI();
		if(uri.indexOf("javascriptlist.do")!=-1) {
			list(req, resp,"javascript");
		} else if(uri.indexOf("clist.do")!=-1) {
			list(req, resp,"c");
		} else if(uri.indexOf("c++list.do")!=-1) {
			list(req, resp,"c++");
		} else if(uri.indexOf("javalist.do")!=-1) {
			list(req, resp,"java");
		} else if(uri.indexOf("html/csslist.do")!=-1) {
			list(req, resp,"html/css");
		} else if(uri.indexOf("list.do")!=-1) {
			list(req, resp,"c");
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
		
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp, String category) throws ServletException, IOException {
		// 글 리스트
		CodeBoardDAO dao=new CodeBoardDAOImpl();
		MyUtil util=new MyUtil();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null) {
			current_page=Integer.parseInt(page);
		}
		
		String condition=req.getParameter("condition");
		String keyword=req.getParameter("keyword");
		if(condition==null) {
			condition="all";
			keyword="";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword, "utf-8");
		}
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount=dao.dataCount(category);
		} else {
			dataCount=dao.dataCount(condition, keyword,category);
		}
		
		int rows=10;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page) 
			current_page=total_page;
		
		int offset=(current_page-1)*rows;
		if(offset<0)
			offset=0;
		
		List<CodeBoardDTO> list;
		if(keyword.length()==0) {
			list=dao.listBoard(offset, rows, category);
		} else {
			list=dao.listBoard(offset, rows, condition, keyword, category);
		}
		
		// 리스트번호 만들기, 글 등록 경과 시간
		Date curDate= new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //new
		int listNum, n=0;
		long gap;
		
		for(CodeBoardDTO dto:list) {
			listNum=dataCount-(offset+n);
			dto.setListNum(listNum);
			
			try {
				Date date=sdf.parse(dto.getRegister_date()); //문자열->Date로 변환
				gap=(curDate.getTime() - date.getTime()) / (1000*60*60);
				dto.setGap(gap); //현재 - 등록시간
			} catch (Exception e) {
			}
			
			dto.setRegister_date(dto.getRegister_date().substring(0, 10));
			
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="
		         +URLEncoder.encode(keyword,"utf-8");
		}
		
		String listUrl=cp+"/code/list.do";
		String articleUrl=cp+"/code/article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl+="?"+query;
			articleUrl+="&"+query;
		}
		String paging=util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("category", category);
		
		// JSP로 포워딩
		String path="/WEB-INF/views/code/list.jsp";
		forward(req, resp, path);
	}

	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		String path="/WEB-INF/views/code/created.jsp";
		req.setAttribute("mode", "created");
		req.setAttribute("category", req.getParameter("category"));
		forward(req, resp, path);
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 등록
		CodeBoardDAO dao=new CodeBoardDAOImpl();
		CodeBoardDTO dto=new CodeBoardDTO();
		String cp=req.getContextPath();
		String listname=req.getParameter("category");
		listname+="list.do";
		
		try {
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");

			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setCategory(req.getParameter("category"));
			dto.setUrl(req.getParameter("url"));
			

			
			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/code/"+listname);
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 보기
		CodeBoardDAO dao=new CodeBoardDAOImpl();
		String cp=req.getContextPath();
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			String page=req.getParameter("page");
			
			String condition=req.getParameter("condition");
			String keyword=req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword=URLDecoder.decode(keyword,"utf-8");
			
			String query="page="+page;
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+
			         URLEncoder.encode(keyword,"utf-8");
			}
			
			// 조회수
			dao.updateHitCount(num);
			
			// 게시글
			CodeBoardDTO dto=dao.readBoard(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/code/list.do?"+query);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			CodeBoardDTO preReadDto=dao.preReadBoard(num, condition, keyword);
			CodeBoardDTO nextReadDto=dao.nextReadBoard(num, condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			req.setAttribute("category", req.getParameter("category"));
			
			String path="/WEB-INF/views/code/article.jsp";
			forward(req, resp, path);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/code/list.do");
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CodeBoardDAO dao=new CodeBoardDAOImpl();
		String cp=req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			
			CodeBoardDTO dto=dao.readBoard(num);
			if(dto==null || ! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/code/list.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			String path="/WEB-INF/views/code/created.jsp";
			forward(req, resp, path);
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/code/list.do?page="+page);
	
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			CodeBoardDAO dao=new CodeBoardDAOImpl();
		   String cp=req.getContextPath();
		   String page=req.getParameter("page");
		   
		   HttpSession session = req.getSession();
		   SessionInfo info=(SessionInfo)session.getAttribute("member");
			
		   
		   try {
			   int num=Integer.parseInt(req.getParameter("num"));
			   CodeBoardDTO dto=dao.readBoard(num);
			   
			   String condition=req.getParameter("condition");
				String keyword=req.getParameter("keyword");
				if(condition==null) {
					condition="subject";
					keyword="";
				}
				keyword=URLDecoder.decode(keyword,"utf-8");
				
				String query="page="+page;
				if(keyword.length()!=0) {
					query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
				 }
				
				//게시글
				if(dto==null) {
					resp.sendRedirect(cp+"/code/list.do?"+query);
					return;
				}
				dto.setContent(dto.getContent().replace("\n", "<br>"));
				
				CodeBoardDTO preReadDto=dao.preReadBoard(num, condition, keyword);
				CodeBoardDTO nextReadDto=dao.nextReadBoard(num, condition, keyword);
				
				req.setAttribute("dto", dto);
				req.setAttribute("preReadDto", preReadDto);
				req.setAttribute("nextReadDto", nextReadDto);
				req.setAttribute("query", query);
				req.setAttribute("page", page);
			   
			   dto.setNum(num);
			   dto.setSubject(req.getParameter("subject"));
			   dto.setUserId(info.getUserId());
			   dto.setContent(req.getParameter("content"));
			   dto.setCategory(req.getParameter("category"));
			   dto.setUrl(req.getParameter("url"));
			   
			   dao.updateBoard(dto);
			   
			   String path="/WEB-INF/views/code/article.jsp"; //WEB-INF는 직접 접근이 불가능하다. 서블릿은 위치파악 불가
			    forward(req, resp, path);  
				return;
			   
		} catch (Exception e) {
			e.printStackTrace();
		}
		   resp.sendRedirect(cp+"/code/list.do?page="+page);
		
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CodeBoardDAO dao=new CodeBoardDAOImpl();
		String cp=req.getContextPath();
		String page=req.getParameter("page");
		String query="page="+page;
		
		try {
			HttpSession session = req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			
			
			int num=Integer.parseInt(req.getParameter("num"));
			dao.deleteBoard(num,info.getUserId());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/code/list.do?"+query);
	
	}
	
}
