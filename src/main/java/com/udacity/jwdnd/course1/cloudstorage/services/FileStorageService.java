package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    @Autowired
    private FileMapper fileMapper;

    public List<File> loadAll(Integer userid) {
        List<File> listFile = new ArrayList<>();
        listFile = fileMapper.getFileWithUserId(userid);
        return listFile;
    }

    public int uploadFile(MultipartFile file, int userId) throws IOException {
        System.out.println("upload file");
        File fileForInsert = new File();
        fileForInsert.setFilename(file.getOriginalFilename());
        fileForInsert.setContenttype(file.getContentType());
        fileForInsert.setFiledata(file.getBytes());
        fileForInsert.setFilesize(String.valueOf(file.getSize()));
        fileForInsert.setUserid(userId);

        return fileMapper.insertFile(fileForInsert);
    }

    public boolean deleteFile(int fileId) {
        int result = fileMapper.deleteFile(fileId);
        if (result == 0) {
            return false;
        } else if (result > 0) {
            return true;
        }

        return false;
    }

    public boolean checkFileIsExist(String fileName, int userid) {
        int result = fileMapper.checkFileIsExist(fileName, userid);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileWithFileId(fileId);
    }
}
