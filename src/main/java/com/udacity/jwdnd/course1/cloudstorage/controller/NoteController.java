package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.Message;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private HomeController homeController;

    @Autowired
    private UserService userService;

    @PostMapping("/processNote")
    public String processNote(Note note, RedirectAttributes redirectAttributes, Authentication authentication) {
        redirectAttributes.getFlashAttributes().clear();

        User currentUser = userService.getUser(authentication.getName());
        note.setUserid(currentUser.getUserId());
        if (note.getNoteId() == null) {
            boolean result = noteService.insertNote(note);
            if (result) {
                redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.CRT_NOTE_SUC_MSG);
            } else {
                redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME, Message.CRT_NOTE_ERR_MSG);
            }
        } else {
            noteService.updateNote(note);
            redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.UPD_NOTE_SUC_MSG);
        }
        homeController.setCurrentTab("note");
        return "redirect:/home";
    }

    @GetMapping("/deleteNote/{noteId:.+}")
    public String deleteNote(@PathVariable int noteId, RedirectAttributes redirectAttributes) {
        redirectAttributes.getFlashAttributes().clear();
        boolean result = noteService.deleteNote(noteId);
        if (result) {
            redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.DEL_NOTE_SUC_MSG);
        } else {
            redirectAttributes.addFlashAttribute(Message.CRT_NOTE_ERR_MSG, Message.DEL_NOTE_ERR_MSG);
        }

        homeController.setCurrentTab("note");
        return "redirect:/home";
    }
}
