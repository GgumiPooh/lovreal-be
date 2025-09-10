package com.lovreal_be.service;

import com.lovreal_be.config.SecurityConfig;
import com.lovreal_be.DTO.CoupleDate;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.DTO.StoryForm;
import com.lovreal_be.repository.MemberRepository;
import com.lovreal_be.repository.StoryRepository;
import com.lovreal_be.domain.Member;
import com.lovreal_be.domain.StoryContent;
import com.lovreal_be.domain.StoryImg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//import static com.lovreal_be.Security.AuthCookieFilter.MEMBER_ID;

@Service
@AllArgsConstructor
public class MemberServiceImpl {
    private final String MEMBER_ID = "aa";
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final SessionService sessionService;
    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final JwtService jwtService;

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
            String nickname = form.getNickname();
            String id = form.getId();
            String password = form.getPassword();
            String encodedPassword = securityConfig.encoder().encode(password);
            String gender = form.getGender();
            Member member = new Member(nickname, id, encodedPassword, gender);
            memberRepository.save(member);
            return ResponseEntity.status(200).body("회원가입 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("에러");
        }

    }

    public ResponseEntity<String> login(MemberForm form, HttpServletResponse response, HttpServletRequest request) {
        String id = form.getId();
        String password = form.getPassword();

        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            System.out.println(securityConfig.encoder().encode(password) + " " + member.getPassword());
            if(securityConfig.encoder().matches(password, member.getPassword())) {
//                cookieService.createCookie(response, member.getId());
                request.getSession().setAttribute(MEMBER_ID, member.getId());
                System.out.println("세션 ID: " + request.getSession().getId());
                if(member.getPartnerId() == null) {
                    return ResponseEntity.status(200).body("로그인 완료 : 커플을 맺어주세요.");
                }
                else if(member.getCoupleDate() == null) {
                    return ResponseEntity.status(201).body("로그인 완료");
                }
                else return ResponseEntity.status(202).body("로그인 완료");
            }
        }
        return ResponseEntity.status(401).body("존재하지 않는 회원입니다.");
    }

    public ResponseEntity<String> createInviteCode(Member member) {
        if (member.getInviteCode() == null) {
            String inviteCode = RandomStringUtils.randomAlphanumeric(8);
            member.setInviteCode(inviteCode);
            memberRepository.save(member);
            return ResponseEntity.ok().body("초대코드가 생성되었습니다.");
        }
            return ResponseEntity.ok().body("초대코드가 존재합니다.");
    }


    public ResponseEntity<String> beCouple(String inviteCode, Member member) {
            Member partner = memberRepository.findByInviteCode(inviteCode);

            if(partner != null) {
            if(inviteCode.equals(member.getInviteCode())) {
                return ResponseEntity.status(400).body("자신한테는 요청을 보낼 수 없습니다.");
            }
                member.setPartnerId(partner.getId());
                partner.setPartnerId(member.getId());
                memberRepository.save(member);
                memberRepository.save(partner);
                return ResponseEntity.status(200).body("커플이 되었습니다!");
            }
            return ResponseEntity.status(400).body("코드를 다시 확인해주세요.");
    }

    public String[] memberAndPartnerName(HttpServletRequest request) {
        String memberId = sessionService.findMemberIdByRequest(request);
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return null;
        }
        Member partner = memberRepository.findById(member.getPartnerId()).orElse(null);
        if(partner == null) {
            return null;
        }
        return new String []{member.getNickname(), partner.getNickname()};
    }

    public ResponseEntity<String> setBeCoupledDate (CoupleDate coupleDate, HttpServletRequest request) {
        String memberId = request.getSession().getAttribute(MEMBER_ID).toString();
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null) {
            return  ResponseEntity.status(401).body("로그인해주세요.");
        }
        try {
            if(coupleDate.getMonth().hashCode()<10){
                coupleDate.setMonth("0"+coupleDate.getMonth());
            }
            if(coupleDate.getDay().hashCode()<10){
                coupleDate.setDay("0"+coupleDate.getDay());
            }

            String year = coupleDate.getYear().toString();
            String month = coupleDate.getMonth().toString();
            String day = coupleDate.getDay().toString();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate date = LocalDate.parse(year+month+day, formatter);
            System.out.println(date.toString());
            member.setCoupleDate(date);
            memberRepository.save(member);
            return ResponseEntity.status(200).body("저장되었습니다.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(400).body("다시 입력해주세요.");
        }

    }

    public String[] memberHome(HttpServletRequest request) {
        String memberId = sessionService.findMemberIdByRequest(request);
//        String memberId = request.getSession().getAttribute(MEMBER_ID).toString();
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null) {
            return null;
        }
        Member partner = memberRepository.findById(member.getPartnerId()).orElse(null);
        if(partner == null) {
            return null;
        }
        String gender = "";
        if(Objects.equals(partner.getGender(), "male")) {
            gender = "남자";
        }
        else {
            gender = "여자";
        }
        return new String[]{member.getNickname(), partner.getNickname(), member.getCoupleDate().toString(), gender};
    }

    public void writeNewStory(StoryForm storyForm,HttpServletResponse response, HttpServletRequest request) throws IOException {
        Member member = jwtService.getTokenAndFindMember(request);
        response.setContentType("application/json; charset=UTF-8");
        if(member == null) {
            String jsonResponse = String.format(
                    "{\"message\": \"%s\"}",
                    "로그인해주세요."
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(jsonResponse);
        }
        MultipartFile[] images = storyForm.getImages();
        String content = storyForm.getContent();
        List<String> urls = s3Service.uploadImageAndGetUrl(images);
        StoryContent storyContent = new StoryContent(member.getId(), content);

        for(String url : urls) {
           storyContent.addImage(new StoryImg(url));
       }
        storyRepository.save(storyContent);
        response.setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = String.format(
                "{\"message\": \"%s\"}",
                "등록완료."
        );
       response.getWriter().write(jsonResponse);
    }

    public ResponseEntity<?> board(HttpServletRequest request) {
        String memberId = sessionService.findMemberIdByRequest(request);
        if(memberId == null) {
            return null;
        }

        List<StoryContent> body = storyRepository.findByMemberId(memberId)
                .stream()
                .map(sc -> new StoryContent(
                        sc.getMemberId(),
                        sc.getContent(),
                        sc.getStoryImgs().stream()
                                .map(img -> new StoryImg(img.getSrc()))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(body);
//        return storyRepository.findByMemberId(memberId);

    }
}


