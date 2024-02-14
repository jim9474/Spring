package com.oracle.oBootMybatis01.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oracle.oBootMybatis01.model.Dept;
import com.oracle.oBootMybatis01.model.DeptVO;
import com.oracle.oBootMybatis01.model.Emp;
import com.oracle.oBootMybatis01.model.EmpDept;
import com.oracle.oBootMybatis01.service.EmpService;
import com.oracle.oBootMybatis01.service.Paging;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmpController {
	private final EmpService es;
	private final JavaMailSender mailSender;
	
	@RequestMapping(value = "listEmp")
	public String empList(Emp emp, Model model) {
		System.out.println("EmpController Start listEmp...");
		// if(emp.getCurrentPage() == null) emp.setPageNum("1");
		// Emp 전체 Count 14
		int totalEmp = es.totalEmp();
		System.out.println("EmpController Start totalEmp->"+totalEmp);
		
		// Paging 작업
		Paging page = new Paging(totalEmp, emp.getCurrentPage());
		// Parameter emp --> Page만 추가 Setting
		emp.setStart(page.getStart());		// 시작시 1
		emp.setEnd(page.getEnd());			// 시작시 10
		
		List<Emp> listEmp = es.listEmp(emp);
		System.out.println("EmpController list listEmp.size()->"+listEmp.size());
		
		
		model.addAttribute("totalEmp", totalEmp);
		model.addAttribute("listEmp", listEmp);
		model.addAttribute("page", page);
		
		return "list";
	}
	
	@GetMapping(value = "detailEmp")
	public String detailEmp(Emp emp1, Model model) {
		System.out.println("EmpController Start detailEmp...");
//		1. EmpService안에 detailEmp method 선언
//		   1) parameter : empno
//		   2) Return      Emp
//
//		2. EmpDao   detailEmp method 선언 
////		                    mapper ID   ,    Parameter
//		emp = session.selectOne("tkEmpSelOne",    empno);
//		System.out.println("emp->"+emp1);
		
		Emp emp = es.detailEmp(emp1.getEmpno());
		model.addAttribute("emp", emp);
		
		return "detailEmp";
	}
	
	@GetMapping(value = "updateFormEmp")
	public String updateFormEmp(Emp emp1, Model model) {
		System.out.println("EmpController Start updateForm...");
		
		Emp emp = es.detailEmp(emp1.getEmpno());
		System.out.println("emp.getEname()->"+emp.getEname());
		System.out.println("emp.getHiredate()->"+emp.getHiredate());
		
		// 문제 
		// 1. DTO  String hiredate
		// 2.View : 단순조회 OK ,JSP에서 input type="date" 문제 발생
		// 3.해결책  : 년월일만 짤라 넣어 주어야 함
		String hiredate = "";
		if (emp.getHiredate() != null) {
			hiredate = emp.getHiredate().substring(0, 10);
			emp.setHiredate(hiredate);
		}
		System.out.println("hiredate->"+hiredate);
		
		model.addAttribute("emp", emp);
		return "updateFormEmp";
	}
	
	@PostMapping(value = "updateEmp")
	public String updateEmp(Emp emp, Model model) {
		log.info("updateEmp start...");
		
//      1. EmpService안에 updateEmp method 선언
//      	1) parameter : Emp
//     		2) Return      updateCount (int)
//
//   	2. EmpDao updateEmp method 선언
////                  		            mapper ID   ,    Parameter
//   		updateCount = session.update("tkEmpUpdate",emp);
		System.out.println("EmpController updateEmp Start..");
		int updateCount = es.updateEmp(emp);
		System.out.println("EmpController updateEmp updateCount"+updateCount);
		
		model.addAttribute("uptCnt", updateCount);
		model.addAttribute("kk3", "Message Test");
		
		
		return "forward:listEmp";			// 파라미터를 데리고 페이지 이동
		// return "redirect:listEmp";		// 단순 페이지 이동
	}
	
	@RequestMapping(value = "writeFormEmp")
	public String writeFormEmp(Model model) {
		System.out.println("EmpController writeFormEmp Start...");
		// 관리자 사번 만 Get
		List<Emp> empList = es.listManager();
		System.out.println("EmpController writeFormEmp empList.size()->"+empList.size());
		model.addAttribute("empMngList", empList);	// emp Manager List
		// 1. Service , DAO --> listManager
		// 2. Mapper -> tkSelectManager
		//    1) Emp Table --> MGR 등록된 정보 Get
		// 부서(코드, 부서명)
		List<Dept> deptList = es.deptSelect();
		model.addAttribute("deptList", deptList);	// dept
		System.out.println("EmpController writeForm deptList.size->"+deptList.size());
		
		return "writeFormEmp";
	}
	
	@PostMapping(value = "writeEmp")
	public String writeEmp(Emp emp, Model model) {
		System.out.println("EmpController Start writeEmp...");
		
		// Service, Dao, Mapper명[insertEmp] 까지 -> insert
		int insertResult = es.insertEmp(emp);
		if(insertResult > 0) return "redirect:listEmp";
		else {
			model.addAttribute("msg","입력 실패 확인해 보세요");
			return "forward:writeFormEmp";
		}
	}
	
	@RequestMapping(value = "writeFormEmp3")
	public String writeFormEmp3(Model model) {
		System.out.println("EmpController writeFormEmp3 Start...");
		// 관리자 사번 만 Get
		List<Emp> empList = es.listManager();
		System.out.println("EmpController writeFormEmp3 empList.size()->"+empList.size());
		model.addAttribute("empMngList", empList);	// emp Manager List
		// 1. Service , DAO --> listManager
		// 2. Mapper -> tkSelectManager
		//    1) Emp Table --> MGR 등록된 정보 Get
		// 부서(코드, 부서명)
		List<Dept> deptList = es.deptSelect();
		model.addAttribute("deptList", deptList);	// dept
		System.out.println("EmpController writeForm3 deptList.size->"+deptList.size());
		
		return "writeFormEmp3";
	}
	
	// Validation시 참조
	@PostMapping(value = "writeEmp3")
	public String writeEmp3(@ModelAttribute("emp") @Valid Emp emp
							, BindingResult result
							, Model model) {
		System.out.println("EmpController Start writeEmp3...");
		
		// Validation 오류시 Result
		if(result.hasErrors()) {
			System.out.println("EmpController writeEmp3 hasErrors...");
			model.addAttribute("msg","BindingResult 입력 실패 확인해 보세요");
			return "forward:writeFormEmp3";
		}
		
		// Service, Dao, Mapper명[insertEmp] 까지 -> insert
		int insertResult = es.insertEmp(emp);
		if(insertResult > 0) return "redirect:listEmp";
		else {
			model.addAttribute("msg","입력 실패 확인해 보세요");
			return "forward:writeFormEmp3";
		}
	}
	
	@GetMapping(value = "confirm")
	public String confirm(Emp emp1, Model model) {
		Emp emp = es.detailEmp(emp1.getEmpno());
		model.addAttribute("empno", emp1.getEmpno());
		if(emp != null) {
			System.out.println("EmpController confirm 중복된 사번..");
			model.addAttribute("msg", "중복된 사번입니다");
			return "forward:writeFormEmp";
		} else {
			System.out.println("EmpController confirm 사용 가능한 사번..");
			model.addAttribute("msg", "사용 가능한 사번입니다");
			return "forward:writeFormEmp";
		}
	}
	
	@RequestMapping(value = "deleteEmp")
	public String deleteEmp(Emp emp, Model model) {
		System.out.println("EmpController Start delete...");
		// name -> Service, dao, mapper
		int result = es.deleteEmp(emp.getEmpno());
		return "redirect:listEmp";
	}
	
	@RequestMapping(value = "listSearch3")
	public String listSearch3(Emp emp, Model model) {
		// Emp 전체 Count 25
		int totalEmp = es.condTotalEmp(emp);
		System.out.println("EmpController listSearch3 totalEmp=>"+totalEmp);
		// Paging 작업
		Paging page = new Paging(totalEmp, emp.getCurrentPage());
		// Parameter emp --> Page만 추가 Setting
		emp.setStart(page.getStart()); 		// 시작시 1
		emp.setEnd(page.getEnd()); 			// 시작시 10
		
		List<Emp> listSearchEmp = es.listSearchEmp(emp);
		System.out.println("EmpController listSearch3 listSearchEmp.size()->"+listSearchEmp.size());
		
		model.addAttribute("totalEmp", totalEmp);
		model.addAttribute("listEmp", listSearchEmp);
		model.addAttribute("page", page);
		
		return "list";
	}
	
	@GetMapping(value = "listEmpDept")
	public String listEmpDept(Model model) {
		System.out.println("EmpController listEmpDept Start...");
		// Service, DAO -> listEmpDept
		// Mapper만 -> tkListEmpDept
		List<EmpDept> listEmpDept = es.listEmpDept();
		model.addAttribute("listEmpDept", listEmpDept);
		
		return "listEmpDept";
	}
	
	@RequestMapping(value = "mailTransport")
	public String mailTransport(HttpServletRequest request, Model model) {
		System.out.println("mailSending...");
		String tomail = "ttaekwang3@naver.com";		// 받는 사람 이메일
		System.out.println(tomail);
		String setfrom = "jim9474@gmail.com";
		String title = "mailTransport 입니다";		// 제목
		try {
			// Mime 전자우편 Internet 표준 Format
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(setfrom); 	// 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(tomail); 		// 받는사람 이메일
			messageHelper.setSubject(title); 	// 메일제목은 생략이 가능하다
			String tempPassword = (int)(Math.random() * 999999) + 1 + "";
			messageHelper.setText("임시 비밀번호입니다 : "+tempPassword); 	// 메일 내용
			System.out.println("임시 비밀번호입니다 : "+tempPassword);
			mailSender.send(message);
			model.addAttribute("check", 1);		// 정상 전달
			// DB Logic 구성
		} catch(Exception e) {
			System.out.println("mailTransport e.getMessage()->"+e.getMessage());
			model.addAttribute("check", 2);		// 메일 전달 실패
		}
		return "mailResult";
	}
	
	// Procedure Test 입력화면
	@RequestMapping(value = "writeDeptIn")
	public String writeDeptIn(Model model) {
		System.out.println("writeDeptIn Start...");
		return "writeDept3";
	}
	
	// Procedure 통한 Dept 입력후 VO 전달
	@PostMapping(value = "writeDept")
	public String writeDept(DeptVO deptVO, Model model) {
		es.insertDept(deptVO);
		if(deptVO == null) {
			System.out.println("deptVO NULL");
		} else {
			System.out.println("deptVO.getOdeptno()->"+deptVO.getOdeptno());
			System.out.println("deptVO.getOdname()->"+deptVO.getOdname());
			System.out.println("deptVO.getOloc()->"+deptVO.getOloc());
			model.addAttribute("msg", "정상 입력되었습니다");
			model.addAttribute("dept", deptVO);
		}
		return "writeDept3";
	}
	
	// Map 적용
	@GetMapping(value = "writeDeptCursor")
	public String writeDeptCursor(Model model) {
		System.out.println("EmpController writeDeptCursor Start...");
		// 부서범위 조회
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("sDeptno", 10);
		map.put("eDeptno", 55);

	    es.selListDept(map);
	    List<Dept> deptLists = (List<Dept>) map.get("dept");
	    for(Dept dept : deptLists) {
	        System.out.println("dept.getDname->"+dept.getDname());
			System.out.println("dept.getLoc->"+dept.getLoc());
	    }
		System.out.println("deptList Size->"+ deptLists.size());
		model.addAttribute("deptList", deptLists);
			
		return "writeDeptCursor";
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
