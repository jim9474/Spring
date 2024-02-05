package com.oracle.oBootJpa02.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@SequenceGenerator(
		name = "member_seq_gen",					// 객체 Seq
		sequenceName = "member_seq_generate",		// DB Seq
		initialValue = 1,							// 시퀀스의 시작값
		allocationSize = 1							// 시퀀스에서 한 번에 할당하는 범위
		)
@Table(name = "member2")
public class Member {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "member_seq_gen"
			)
	@Column(name = "member_id", precision = 10)
	private Long id;
	@Column(name = "user_name", length = 50)
	private String name;
	private Long sal;
	
	// 관계 설정
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;
	
	// 실제Column X --> Buffer용도
	@Transient
	private String teamname;
	
	@Transient
	private Long teamid;

}
