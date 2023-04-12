package com.example.servertest.main.member.domain.controller;

import com.example.servertest.main.member.global.jwt.JwtAuthenticationFilter;
import com.example.servertest.main.member.global.jwt.TokenProvider;
import com.example.servertest.main.member.domain.entity.Member;
import com.example.servertest.main.member.domain.model.*;
import com.example.servertest.main.member.domain.service.MemberService;
import com.example.servertest.main.member.domain.type.MemberType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	@PostMapping("/signUp")
	public ResponseEntity<RegisterMember.Response> signUp(@RequestBody @Valid RegisterMember.Request request) {

		System.out.println(request.toString());
		return ResponseEntity.ok(memberService.register(request));
	}

	@PostMapping("/signIn")
	public ResponseEntity<TokenDto> signIn(@RequestBody @Valid LoginMember loginMember) {

		Member member = memberService.login(loginMember);
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