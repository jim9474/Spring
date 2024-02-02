package com.oracle.oBootJpa01.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity		// 엔터티는 논리적 객체임
@Table(name = "member1")	// 물리적 테이블을 만들어줌
@Getter
@Setter
@ToString
public class Member {
	// jpa에서는 엔터티의 변수명과 뷰단의 name=""이 일치하면 자동으로 매핑을해서
	// 겟파라미터없이도 뷰단의 입력값을 갖고와준다
	@Id		// 프라이머리 키로 만들어줌
	private Long id;
	private String name;
	
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		//return super.toString();
//		String returnStr = "";
//		returnStr = "[id:"+this.id+", name:"+this.name+"]";
//		return returnStr;
//	}
	
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
}
