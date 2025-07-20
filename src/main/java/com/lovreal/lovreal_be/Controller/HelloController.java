package com.lovreal.lovreal_be.Controller;

import org.springframework.web.bind.annotation.*;



@RestController
public class HelloController {

    private static class Test {
        public String data;

        Test(String data) {
            this.data = data;
        }
    }
    @PostMapping("/love")
    public Test hello(@RequestParam("id") String id,
                      @RequestParam("password") String password, @RequestParam("gender") String gender) {

        System.out.println(id + password + gender);
        return new Test(id + password + gender);
    }

}