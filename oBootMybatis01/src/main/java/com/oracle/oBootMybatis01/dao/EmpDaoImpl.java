package com.oracle.oBootMybatis01.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.oracle.oBootMybatis01.model.Emp;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor 
public class EmpDaoImpl implements EmpDao {
	// Mybatis DB 연동
	private final SqlSession session;
	
	@Override
	public int totalEmp() {
		int totEmpCount = 0;
		System.out.println("EmpDaoImpl Start total...");
		
		try {
			totEmpCount = session.selectOne("com.oracle.oBootMybatis01.EmpMapper.empTotal");
			System.out.println("EmpDaoImpl totalEmp totEmpCount->"+totEmpCount);
		} catch(Exception e) {
			System.out.println("EmpDaoImpl totalEmp Exception->"+e.getMessage());
		}
		return totEmpCount;
	}

	@Override
	public List<Emp> listEmp(Emp emp) {
		List<Emp> empList = null;
		System.out.println("EmpDaoImpl listEmp Start...");
		try {
			//								map ID		parameter		
			empList = session.selectList("tkEmpListAll", emp);
			System.out.println("EmpDaoImpl listEmp empList.size()->"+empList.size());
		} catch(Exception e) {
			System.out.println("EmpDaoImpl listEmp e.getMassage()->"+e.getMessage());
		}
		return empList;
	}

	@Override
	public Emp detailEmp(int empno) {
		Emp emp = null;
		System.out.println("EmpDaoImpl detailEmp start...");
		try {
			emp = session.selectOne("tkEmpSelOne", empno);
		} catch(Exception e) {
			System.out.println("EmpDaoImpl emp e.getMassage()->"+e.getMessage());
		}
		
		return emp;
	}

	@Override
	public int updateEmp(Emp emp) {
		int updateCount = 0;
		System.out.println("EmpDaoImpl updateEmp start...");
		try {
			updateCount = session.update("tkEmpUpdate", emp);
		} catch(Exception e) {
			System.out.println("EmpDaoImpl e.getMassage()->"+e.getMessage());
		}
		return updateCount;
	}

}
