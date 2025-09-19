package com.lovreal_be.Controller;

import com.lovreal_be.DTO.CoupleDate;
import com.lovreal_be.DTO.StoryForm;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.Service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final SessionService sessionService;

    @PostMapping("/input-invite-code")
    public ResponseEntity<?> inputInviteCode(@RequestParam("inviteCode") String inviteCode, HttpServletRequest request) {
        return memberService.beCouple(inviteCode, request);
    }

    @PostMapping("/invite-code")
    public void inviteCode(HttpServletRequest request) {
        memberService.createInviteCode(request);
    }

    @PostMapping("/couple-date")
    public ResponseEntity<?> coupleDate(@RequestBody CoupleDate coupleDate, HttpServletRequest request) {
        return memberService.setBeCoupledDate(coupleDate, request);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        if (sessionService.findMemberIdByRequest(request) == null) {
            return ResponseEntity.status(401).body("로그인해주세요.");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new-story")
    public ResponseEntity<?> newStory(@ModelAttribute StoryForm storyForm, HttpServletRequest request) {
        return memberService.writeNewStory(storyForm, request);
    }
}

