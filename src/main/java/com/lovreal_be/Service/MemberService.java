package com.lovreal_be.Service;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.CoupleRequest;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.lovreal_be.Security.AuthCookieFilter.MEMBER_ID;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final CookieService cookieService;

    public ResponseEntity<?> idDuplicateCheck(String id) {
        if (memberRepository.findById(id).isPresent()) {
            System.out.println("fail");
            return ResponseEntity.status(409).body(Map.of("message", "이미 존재하는 아이디입니다."));
        } else {
            System.out.println("success");
            return ResponseEntity.ok(Map.of("message", "사용가능한 아이디입니다."));
        }
    }

    public ResponseEntity<?> signUp(MemberForm form) {
        try {
            String id = form.getId();
            String password = form.getPassword();
            String encodedPassword = securityConfig.encoder().encode(password);
            String gender = form.getGender();
            Member member = new Member(id, encodedPassword, gender);
            memberRepository.save(member);
            return ResponseEntity.status(200).body("회원가입 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("에러");
        }

    }

    public ResponseEntity<?> login(MemberForm form, HttpServletResponse response, HttpServletRequest request) {
        String id = form.getId();
        String password = form.getPassword();

        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            System.out.println(securityConfig.encoder().encode(password) + " " + member.getPassword());
            if(securityConfig.encoder().matches(password, member.getPassword())) {
                cookieService.createCookie(response, member.getId());
                request.getSession().setAttribute(MEMBER_ID, member.getId());
                System.out.println("세션 ID: " + request.getSession().getId());

                if(member.getPartnerId() == null) {
                    return ResponseEntity.status(200).body("로그인 완료 : 커플을 맺어주세요.");
                }
                else {
                    return ResponseEntity.status(201).body("로그인 완료");
                }
            }
        }
        return ResponseEntity.status(401).body("존재하지 않는 회원입니다.");
    }

    public void createInviteCode(Member member) {
            String inviteCode = RandomStringUtils.randomAlphanumeric(8);
            member.setInviteCode(inviteCode);
            memberRepository.save(member);
        }


    public ResponseEntity<?>coupleRequest(String inviteCode, HttpServletRequest request) {
        Member partner = memberRepository.findByInviteCode(inviteCode);
        if(partner != null) {
            String memberId = request.getSession().getAttribute(MEMBER_ID).toString();
            Member me = memberRepository.findById(memberId).orElse(null);
            if(me != null) {
            if(inviteCode.equals(me.getInviteCode())) {
                return ResponseEntity.status(401).body("자신한테는 요청을 보낼 수 없습니다.");
            }
                try {
                CoupleRequest coupleRequest = new CoupleRequest(inviteCode, partner);
                partner.getCoupleRequests().add(coupleRequest);
                memberRepository.save(partner);
                } catch (Exception e) { //만약 중복된 값이 있어 저장이 안된다면
                    System.out.println(e.getMessage());
                    return ResponseEntity.status(401).body("이미 보낸 요청입니다.");
                }
                return ResponseEntity.status(200).body("커플요청을 보냈습니다!");
            }
        }
        return ResponseEntity.status(401).body("코드를 다시 확인해주세요.");
    }
}


