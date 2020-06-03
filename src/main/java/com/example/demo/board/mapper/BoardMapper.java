package com.example.demo.board.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.board.domain.BoardVO;

/*
 * @Repository: 해당 클래스가 데이터베이스에 접간하는 클래스임을 명시
 * */
@Repository("com.example.demo.board.mapper.BoardMapper")
public interface BoardMapper {

	// 개수
	public int boardCount() throws Exception;
	
	// 목록
	public List<BoardVO> boardList() throws Exception;
	
	// 상세
	public BoardVO boardDetail(int bno) throws Exception;
	
	// 작성
	public void boardInsert(BoardVO board) throws Exception;
	
	// 수정
	public void boardUpdate(BoardVO board) throws Exception;
		
	// 삭제
	public void boardDelete(int bno) throws Exception;
}
