package ggwp.server.guardiango.controller;

import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestController {
    @Autowired
    private final FirebaseService firebaseService;

    public RestController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping("/insertUser")
    public String insertMember(@RequestParam User user) throws Exception{
        return firebaseService.insertUser(user);
    }

    @PostMapping("/getUserDetail")
    @ResponseBody
    public User getMemberDetail(@RequestParam String email) throws Exception{
        return firebaseService.getUserDetail(email);
    }

    @GetMapping("/updateUser")
    public String updateMember(@RequestBody User user) throws Exception{
        return firebaseService.updateUser(user);
    }

    @GetMapping("/deleteUser")
    public String deleteMember(@RequestParam String id) throws Exception{
        return firebaseService.deleteUser(id);
    }
}