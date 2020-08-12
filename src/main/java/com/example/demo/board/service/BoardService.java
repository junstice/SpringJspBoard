package com.example.demo.board.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.board.domain.BoardVO;
import com.example.demo.board.domain.FileVO;
import com.example.demo.board.mapper.BoardMapper;

@Service("com.example.demo.board.service.BoardService")
public class BoardService {

	@Resource(name = "com.example.demo.board.mapper.BoardMapper")
	BoardMapper boardMapper;
	
	// 개수
	public int boardCountService() throws Exception {
		return boardMapper.boardCount();
	}
	
	// 목록
	public List<BoardVO> boardListService() throws Exception {
		return boardMapper.boardList();
	}
	
	// 상세
	public BoardVO boardDetailService(int bno) throws Exception {
		return boardMapper.boardDetail(bno);
	}
	
	// 작성
	public void boardInsertService(BoardVO board) throws Exception {
		boardMapper.boardInsert(board);
	}
	
	// 파일 업로드
	public void fileInsertService(FileVO file) throws Exception {
		boardMapper.fileInsert(file);
	}
	
	// 파일 업로드
	public FileVO fileDetailService(int bno) throws Exception {
		return boardMapper.fileDetail(bno);
	}
	
	// 수정
	public void boardUpdateService(BoardVO board) throws Exception {
		boardMapper.boardUpdate(board);
	}
		
	// 삭제
	public void boardDeleteService(int bno) throws Exception {
		boardMapper.boardDelete(bno);
	}

}
