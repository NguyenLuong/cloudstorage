package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private FileStorageService fileService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private UserService userService;

    private String currentTab;

    @PostConstruct
    public void init() {
        currentTab = "file";
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

    @GetMapping
    public String homeView(Model model, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userService.getUser(currentUsername);
        List<File> files = fileService.loadAll(currentUser.getUserId());
        model.addAttribute("files", files);
        List<Note> notes = noteService.loadAllNoteByUserId(currentUser.getUserId());
        model.addAttribute("notes", notes);
        List<Credential> credentials = credentialService.loadAllCredentialByUserId(currentUser.getUserId());
        model.addAttribute("credentials", credentials);

        model.addAttribute("currentTab", currentTab);
        return "home";
    }
}
