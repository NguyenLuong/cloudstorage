package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    @Autowired
    CredentialMapper credentialMapper;

    @Autowired
    EncryptionService encryptionService;

    public List<Credential> loadAllCredentialByUserId(int userId) {
        return credentialMapper.loadAllByUserId(userId);
    }

    public boolean insert(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setKey(encodedKey);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));

        int result = credentialMapper.insert(credential);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void update(Credential credential) {
        Credential creBeforeUpdate = credentialMapper.loadByCredentialId(credential.getCredentialId());
        credential.setKey(creBeforeUpdate.getKey());
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), creBeforeUpdate.getKey()));
        credentialMapper.update(credential);
    }

    public boolean delete(int credentialId) {
        int result = credentialMapper.delete(credentialId);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Credential getCreById(int credentialId) {
        Credential credential = credentialMapper.loadByCredentialId(credentialId);
        return credential;
    }
}
