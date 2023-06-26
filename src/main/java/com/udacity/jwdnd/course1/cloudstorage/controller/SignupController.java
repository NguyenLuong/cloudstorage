package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.Message;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
    @Autowired
    private UserService service;

    @GetMapping
    public String signupView() {
        return "signup";
    }

    private Logger logger = LoggerFactory.getLogger(SignupController.class);

    @PostMapping
    public String signupUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {

        System.out.println("start signup");
        String signupError = null;
        boolean isUsernameExist = service.isUsernameAvailable(user.getUsername());
        if (isUsernameExist) {
            model.addAttribute("signupError", Message.SNU_USR_EXS_ERR_MSG);
            return "signup";
        } else {
            int rowAdded = service.createUser(user);
            if (rowAdded <= 0) {
                model.addAttribute("signupError", Message.SNU_SYS_ERR_MSG);
                return "signup";
            }
            redirectAttributes.addFlashAttribute("successMsg", true);
            return "redirect:/login";
        }
    }
}
