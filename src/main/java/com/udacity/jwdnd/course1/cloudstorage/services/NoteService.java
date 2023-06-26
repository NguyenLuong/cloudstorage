package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public List<Note> loadAllNoteByUserId(int userId) {
        return noteMapper.getNoteByUserId(userId);
    }

    public boolean insertNote(Note note) {
        int result = noteMapper.insert(note);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void updateNote(Note note) {
        noteMapper.update(note);
    }

    public boolean deleteNote(int noteId) {
        int result = noteMapper.delete(noteId);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
}
