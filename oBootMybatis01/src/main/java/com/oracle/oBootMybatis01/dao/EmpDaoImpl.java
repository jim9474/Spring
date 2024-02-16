package com.oracle.oBootMybatis01.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.oracle.oBootMybatis01.model.Emp;
import com.oracle.oBootMybatis01.model.EmpDept;

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

	@Override
	public List<Emp> listManager() {
		List<Emp> empList = null;
		System.out.println("EmpDaoImpl listManager start..");
		try {
			empList = session.selectList("tkSelectManager");
		} catch(Exception e) {
			System.out.println("EmpDaoImpl e.getMassage()->"+e.getMessage());
		}
		return empList;
	}

	@Override
	public int insertEmp(Emp emp) {
		int insertResult = 0;
		System.out.println("EmpDaoImpl insertEmp Start..");
		try {
			insertResult = session.insert("insertEmp", emp);
		} catch(Exception e) {
			System.out.println("EmpDaoImpl e.getMassage()->"+e.getMessage());
		}
		return insertResult;
	}

	@Override
	public int deleteEmp(int empno) {
		int result = 0;
		System.out.println("EmpDaoImpl deleteEmp Start..");
		System.out.println("EmpDaoImpl deleteEmp empno->"+empno);
		try {
			result = session.delete("deleteEmp", empno);
			System.out.println("EmpDaoImpl deleteEmp result->"+result);
		} catch(Exception e) {
			System.out.println("EmpDaoImpl e.getMassage()->"+e.getMessage());
		}
		return result;
	}

	@Override
	public List<Emp> empSearchList3(Emp emp) {
		List<Emp> empSearchList3 = null;
		System.out.println("EmpDaoImpl empSearchList3 Start...");
		System.out.println("EmpDaoImpl empSearchList3 emp->"+emp);
		try {
			// keyword 검색
			// Naming Rule							Map ID			parameter
			empSearchList3 = session.selectList("tkEmpSearchList3", emp);
		} catch(Exception e) {
			System.out.println("EmpDaoImpl listEmp Exception->"+e.getMessage());
		}
		return empSearchList3;
	}

	@Override
	public int condtotalEmp(Emp emp) {
		int totEmpCount = 0;
		System.out.println("EmpDaoImpl Start condtotal...");
		System.out.println("EmpDaoImpl Start emp->"+emp);
		try {
			totEmpCount = session.selectOne("condtotalEmp", emp);
			System.out.println("EmpDaoImpl totalEmp totEmpCount->"+totEmpCount);
		} catch(Exception e) {
			System.out.println("EmpDaoImpl totalEmp Exception->"+e.getMessage());
		}
		return totEmpCount;
	}

	@Override
	public List<EmpDept> listEmpDept() {
		List<EmpDept> empDept = null;
		System.out.println("EmpDaoImpl listEmpDept Start...");
		try {
			empDept = session.selectList("tkListEmpDept");
			System.out.println("EmpDaoImpl listEmpDept empDept.size()->"+empDept.size());
		} catch(Exception e) {
			System.out.println("EmpDaoImpl delete Exception->"+e.getMessage());
		}
		return empDept;
	}

	@Override
	public String deptName(int deptno) {
		System.out.println("EmpDaoImpl deptName start...");
		String resultStr = "";
		try {
			System.out.println("EmpDaoImpl deptName deptno->"+deptno);
			resultStr = session.selectOne("tkDeptName", deptno);
			System.out.println("EmpDaoImpl deptName resultStr->"+resultStr);
		} catch (Exception e) {
			System.out.println("EmpDaoImpl deptName Exception->"+e.getMessage());
		}
		return resultStr;
	}
	
	
	
	
	
	
	
	
	
	

}
