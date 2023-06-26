package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.Message;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HomeController homeController;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, RedirectAttributes redirectAttributes, Authentication authentication) {
        redirectAttributes.getFlashAttributes().clear();
        int userId = userService.getUser(authentication.getName()).getUserId();
        if (file.getOriginalFilename() == "") {
            redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME,Message.NO_FILE_SELECT_MSG);
            homeController.setCurrentTab("file");
            return "redirect:/home";
        } else if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME, Message.FILE_EMPTY_MSG);
            homeController.setCurrentTab("file");
            return "redirect:/home";
        } else {
            String uploadFilename = file.getOriginalFilename();
            boolean existed = fileStorageService.checkFileIsExist(uploadFilename, userId);
            if (existed) {
                redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME, Message.DUPL_FILE_MSG);
                homeController.setCurrentTab("file");
                return "redirect:/home";
            }
        }
        try {
            fileStorageService.uploadFile(file, userId);
        } catch (IOException e) {
             redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME, Message.SYS_ERR_MSG);
        }

        redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.UPD_SUC_MSG);
        homeController.setCurrentTab("file");
        return "redirect:/home";
    }

    @GetMapping("/deleteFile/{fileId:.+}")
    public String deleteFile(@PathVariable int fileId, RedirectAttributes redirectAttributes) {
        redirectAttributes.getFlashAttributes().clear();
        boolean existed = fileStorageService.deleteFile(fileId);

        if (existed) {
            redirectAttributes.addFlashAttribute(Message.PROC_SUC_ATTR_NAME, Message.DEL_SUC_MSG);
        } else {
            redirectAttributes.addFlashAttribute(Message.PROC_ERR_ATTR_NAME, Message.FILE_NO_EXST_MSG);
        }

        homeController.setCurrentTab("file");
        return "redirect:/home";
    }

    @GetMapping("/downloadFile/{fileId:.+}")
    public void downloadFile(@PathVariable int fileId, HttpServletResponse httpServletResponse) {

        File file = fileStorageService.getFileById(fileId);

        httpServletResponse.setContentType(file.getContenttype());
        httpServletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getFilename() + "\""));
        InputStream inputStream = new ByteArrayInputStream(file.getFiledata());
        try {
            FileCopyUtils.copy(inputStream, httpServletResponse.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
