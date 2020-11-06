package com.recruit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/bbs_recruit/*")
public class RecruitServlet extends MyUploadServlet{
	private static final long serialVersionUID = 1L;
	String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		//이미지 저장할 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"bbs_recruit";
		
		if(uri.indexOf("list.do")!=-1) {
			list(req,resp);
		}else if (uri.indexOf("created.do")!=-1) {
			createdForm(req,resp);
		}else if (uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req,resp);
		}else if (uri.indexOf("article.do")!=-1) {
			article(req,resp);
		}else if (uri.indexOf("update.do")!=-1) {
			updateForm(req,resp);
		}else if (uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req,resp);
		}else if (uri.indexOf("delete.do")!=-1) {
			delete(req,resp);
		}
		
	}
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RecruitDAO dao = new RecruitDAOImpl();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		
		int dataCount=dao.dataCount();
	
		
		int rows=6;
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
	
	
		
		
		forward(req,resp,"/WEB-INF/views/bbs_recruit/list.jsp");

}
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			req.setAttribute("mode", "created");
			forward(req,resp,"/WEB-INF/views/bbs_recruit/created.jsp");
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		RecruitDAO dao = new RecruitDAOImpl();
		try {
			RecruitDTO dto = new RecruitDTO();
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			String filename=null;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p,pathname);
			if(map != null) {
				filename = map.get("saveFilename");
				dto.setImageFilename(filename);
				
				dao.insertRecruit(dto); // 이미지를 저장하지 않으면 DB에 저장하지 못하도록
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/bbs_recruit/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		RecruitDAO dao = new RecruitDAOImpl();
		String page= req.getParameter("page");
		
		try {
			int num= Integer.parseInt(req.getParameter("num"));
			RecruitDTO dto = dao.readRecruit(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/bbs_recruit/list.do?page="+page);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/bbs_recruit/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/bbs_recruit/list.do?page="+page);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info =(SessionInfo)session.getAttribute("member");
		RecruitDAO dao = new RecruitDAOImpl();
		String page= req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			RecruitDTO dto = dao.readRecruit(num);
			
			if(dto==null || ! info.getUserId().equals(dto.getUserId())) {
				//게시물이 없거나 글을 작성한 사람이 아니면
				resp.sendRedirect(cp+"/bbs_recruit/list.do?page="+page);
				return;
			}
			
			req.setAttribute("page", page);
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");
			forward(req, resp, "/WEB-INF/views/bbs_recruit/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/bbs_recruit/list.do?page="+page);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		RecruitDAO dao = new RecruitDAOImpl();
		RecruitDTO dto = new RecruitDTO();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			//GET방식 접근인 경우
			resp.sendRedirect(cp+"/photo/list.do");
			return;
		}
		String page = req.getParameter("page");
		
		try {
			String imageFilename =req.getParameter("imageFilename");
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subeject"));
			dto.setContent(req.getParameter("content"));
			
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map!=null) {
				//새로운 이미지를 등록한 경우
				String Filename=map.get("saveFilename");
				dto.setImageFilename(Filename);
				//기존 파일 지우기
				//안지우면 서버에 사라짐
				FileManager.doFiledelete(pathname, imageFilename);
				
				
			}else {
				//이미지 파일을 변경하지 않은 경우
				dto.setImageFilename(imageFilename);
			}
			dao.updateRecruit(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/photo/list.do?page="+page);
			
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		RecruitDAO dao = new RecruitDAOImpl();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String page= req.getParameter("page");
		
		try {
			int num =Integer.parseInt(req.getParameter("num"));
			RecruitDTO dto =dao.readRecruit(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/photo/list.do?page="+page);
				return;
			}
			
			if(! dto.getUserId().equals(info.getUserId()) && ! info.getUserId().equals("admin")){
				resp.sendRedirect(cp+"/photo/list.do?page="+page);
				return;
			}
			
			//파일지우기
			FileManager.doFiledelete(pathname, dto.getImageFilename());
			
			dao.deleteRecruit(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/photo/list.do?page="+page);
	}
	
}
