package com.lecture;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	@WebServlet("/bbs_lecture/*")
	public class LectureSurblet extends MyUploadServlet {
		private static final long serialVersionUID = 1L;

		private String pathname;
		
		@Override
		protected void process(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			req.setCharacterEncoding("utf-8");
			String cp = req.getContextPath();
			String uri = req.getRequestURI();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			if(uri.indexOf("list.do") == -1 && info == null ) {
				resp.sendRedirect(cp+"/member/login.do");
				return;
			}
			
			// 파일을 저장할 경로(pathname)
			String root=session.getServletContext().getRealPath("/");
			pathname = root+"uploads"+File.separator+"lecture";			
		// uri에 따른 작업 구분
			if(uri.indexOf("list.do")!=-1) {
				list(req, resp);
			} else if(uri.indexOf("created.do")!=-1) {
				createdForm(req, resp);
			}  else if(uri.indexOf("created_ok.do")!=-1) {
				createdSubmit(req, resp);
			}  else if(uri.indexOf("article.do")!=-1) {
				article(req, resp);
			} else if(uri.indexOf("update.do")!=-1) {
				updateForm(req, resp);
			} else if(uri.indexOf("update_ok.do")!=-1) {
				updateSubmit(req, resp);
			} else if(uri.indexOf("deleteFile.do")!=-1) {
				deleteFile(req, resp);
			} else if(uri.indexOf("delete.do")!=-1) {
				delete(req, resp);
			} else if(uri.indexOf("download.do")!=-1) {
				download(req, resp);
			} else if(uri.indexOf("deleteList.do")!=-1) {
				deleteList(req, resp);
			}
		}

		private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 게시물 리스트
			LectureBoardDAO dao=new LectureBoardDAO();
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
				condition="subject";
				keyword="";
			}
			if(req.getMethod().equalsIgnoreCase("GET")) {
				keyword=URLDecoder.decode(keyword,"utf-8");
			}
			
			// 한페이지 표시할 데이터 개수
	        String numPerPage=req.getParameter("rows");
	        int rows = numPerPage == null ? 10 : Integer.parseInt(numPerPage);
	        
			int dataCount, total_page;
			
			if(keyword.length()!=0) {
				dataCount= dao.dataCount(condition, keyword);
			} else {
				dataCount= dao.dataCount();
			}
			total_page=util.pageCount(rows, dataCount);
			
			if(current_page>total_page)
				current_page=total_page;
			
			int offset=(current_page-1)*rows;
		
			List<LectureBoardDTO> list;
			if(keyword.length()!=0) {
				list= dao.listLecture(offset, rows, condition, keyword);
			} else {
				list= dao.listLecture(offset, rows);
			}

			long gap;
			Date curDate = new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// 리스트 글번호 만들기
			int listNum, n=0;
			for(LectureBoardDTO dto : list){
				listNum=dataCount-(offset+n);
				dto.setListNum(listNum);
				
				try {
					Date date=sdf.parse(dto.getCreated());
					
					// gap = (curDate.getTime() - date.getTime()) /(1000*60*60*24); // 일자
					gap = (curDate.getTime() - date.getTime()) /(1000*60*60); // 시간 
					dto.setGap(gap);
				}catch (Exception e) {
				}
				
				dto.setCreated(dto.getCreated().substring(0, 10));
				n++;
			}
			
			String query="";
			String listUrl;
			String articleUrl;
			
			listUrl=cp+"/bbs_lecture/list.do?rows="+rows;
			articleUrl=cp+"/bbs_lecture/article.do?page=" +current_page+"&rows="+rows;
			if(keyword.length()!=0) {
				query="condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
				
				listUrl += "&"+query;
				articleUrl += "&"+query;
			}
			
			String paging=util.paging(current_page, total_page, listUrl);
			
			// 포워딩 jsp에 넘길 데이터
			req.setAttribute("list", list);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			req.setAttribute("rows", rows);
		
			// JSP로 포워딩
			forward(req, resp, "/WEB-INF/views/bbs_lecture/list.jsp");
		}
		
		private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 글쓰기 폼
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			String cp=req.getContextPath();
			String rows=req.getParameter("rows");
			
			
			req.setAttribute("mode", "created");
			req.setAttribute("rows", rows);
			forward(req, resp, "/WEB-INF/views/bbs_lecture/created.jsp");
		}
		
		private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 글 저장
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
		
			LectureBoardDAO dao=new LectureBoardDAO();
			String cp=req.getContextPath();
			
			String rows=req.getParameter("rows");
			// admin만 글을 등록
			if(! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/bbs_lecture/list.do?rows="+rows);
				return;
			}
			
			try {
				LectureBoardDTO dto=new LectureBoardDTO();
			    dto.setUserId(info.getUserId());
			    
				Part p = req.getPart("upload");
				Map<String, String> map = doFileUpload(p, pathname);
				if(map != null) {
					String saveFilename = map.get("saveFilename");
					String originalFilename = map.get("originalFilename");
					long size = p.getSize();
					dto.setSaveFilename(saveFilename);
			    	dto.setOriginalFilename(originalFilename);
			    	dto.setFileSize(size);
				}
				
			    dao.insertLecture(dto);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect(cp+"/bbs_lecture/list.do?rows="+rows);
		}

		private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 글보기
			LectureBoardDAO dao=new LectureBoardDAO();
			String cp=req.getContextPath();
			
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			String query="page="+page+"&rows="+rows;
			
			try {
				int num=Integer.parseInt(req.getParameter("num"));
				
				String condition=req.getParameter("condition");
				String keyword=req.getParameter("keyword");
				if(condition==null) {
					condition="subject";
					keyword="";
				}
				keyword=URLDecoder.decode(keyword, "utf-8");

				if(keyword.length()!=0) {
					query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
				}
				
				// 조회수
				dao.updateHitCount(num);
				
				// 게시물 가져오기
				LectureBoardDTO dto=dao.readLecture(num);
				if(dto==null) {
					resp.sendRedirect(cp+"/bbs_lecture/list.do?"+query);
					return;
				}
				
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				
				// 이전글/다음글
				LectureBoardDTO preReadDto = dao.preReadLecture(dto.getNum(), condition, keyword);
				LectureBoardDTO nextReadDto = dao.nextReadLecture(dto.getNum(), condition, keyword);
				
				req.setAttribute("dto", dto);
				req.setAttribute("preReadDto", preReadDto);
				req.setAttribute("nextReadDto", nextReadDto);
				req.setAttribute("query", query);
				req.setAttribute("page", page);
				req.setAttribute("rows", rows);
				
				forward(req, resp, "/WEB-INF/views/bbs_lecture/article.jsp");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect(cp+"/bbs_lecture/list.do?"+query);
		}
		
		private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 수정 폼
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			
			LectureBoardDAO dao=new LectureBoardDAO();
			String cp=req.getContextPath();
			
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			try {
				int num=Integer.parseInt(req.getParameter("num"));
				
				LectureBoardDTO dto=dao.readLecture(num);
				if(dto==null) {
					resp.sendRedirect(cp+"/bbs_lecture/list.do?page="+page+"&rows="+rows);
					return;
				}
				
				// 글을 등록한 사람만 수정 가능
				if(! info.getUserId().equals(dto.getUserId())) {
					resp.sendRedirect(cp+"/bbs_lecture/list.do?page="+page+"&rows="+rows);
					return;
				}
				
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("rows", rows);
				
				req.setAttribute("mode", "update");
				
				forward(req, resp, "/WEB-INF/views/bbs_lecture/created.jsp");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
		}

		private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 수정 완료
			LectureBoardDAO dao=new LectureBoardDAO();
			String cp=req.getContextPath();
			
			LectureBoardDTO dto=new LectureBoardDTO();
			
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				resp.sendRedirect(cp+"/bbs_lecture/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			try {
				int num=Integer.parseInt(req.getParameter("num"));
				dto.setNum(num);
				
				dto.setSubject(req.getParameter("subject"));
				dto.setContent(req.getParameter("content"));
				dto.setSaveFilename(req.getParameter("saveFilename"));
				dto.setOriginalFilename(req.getParameter("originalFilename"));
				dto.setFileSize(Long.parseLong(req.getParameter("fileSize")));
				
				Part p = req.getPart("upload");
				Map<String, String> map = doFileUpload(p, pathname);
				if(map != null) {
					if(req.getParameter("saveFilename").length()!=0) {
						// 기존파일 삭제
						FileManager.doFiledelete(pathname, req.getParameter("saveFilename"));
					}

					// 새로운 파일
					String saveFilename = map.get("saveFilename");
					String originalFilename = map.get("originalFilename");
					long size = p.getSize();
					dto.setSaveFilename(saveFilename);
			    	dto.setOriginalFilename(originalFilename);
			    	dto.setFileSize(size);
				}

				dao.updateLecture(dto);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect(cp+"/bbs_lecture/list.do?page="+page+"&rows="+rows);
		}

		private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 수정에서 파일만 삭제
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			
			LectureBoardDAO dao=new LectureBoardDAO();
			String cp=req.getContextPath();
		
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			try {
				int num=Integer.parseInt(req.getParameter("num"));
				LectureBoardDTO dto=dao.readLecture(num);
				if(dto==null) {
					resp.sendRedirect(cp+"/bbs_lecture/list.do?page="+page+"&rows="+rows);
					return;
				}
				
				if(info.getUserId().equals(dto.getUserId())) {
					resp.sendRedirect(cp+"/bbs_lecture/list.do?page="+page+"&rows="+rows);
					return;
				}
				
				// 파일삭제
				FileManager.doFiledelete(pathname, dto.getSaveFilename());
				
				// 파일명과 파일크기 변경
				dto.setOriginalFilename("");
				dto.setSaveFilename("");
				dto.setFileSize(0);
				dao.updateLecture(dto);
				
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("rows", rows);
				
				req.setAttribute("mode", "update");

				forward(req, resp, "/WEB-INF/views/bbs_lecture/created.jsp");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect(cp+"/bbs_lecture/list.do?page="+page+"&rows="+rows);
		}

		private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 삭제
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			
			LectureBoardDAO dao=new LectureBoardDAO();
			String cp=req.getContextPath();
			
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			String query="page="+page+"&rows="+rows;
			
			try {
				int num=Integer.parseInt(req.getParameter("num"));
				String condition=req.getParameter("condition");
				String keyword=req.getParameter("keyword");
				if(condition==null) {
					condition="subject";
					keyword="";
				}
				keyword=URLDecoder.decode(keyword, "utf-8");

				if(keyword.length()!=0) {
					query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
				}

				LectureBoardDTO dto=dao.readLecture(num);
				if(dto==null) {
					resp.sendRedirect(cp+"/bbs_lecture/list.do?"+query);
					return;
				}
				
				// 글을 등록한 사람, admin 만 삭제 가능
				if(! info.getUserId().equals(dto.getUserId()) && ! info.getUserId().equals("admin")) {
					resp.sendRedirect(cp+"/bbs_lecture/list.do?"+query);
					return;
				}
				
				if(dto.getSaveFilename()!=null && dto.getSaveFilename().length()!=0) {
					FileManager.doFiledelete(pathname, dto.getSaveFilename());
				}
				
				dao.deleteLecture(num);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect(cp+"/bbs_lecture/list.do?"+query);
		}

		private void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 파일 다운로드
			LectureBoardDAO dao=new LectureBoardDAO();
			int num=Integer.parseInt(req.getParameter("num"));
			
			LectureBoardDTO dto=dao.readLecture(num);
			boolean b=false;
			if(dto!=null) {
				b = FileManager.doFiledownload(dto.getSaveFilename(),//서버에 저장된파일명
						dto.getOriginalFilename(), pathname, resp); //다운할때 보이는 파일명
			}
			
			if(! b) {
				resp.setContentType("text/html;charset=utf-8");
				PrintWriter out=resp.getWriter();
				out.print("<script>alert('파일다운로드가 실패 했습니다.');history.back();</script>");
			}
		}
		
		private void deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			String cp=req.getContextPath();
			
			if(! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/bbs_lecture/list.do");
				return;
			}
			
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			String query="rows="+rows+"&page="+page;
			
			String condition=req.getParameter("condition");
			String keyword=req.getParameter("keyword");
			
			try {
				if(keyword!=null && keyword.length()!=0) {
					query+="&condition="+condition+"&keyword="+
				               URLEncoder.encode(keyword, "UTF-8");
				}
				
				// 업로드된 파일 지우기
				String []ff=req.getParameterValues("filenames");
				if(ff!=null) {
					for(String f : ff) {
						FileManager.doFiledelete(pathname, f);
					}
				}
				
				// 게시글 지우기
				String []nn=req.getParameterValues("nums");
				int nums[]=null;
				nums=new int[nn.length];
				for(int i=0; i<nn.length; i++)
					nums[i]=Integer.parseInt(nn[i]);
				
				LectureBoardDAO dao=new LectureBoardDAO();
				dao.deleteLectureList(nums);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect(cp+"/bbs_lecture/list.do?"+query);

		}
	}

