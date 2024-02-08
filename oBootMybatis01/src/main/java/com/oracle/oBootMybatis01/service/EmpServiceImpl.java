package com.oracle.oBootMybatis01.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oracle.oBootMybatis01.dao.EmpDao;
import com.oracle.oBootMybatis01.model.Emp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpServiceImpl implements EmpService {
	
	private final EmpDao ed;
	
	@Override
	public int totalEmp() {
		System.out.println("EmpServiceImpl start total...");
		int totEmpCnt = ed.totalEmp();
		System.out.println("EmpServiceImpl totalEmp totEmpCnt->"+totEmpCnt);
		return totEmpCnt;
	}

	@Override
	public List<Emp> listEmp(Emp emp) {
		List<Emp> empList = null;
		System.out.println("EmpServiceImpl listManager start..");
		empList = ed.listEmp(emp);
		System.out.println("EmpServiceImpl listEmp empList.size()->"+empList.size());
		
		return empList;
	}

	@Override
	public Emp detailEmp(int empno) {
		Emp emp = null;
		System.out.println("EmpServiceImpl detailEmp start..");
		emp = ed.detailEmp(empno);
		System.out.println("EmpServiceImpl detailEmp emp->"+emp);
		return emp;
	}

	@Override
	public int updateEmp(Emp emp) {
		int updateCount = 0;
		System.out.println("EmpServiceImpl updateEmp start..");
		updateCount = ed.updateEmp(emp);
		System.out.println("EmpServiceImpl updateEmp updateCount->"+updateCount);
		return updateCount;
	}

}
