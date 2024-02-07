package com.oracle.oBootJpaApi01.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.oBootJpaApi01.domain.Member;
import com.oracle.oBootJpaApi01.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Controller + ResponseBody
// 사용목적 --> Ajax + RestApi
@RestController
@RequiredArgsConstructor
@Slf4j
public class JpaRestApiController {
	private final MemberService memberService;
	
	// postman ---> Body --> raw---> JSON	 
    //  예시    {	    "name" : "kkk222"	    }
	
	@PostMapping("/restApi/v1/memberSave")
	// @RequestBody : Json(member)으로 온것을 --> Member member Setting
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		System.out.println("JpaRestApiController /api/v1/memberSave member.getId()->"+member.getId());
		log.info("member.getName()-> {}.", member.getName());
		log.info("member.getSal()-> {}.", member.getSal());
		
		Long id = memberService.saveMember(member);
		return new CreateMemberResponse(id);
	}
	
	// 목적  : Entity Member member --> 직접 화면이나 API위한 Setting 금지
	// 예시  : @NotEmpty  -->	@Column(name = "userName")
	@PostMapping("/restApi/v2/memberSave")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest cMember) {
		System.out.println("JpaRestApiController /api/v2/memberSave member.getId()->"+cMember.getName());
		log.info("cMember.getName()-> {}.", cMember.getName());
		log.info("cMember.getSal()-> {}.", cMember.getSal());
		Member member = new Member();
		member.setName(cMember.getName());
		member.setSal(cMember.getSal());
		
		Long id = memberService.saveMember(member);
		return new CreateMemberResponse(id);
	}
	
	@Data
	static class CreateMemberRequest {
		@NotEmpty
		private String name;
		private Long sal;
	}
	
	@Data
	static class CreateMemberResponse {
		private final Long id;
	}
	
	// Bad API
	@GetMapping("/restApi/v1/members")
	public List<Member> membersVer1() {
		System.out.println("JpaRestApiController /restApi/v1/members start..");
		List<Member> listMember = memberService.getListAllMember();
		return listMember;
	}
	
	// Good API Easy Version
	// 목표 : 이름 & 급여 만 전송
	@GetMapping("/restApi/v21/members")
	public Result membersVer21() {
	// public Object membersVer21() {
	// public List<MemberRtnDto> membersVer21() {
		List<Member> findMembers = memberService.getListAllMember();
		
		System.out.println("JpaRestApiController restApi/v21/members findMembers.size()->"+findMembers.size());
		List<MemberRtnDto> resultList = new ArrayList<MemberRtnDto>();
		// List<Member> findMembers를 --> List<MemberRtnDto> resultList 이전
		// 이전 목적 : 반드시 필요한 Data 만 보여준다(외부 노출 최대한 금지)
		for(Member member : findMembers) {
			MemberRtnDto memberRtnDto = new MemberRtnDto(member.getName(), member.getSal());
			System.out.println("restApi/v21/members getName->"+memberRtnDto.getName());
			System.out.println("restApi/v21/members getSal->"+memberRtnDto.getSal());
			resultList.add(memberRtnDto);
		}
		System.out.println("restApi/v21/members resultList.size()->"+resultList.size());
		// return new Result(resultList.size(), resultList);
		return new Result(resultList.size(), resultList);
		// return resultList;
	}
	
	// Good API 림다 Version
	// 목표 : 이름 & 급여 만 전송
		@GetMapping("/restApi/v22/members")
		public Result membersVer22() {
			List<Member> findMembers = memberService.getListAllMember();
			System.out.println("JpaRestApiController /restApi/v22/members findMembers.size()->"+findMembers.size());
			// 자바 8에서 추가한 스트림(Stream)은 람다를 활용할 수 있는 기술 중 하나
			List<MemberRtnDto> memberCollect = findMembers.stream()		// 컬렉션을 스트림으로 변환
												.map(m->new MemberRtnDto(m.getName(), m.getSal()))
												.collect(Collectors.toList());		// 변환된 결과를 리스트로 수집
			System.out.println("restApi/v22/members memberCollect.size()->"+memberCollect.size());
			return new Result(memberCollect.size(), memberCollect);
		
		}
	
	
	// T는 인스턴스를 생성할 때 구체적인 타입으로 변경 --> 유연성
	
	@Data
	@AllArgsConstructor
	class Result<T> {
		private final int totCount;	// 총 인원수 추가
		private final T data;
	}
	
	@Data
	@AllArgsConstructor
	class MemberRtnDto{
		private String name;
		private Long sal;
	}
	
	/*
	 *   수정 API
	 *   PUT 방식을사용했는데, PUT은 전체 업데이트를 할 때 사용
	 *   URI 상에서 '{ }' 로 감싸여있는 부분과 동일한 변수명을 사용하는 방법
	 *   해당 데이터가 있으면 업데이트를 하기에 
	 *   PUT요청이 여러번 실행되어도 해당 데이터는 같은 상태이기에 멱등
	 */
	@PutMapping("/restApi/v21/members/{id}")
	public UpdateMemberResponse updateMemberV21
		(@PathVariable("id") Long id,
		@RequestBody @Valid UpdateMemberRequest uMember) {
		
		memberService.updateMember(id, uMember.getName(), uMember.getSal());
		Member findMember = memberService.findByMember(id);
		return new UpdateMemberResponse(findMember.getId()
										,findMember.getName()
										,findMember.getSal());
	}
	
	@Data
	static class UpdateMemberRequest {
		private String name;
		private Long sal;
	}
	
	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {
		private Long id;
		private String name;
		private Long sal;
		
	}
}
