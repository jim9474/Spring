package com.oracle.oBootHello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.oBootHello.domain.Emp;

@Controller		// 컨트롤러를 정의
public class HelloController {
	
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);	// 로거를 가져옴

	// Prefix -> templates
	// suffix -> .html
	@RequestMapping("hello")	// /hello 경로에 대한 핸들러매핑을 정의
	public String hello(Model model) {
		System.out.println("hello start...");
		logger.info("start ...");
		model.addAttribute("parameter", "boot start...");	// parameter:model 키, boot start:model 값
		// D/S --> templates/ + hello + .html
		return "hello";		// 템플릿의 파일이름 hello.html
	}
	
	@ResponseBody	// 메소드가 직접 응답의 내용을 만들어서 반환
	@GetMapping("ajaxString")	// /ajaxString 경로에 대한 GET요청 처리
	public String ajaxString(@RequestParam("ajaxName") String aName) {
		System.out.println("HelloController ajaxString aName->"+aName);
		return aName;
	}
	
	@ResponseBody
	@GetMapping("ajaxEmp")
	public Emp ajaxEmp(@RequestParam("empno") String empno,
						@RequestParam("ename") String ename) {
		
		System.out.println("HelloController ajaxEmp empno->"+empno);
		logger.info("ename -> {}", ename);
		Emp emp = new Emp();
		emp.setEmpno(empno);
		emp.setEname(ename);
		return emp;
	}
	
}
