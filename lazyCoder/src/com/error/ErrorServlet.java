package com.error;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/error_board/*")
public class ErrorServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		if (info == null) {
			String cp = req.getContextPath();
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		if (uri.indexOf("list.do") != -1) { //에러떠요!
			list(req, resp,"all");
		}else if (uri.indexOf("list_404.do") != -1) { // 404/500 에러
			list(req, resp,"404/500");
		}else if (uri.indexOf("list_Null.do") != -1) { // NullPointer 에러
			list(req, resp,"NullPointer");
		}else if (uri.indexOf("list_Exception.do") != -1) { // Exception 예외
			list(req, resp,"Exception");
		}else if (uri.indexOf("list_Etc.do") != -1) { // 기타 에러
			list(req, resp,"Etc.");
		}else if (uri.indexOf("created.do") != -1) {
			createdForm(req, resp);
		} else if (uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("reply.do") != -1) {
			replyForm(req, resp);
		} else if (uri.indexOf("reply_ok.do") != -1) {
			replySubmit(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}

	}

	protected void list(HttpServletRequest req, HttpServletResponse resp, String category) throws ServletException, IOException {
		// 게시글 리스트
		ErrorDAO dao = new ErrorDAOImpl();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();

		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null) {
			current_page = Integer.parseInt(page);
		}

		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if (condition == null) {
			condition = "all";
			keyword = "";
		}

		if (req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}

		int dataCount;
		if (keyword.length() == 0) {
			dataCount = dao.dataCount(category);
		} else {
			dataCount = dao.dataCount(condition, keyword,category);
		}

		int rows = 9;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page) {
			current_page = total_page;
		}

		int offset = (current_page - 1) * rows;
		if (offset < 0) {
			offset = 0;
		}

		List<ErrorDTO> list;
		if (keyword.length() == 0) {
			list = dao.listBoard(offset, rows ,category);
		} else {
			list = dao.listBoard(offset, rows, condition, keyword, category);
		}

		// 리스트번호 만들기, 글 등록 경과 시간
		Date curDate= new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //new
		int listNum, n = 0;
		long gap;
		
		for (ErrorDTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			try {
				Date date=sdf.parse(dto.getCreated()); //문자열->Date로 변환
				gap=(curDate.getTime() - date.getTime()) / (1000*60*60);
				dto.setGap(gap); //현재 - 등록시간
			} catch (Exception e) {
			}
			
			dto.setCreated(dto.getCreated().substring(0, 10));
			n++;
		}

		String query = "";
		if (keyword.length() != 0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}

		String listUrl = cp + "/error_board/list.do";
		String articleUrl = cp + "/error_board/article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		String paging = util.paging(current_page, total_page, listUrl);

		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("category", category);

		forward(req, resp, "/WEB-INF/views/error_board/list.jsp");
	}


	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기폼
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/error_board/created.jsp");
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 글저장
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();
		String listname = req.getParameter("category").replaceAll("/", "");
		listname+="list.do";
		ErrorDAO dao = new ErrorDAOImpl();

		try {
			ErrorDTO dto = new ErrorDTO();
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setCategory(req.getParameter("category"));

			dao.insertBoard(dto, "created");

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/error_board/"+listname);
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		ErrorDAO dao = new ErrorDAOImpl();
		String cp = req.getContextPath();

		try {
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			String page = req.getParameter("page");

			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");

			String query = "page=" + page;
			if (keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
			}

			// 조회수
			dao.updateHitCount(boardNum);

			// 게시글
			ErrorDTO dto = dao.readBoard(boardNum);
			if (dto == null) {
				resp.sendRedirect(cp + "/error_board/list.do?" + query);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			// 이전/다음 글
			ErrorDTO preReadDto = dao.preReadBoard(dto.getGroupNum(), dto.getOrderNo(), condition, keyword);
			ErrorDTO nextReadDto = dao.nextReadBoard(dto.getGroupNum(), dto.getOrderNo(), condition, keyword);

			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);

			String path = "/WEB-INF/views/error_board/article.jsp";
			forward(req, resp, path);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/error_board/list.do");

	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정폼
		ErrorDAO dao = new ErrorDAOImpl();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if (keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
			}

			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			ErrorDTO dto = dao.readBoard(boardNum);
			if (dto == null || !dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/error_board/list.do?" + query);
				return;
			}

			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
		

			forward(req, resp, "/WEB-INF/views/error_board/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/error_board/list.do?"+ query);

	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정완료
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;

		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/error_board/list.do");
			return;
		}

		ErrorDAO dao = new ErrorDAOImpl();
		try {
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if (keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
			}
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			ErrorDTO dto = dao.readBoard(boardNum);
			dto.setBoardNum(Integer.parseInt(req.getParameter("boardNum")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setCategory(req.getParameter("category"));
			dao.updateBoard(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/error_board/list.do?" + query);
	}

	protected void replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변폼
		ErrorDAO dao = new ErrorDAOImpl();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			ErrorDTO dto = dao.readBoard(boardNum);
			
			if(dto==null || (info.getMemberClass() == 1 && !dto.getUserId().equals(info.getUserId()))) {
				resp.sendRedirect(cp + "/error_board/list.do?page=" + page);
				return;
			}
			
			String s = "[" + dto.getSubject() + "] 에 대한 답변입니다.\n";
			dto.setContent(s);

			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "reply");

			forward(req, resp, "/WEB-INF/views/error_board/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/error_board/list.do?page=" + page);
	}

	protected void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변저장
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/error_board/list.do");
			return;
		}

		String page = req.getParameter("page");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		ErrorDAO dao = new ErrorDAOImpl();

		try {
			
			ErrorDTO dto = new ErrorDTO();
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
			dto.setOrderNo(Integer.parseInt(req.getParameter("orderNo")));
			dto.setDepth(Integer.parseInt(req.getParameter("depth")));
			dto.setParent(Integer.parseInt(req.getParameter("parent")));
			dto.setCategory(req.getParameter("category"));

			dao.insertBoard(dto, "reply");

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/error_board/list.do?page=" + page);

	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;

		ErrorDAO dao = new ErrorDAOImpl();
		try {

			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if (keyword.length() != 0) {
				query += "&condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
			}

			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			ErrorDTO dto = dao.readBoard(boardNum);

			if (dto == null) {
				resp.sendRedirect(cp + "/error_board/list.do?" + query);
				return;
			}

			if (!dto.getUserId().equals(info.getUserId()) && !info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/error_board/list.do?" + query);
				return;
			}

			String userId = info.getUserId();
			dao.deleteBoard(boardNum, userId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/error_board/list.do?" + query);
	}

}
