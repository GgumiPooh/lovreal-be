package com.lovreal_be.service;

import com.lovreal_be.DTO.StoryImgForm;
import com.lovreal_be.config.SecurityConfig;
import com.lovreal_be.DTO.CoupleDate;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.DTO.StoryForm;
import com.lovreal_be.domain.*;
import com.lovreal_be.repository.MemberRepository;
import com.lovreal_be.repository.StoryRepository;
import com.lovreal_be.security.JwtTokenProvider;
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
import java.time.temporal.ChronoUnit;
import java.util.*;

//import static com.lovreal_be.Security.AuthCookieFilter.MEMBER_ID;

@Service
@AllArgsConstructor
public class MemberServiceImpl {
    private final String MEMBER_ID = "aa";
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final NaverAuthService naverAuthService;

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
            Gender gender = form.getGender();
            Member member = new Member(nickname, id, encodedPassword, gender, Provider.LOVREAL);
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

    public ResponseEntity<String> memberStatusCheck(Member member) {
        if(member.getPartnerId() == null) {
            return ResponseEntity.status(201).body("커플 맺기");
        }
        else if(member.getCoupleDate() == null) {
            return ResponseEntity.status(202).body("커플이 된 날");
        }
        else {
            return ResponseEntity.ok().build();
        }
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

    public String[] memberAndPartnerName(Member member) {
        Member partner = memberRepository.findById(member.getPartnerId()).orElse(null);
        if(partner == null) {
            return null;
        }
        return new String []{member.getNickname(), partner.getNickname()};
    }

    public void setBeCoupledDay(CoupleDate coupleDate, Member member, HttpServletRequest request, HttpServletResponse response) {

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
            member.setCoupleDate(date);
            memberRepository.save(member);

            response.setContentType("application/json; charset=UTF-8");
            String jsonResponse = String.format(
                    "{\"message\": \"%s\"}",
                    "저장되었습니다."
            );
            response.getWriter().write(jsonResponse);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(500);
        }

    }

    public String[] memberHome(Member member) {
//        String memberId = request.getSession().getAttribute(MEMBER_ID).toString();
        if(member == null) {
            return null;
        }
        Member partner = memberRepository.findById(member.getPartnerId()).orElse(null);
        if(partner == null) {
            return null;
        }
        String gender = "";
        if(Objects.equals(partner.getGender(), Gender.M )) {
            gender = "남자";
        }
        else if(Objects.equals(partner.getGender(), Gender.F)) {
            gender = "여자";
        }
        else {
            gender = "설정해주세요.";
        }
        return new String[]{member.getNickname(), partner.getNickname(), member.getCoupleDate().toString(), gender};
    }

    public void writeNewStory(StoryForm storyForm,HttpServletResponse response,String token, HttpServletRequest request) throws IOException {
        System.out.println("urls = " );
//        Member member = jwtService.getTokenAndFindMember(request);
        Member member = jwtTokenProvider.getMemberFromJWT(token);
        response.setContentType("application/json; charset=UTF-8");
        if(member == null) {
            String jsonResponse = String.format(
                    "{\"message\": \"%s\"}",
                    "로그인해주세요."
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(jsonResponse);
            return;
        }

        List<MultipartFile> fils = storyForm.getImages();
        String content = storyForm.getContent();
        List<String> urls = s3Service.uploadImageAndGetUrl(fils);
        Story story = new Story(member.getId(), content);

        for(String url : urls) {
           story.addImage(new StoryImg(url));
       }
        storyRepository.save(story);
        response.setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = String.format(
                "{\"message\": \"%s\"}",
                "등록완료."
        );
       response.getWriter().write(jsonResponse);
    }

    public List<StoryForm> board(Member member, HttpServletRequest request, HttpServletResponse response) {
        String memberId = member.getId();
        System.out.println("memberId = " + memberId);
        List<StoryForm> body = storyRepository.findByMemberId(memberId)
                .stream().map(story -> new StoryForm(
                        memberId, story.getContent(), story.getStoryImgs().stream().map(
                                imgForm-> new StoryImgForm(imgForm.getImgId(), imgForm.getSrc())).toList())).toList();
//                        story.getStoryImgs().stream().map(StoryImg::getSrc).toList())).toList();
        System.out.println("body = " + body);
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        return body;

    }

    public Long dDayCalculator(Member member) {
        LocalDate coupleDay = member.getCoupleDate();
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(coupleDay, today);
    }

    public String memberProfileImg(Member member) {
        if(member.getProfileImg() == null) {
            return "https://lovreal-files.s3.ap-northeast-2.amazonaws.com/images/%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpg";
        }
        else {
            return member.getProfileImg();
        }
    }
}


