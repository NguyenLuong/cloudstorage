package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.Message;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;

@Controller
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private HomeController homeController;

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionService encryptionService;


    @PostMapping("/processCre")
    public String processCredential(Credential credential, RedirectAttributes redirectAttributes, Authentication authentication) {
        redirectAttributes.getFlashAttributes().clear();

        User currentUser = userService.getUser(authentication.getName());
        credential.setUserid(currentUser.getUserId());
        if (credential.getCredentialId() == null) {
            boolean result = credentialService.insert(credential);
            if (result) {
                redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.CRT_CRE_SUC_MSG);
            } else {
                redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME, Message.CRT_CRE_ERR_MSG);
            }
        } else {
            credentialService.update(credential);
            redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.UPD_CRE_SUC_MSG);
        }

        homeController.setCurrentTab("credential");
        return "redirect:/home";
    }

    @GetMapping("/deleteCre/{credentialId:.+}")
    public String deleteCredential(@PathVariable int credentialId, RedirectAttributes redirectAttributes) {
        redirectAttributes.getFlashAttributes().clear();
        boolean result = credentialService.delete(credentialId);
        if (result) {
            redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.DEL_CRE_SUC_MSG);
        } else {
            redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME, Message.DEL_CRE_ERR_MSG);
        }

        homeController.setCurrentTab("credential");
        return "redirect:/home";
    }

    @PostMapping("/editCre")
    @ResponseBody
    public Credential editCredential(@RequestBody Credential credential, RedirectAttributes redirectAttributes) {
        System.out.println("start edit credential");
        redirectAttributes.getFlashAttributes().clear();
        Credential creBeforeEdit = credentialService.getCreById(credential.getCredentialId());
        String decryptedPassword = encryptionService.decryptValue(creBeforeEdit.getPassword(), creBeforeEdit.getKey());

        creBeforeEdit.setPassword(decryptedPassword);

        return creBeforeEdit;
    }
}
