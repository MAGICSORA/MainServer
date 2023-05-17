package com.example.servertest.main.member.controller;

import com.example.servertest.main.crop.entity.Category;
import com.example.servertest.main.crop.repository.CategoryRepository;
import com.example.servertest.main.global.jwtManage.jwt.JwtAuthenticationFilter;
import com.example.servertest.main.global.jwtManage.jwt.TokenProvider;
import com.example.servertest.main.global.jwtManage.model.RefreshApiResponseMessage;
import com.example.servertest.main.global.jwtManage.model.Token;
import com.example.servertest.main.global.jwtManage.service.JwtService;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberException;
import com.example.servertest.main.member.model.*;
import com.example.servertest.main.member.repository.MemberRepository;
import com.example.servertest.main.member.service.MemberService;
import com.example.servertest.main.member.type.MemberType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;
	private final JwtService jwtService;

	private final MemberRepository memberRepository;
	private final CategoryRepository categoryRepository;

	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@RequestBody @Valid RegisterMember.Request request) {

		System.out.println(request.toString());
		ServiceResult result = memberService.register(request);

		Optional<Member> optionalMember = memberRepository.findByEmail(request.getEmail());
		Member member = optionalMember.get();

		categoryRepository.save(Category.builder()
				.userId(member.getId())
				.name("unclassified")
				.regDt(LocalDateTime.now())
				.build());

		return ResponseResult.result(result);
	}

	@PostMapping("/signIn")
	public ResponseEntity<?> signIn(@RequestBody @Valid LoginMember loginMember) {

		Member member;
		try {
			member = memberService.login(loginMember);
		} catch (MemberException e) {
			return ResponseResult.fail(String.valueOf(e.getMemberError()), e.getMessage());
		}

		String email = member.getEmail();
		String role = member.getType().toString();

		Token token = tokenProvider.createAccessToken(email, role);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtAuthenticationFilter.TOKEN_HEADER,
				JwtAuthenticationFilter.TOKEN_PREFIX + token);

		jwtService.login(token);


		return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
	}

	@PostMapping("/withdraw/{memberId}")
	public void withDrawMember(@PathVariable Long memberId,
							   @RequestHeader("Authorization") String token, @RequestBody @Valid WithDrawMember request) {
		memberService.withDrawMember(memberId, token, request);
	}

	@GetMapping("/currentUser")
	public ResponseEntity<?> UserInfo(@RequestHeader("Authorization") String token) {
		return ResponseResult.result(memberService.getMemberInfo(token));
	}

	@PostMapping("/refresh")
	public ResponseEntity<RefreshApiResponseMessage> validateRefreshToken(@RequestBody HashMap<String, String> bodyJson){

		log.info("refresh controller 실행");
		Map<String, String> map = jwtService.validateRefreshToken(bodyJson.get("refreshToken"));
//		System.out.println(map);

		if(map.get("status").equals("402")){
			log.info("RefreshController - Refresh Token이 만료.");
			return new ResponseEntity(map, HttpStatus.UNAUTHORIZED);
		}

		log.info("RefreshController - Refresh Token이 유효.");
		return new ResponseEntity(map, HttpStatus.OK);
	}
}