package com.example.servertest.main.member.controller;

import com.example.servertest.main.global.jwt.JwtAuthenticationFilter;
import com.example.servertest.main.global.jwt.TokenProvider;
import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberException;
import com.example.servertest.main.member.model.*;
import com.example.servertest.main.member.service.MemberService;
import com.example.servertest.main.member.type.MemberType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@RequestBody @Valid RegisterMember.Request request) {

		System.out.println(request.toString());
		return ResponseResult.result(memberService.register(request));
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
		MemberType role = member.getType();

		String token = tokenProvider.generatedToken(email, role);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtAuthenticationFilter.TOKEN_HEADER,
				JwtAuthenticationFilter.TOKEN_PREFIX + token);

		return new ResponseEntity<>(new TokenDto(token), httpHeaders, HttpStatus.OK);
	}

	@GetMapping("{memberId}")
	public ResponseEntity<MemberInfo> getMemberInfo(@PathVariable Long memberId,
													@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(memberService.getMemberInfo(memberId, token));
	}

	@PostMapping("/withdraw/{memberId}")
	public void withDrawMember(@PathVariable Long memberId,
							   @RequestHeader("Authorization") String token, @RequestBody @Valid WithDrawMember request) {
		memberService.withDrawMember(memberId, token, request);
	}
}