package com.oracle.oBootJpa02;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.oBootJpa02.domain.Member;
import com.oracle.oBootJpa02.repository.MemberRepository;
import com.oracle.oBootJpa02.service.MemberService;

//@SpringBootTest : 스프링 부트 띄우고 테스트(이게 없으면 @Autowired 다 실패)
//반복 가능한 테스트 지원, 각각의 테스트를 실행할 때마다 트랜잭션을 시작하고
//테스트가 끝나면 트랜잭션을 강제로 롤백
@SpringBootTest
@Transactional
public class MemberServiceTest {
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	
	@BeforeEach
	public void before1() {
		System.out.println("Test @BeforeEach ...");
	}
	
	@Test
	@Rollback(value = false)
	public void memberSave() {
		// 1. 조건
		Member member = new Member();
		member.setTeamname("고구려3");
		member.setName("개소문");
		
		// 2. 행위
		Member member3 = memberService.join(member);
		
		// 3. 결과
		System.out.println("MemberServiceTest memberSave member.getId()->"+member.getId());
		System.out.println("MemberServiceTest memberSave member3.getId()->"+member3.getId());
	}
	
	@Test
	public void memberFind() {
		// 1. 조건
		// 회원 조건 조회
		Long findId = 1L;
		
		// 2. 행위
		Optional<Member> member = memberService.findByMember(findId);
		
		// 3. 결과
		System.out.println("MemberServiceTest member->"+member);
		
	}
	
	@Test
	public void memberFindAll() {
		// 1. 조건
		// 회원 전체 조회
		
		// 2. 행위
		List<Member> memberList = memberService.getListAllMember();
		
		// 3. 결과
		System.out.println("MemberServiceTest memberList.size()->"+memberList.size());
		
	}
}
