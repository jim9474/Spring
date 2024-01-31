package com.oracle.oBootBoard.command;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.oracle.oBootBoard.dao.BDao;
import com.oracle.oBootBoard.dto.BDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BExecuteCommand {
	private final BDao jdbcDao;
	
	public BExecuteCommand(BDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	public void bListCmd(Model model) {
		// Dao 연결
		ArrayList<BDto> boardDtoList = jdbcDao.boardList();
		System.out.println("bListCommand boardDtoList.size()-->"+boardDtoList.size());
		model.addAttribute("boardList", boardDtoList);
	}

	public void bWriteCmd(Model model) {
//		  1)  model이용 , map 선언
//		  2) request 이용 ->  bName  ,bTitle  , bContent  추출
//		  3) dao  instance 선언
//		  4) write method 이용하여 저장(bName, bTitle, bContent)
		Map<String, Object> map = model.asMap();	// 모델 객체를 맵형태로 변환
		HttpServletRequest request = (HttpServletRequest) map.get("request");	// 모델의 value인 request오브젝트를 뽑아옴
		String bName = request.getParameter("bName");
		String bTitle = request.getParameter("bTitle");
		String bContent = request.getParameter("bContent");
		
		jdbcDao.write(bName, bTitle, bContent);
		
	}

	// HW2
	public void bContentCmd(Model model) {
		// 1. model이를 Map으로 전환
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		// 2. request -> bId Get
		int bId = Integer.parseInt(request.getParameter("bId"));
		// 3. HW3
		BDto board = jdbcDao.contentView(bId);
		
		model.addAttribute("mvc_board", board);
		
	}
}
