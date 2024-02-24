package com.pmp.noteapp.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.utils.NoteTag;

public interface NoteService {
	
	public Note createNote(Note note) ;
	public Note updateNote(Note note) ;
	public void deleteNote(String id) ;
	public List<Note> getAllNotes() ;
	public Optional<Note> getNoteById(String noteId) ;
	public Map<String,Integer> getNoteStatistics(String id);
	//public List<Note> listNotesByTags(List<NoteTag> tags,int pageNumber);	
	public List<Note> listUserNotesByTags(String userName, List<NoteTag> tags,int pageNumber) ;
	public List<Note> getNoteByUsernameAndTitle(String userName,String title);


}
