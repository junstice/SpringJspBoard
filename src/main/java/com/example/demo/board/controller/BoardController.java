package com.example.demo.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.board.domain.BoardVO;
import com.example.demo.board.domain.FileVO;
import com.example.demo.board.service.BoardService;

@Controller
public class BoardController {

	@Resource(name = "com.example.demo.board.service.BoardService")
	BoardService boardService;
	
	@RequestMapping("/list")
	private String boardList(Model model) throws Exception {
		
		model.addAttribute("list", boardService.boardListService());
		return "list";
	}
	
	@RequestMapping("/detail/{bno}")
	private String boardDetail(@PathVariable int bno, Model model) throws Exception {
		
		model.addAttribute("detail", boardService.boardDetailService(bno));
		model.addAttribute("files", boardService.fileDetailService(bno));
		return "detail";
	}
	
	@RequestMapping("/fileDown/{bno}")
	private void fileDown(@PathVariable int bno, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		FileVO fileVo = boardService.fileDetailService(bno);
		
		try {
			String fileUrl = fileVo.getFileUrl();
			fileUrl += "/";
			String savePath = fileUrl;
			String fileName = fileVo.getFileName();
			
			String oriFileName = fileVo.getFileOriName();
			InputStream in = null;
			OutputStream out = null;
			File file = null;
			boolean skip = false;
			String client = "";
			
			try {
				file = new File(savePath, fileName);
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				skip = true;
				System.out.println("다운로드 대상 파일이 존재하지 않으므로 excpetion catch");
			}
			
			client = request.getHeader("User-Agent");
			
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Description", "JSP Generated Data");
			
			if (!skip) {
				// 구 IE
				if (client.indexOf("MSIE") != -1) {
					response.setHeader("Content-Disposition", 
							"attachment; filename=\"" + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
				} 
				
				// IE 11 이상
				else if (client.indexOf("Trident") != -1) {
					response.setHeader("Content-Disposition", 
							"attachment; filename=\"" + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
				} 

				// 그 외
				else {
					// 한글 파일명 처리
                    response.setHeader("Content-Disposition",
                            "attachment; filename=\"" + new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
                    response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
				}
				response.setHeader("Content-Length", "" + file.length());
                out = response.getOutputStream();
                byte b[] = new byte[(int) file.length()];
                int leng = 0;
                while ((leng = in.read(b)) > 0) {
                	out.write(b, 0, leng);
                }
			} else {
                response.setContentType("text/html;charset=UTF-8");
                System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
            }
            
			in.close();
            out.close();
            
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
		}
	}
	
	@RequestMapping("/insert")
	private String boardInsertForm() {
		return "insert";
	}
	
	@RequestMapping("/insertProc")
	private String boardInsertProc(HttpServletRequest request, @RequestPart MultipartFile files) throws Exception {
		
//		BoardVO board = (BoardVO) request.getParameterMap(); // 최초 동작 확인용 라인
		BoardVO board = new BoardVO();
		FileVO file = new FileVO();
		
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		board.setWriter(request.getParameter("writer"));
		
		if (files.isEmpty()) {
			boardService.boardInsertService(board);
		} 
		
		else {
			String sourceFileName = files.getOriginalFilename();
			String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName);
			File destinationFile;
			String destinationFileName;
			String fileUrl = "C:\\SpringProject\\workspace\\ex_spring_maven_board\\src\\main\\webapp\\WEB-INF\\uploadFiles\\";
			
			do {
				destinationFileName = RandomStringUtils.randomAlphanumeric(32) + sourceFileNameExtension;
				destinationFile = new File(fileUrl + destinationFileName);
			} while (destinationFile.exists());
			
			destinationFile.getParentFile().mkdirs();
			files.transferTo(destinationFile);
			
			boardService.boardInsertService(board);
			System.out.println("bno: " + board.getBno());
			
			file.setBno(board.getBno());
			file.setFileName(destinationFileName);
			file.setFileOriName(sourceFileName);
			file.setFileUrl(fileUrl);
			
			boardService.fileInsertService(file);
		}
		
		return "redirect:/list";
	}
	
	@RequestMapping("/update/{bno}")
	private String boardUpdateForm(@PathVariable int bno, Model model) throws Exception {
		
		model.addAttribute("detail", boardService.boardDetailService(bno));
		return "update";
	}
	
	@RequestMapping("/updateProc")
	private String boardUpdateProc(HttpServletRequest request) throws Exception {
		
//		BoardVO board = (BoardVO) request.getParameterMap();
		BoardVO board = new BoardVO();
		
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		board.setBno(Integer.parseInt(request.getParameter("bno")));		
		
		boardService.boardUpdateService(board);
		
		return "redirect:/detail/" + request.getParameter("bno");
	}
	
	@RequestMapping("/delete/{bno}")
	private String boardDelete(@PathVariable int bno) throws Exception {
		
		boardService.boardDeleteService(bno);
		return "redirect:/list";
	}
	
}
