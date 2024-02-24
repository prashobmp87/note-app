package com.pmp.noteapp.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.exception.NoteAppException;
import com.pmp.noteapp.repo.NoteRepo;
import com.pmp.noteapp.utils.NoteTag;
import com.pmp.noteapp.utils.Utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class NoteServiceImpl implements NoteService{

	private NoteRepo noteRepo;
	private  int PAGE_SIZE;

	@Autowired
	public NoteServiceImpl(NoteRepo noteRepo,@Value("${noteapp.pagesize}") int PAGE_SIZE) {
		this.noteRepo=noteRepo;
		this.PAGE_SIZE=PAGE_SIZE;
	}
	
	@Override
	public Note createNote(Note note)  {
		log.info("Inside NoteAppServiceImpl.saveNote:"+note);
		List<Note> existingNotes = noteRepo.findNoteByUserNameAndTitle(note.getUserName(), note.getTitle());
		if(existingNotes!=null && existingNotes.size()>0) {
			throw new NoteAppException("Note already exists with same title");
		}
		log.info("B4 leaving NoteAppServiceImpl.saveNote:");
		return noteRepo.save(note);
	}
	
	
	@Override
	public Note updateNote(Note note)  {
		log.info("Inside NoteAppServiceImpl.updateNote:"+note);
	    List<Note> existingNotes =  noteRepo.findNoteByIdOrUserNameAndTitle(note.getId(),note.getUserName(),note.getTitle());
	    log.info("existingNotes"+existingNotes);
	    if(existingNotes !=null ) {
			if (existingNotes.size()==1) {
			    Note existingNote=existingNotes.get(0);
			    if(Utils.isValid(note.getText())) {
			    	existingNote.setText(note.getText());
			    }
			    if(Utils.isValid(note.getTitle())) {
			    	existingNote.setTitle(note.getTitle());
			    }
			    existingNote.setTags(note.getTags());
			    note = noteRepo.save(existingNote);
			}else if (existingNotes.size()>1){
		    	throw new NoteAppException("Multiple note found with same title.");
		    }
	    }else {
	    	throw new NoteAppException("Multiple note found with same title.");
	    }
		log.info("B4 leaving NoteAppServiceImpl.updateNote:");
	    return note;

	}
	
	
	
	@Override
	public void deleteNote(String id)  {
		log.info("Inside NoteAppServiceImpl.deleteNote:");
		if(noteRepo.existsById(id)) {
			noteRepo.deleteById(id);
		}
		else {
			throw new NoSuchElementException();
		}
		log.info("B4 leaving NoteAppServiceImpl.deleteNote:");
	}
	
	
	public List<Note> getAllNotes() {
		log.info("Inside NoteAppServiceImpl.getAllNotes:");
		return noteRepo.findAll();
	}
	
	public Optional<Note> getNoteById(String id) {
		return noteRepo.findById(id);
	}


	@Override
	public Map<String, Integer> getNoteStatistics(String id) {
		log.info("Inside NoteAppServiceImpl.getNoteStatistics:");
		Optional<Note> noteOptional = getNoteById(id);
		if(noteOptional.isPresent()) {
			Note  note= noteOptional.get();			
			Map<String,Integer> statiscticsMap=Arrays.stream(note.getText().split("\\s+"))
				 .collect(Collectors.groupingBy(Function.identity(), Collectors.collectingAndThen(Collectors.counting(),Long::intValue)));
			
			Map<String,Integer> statSortedMap =statiscticsMap.entrySet().stream().sorted((e1,e2)->e2.getValue()-e1.getValue())
										.collect(Collectors.groupingBy(e->e.getKey(),
												LinkedHashMap::new,
												Collectors.summingInt(e->e.getValue())));		
			return statSortedMap;
		}else {
			throw new NoSuchElementException();
		}
	}
	
	@Override
	public List<Note> listUserNotesByTags(String userName, List<NoteTag> tags,int pageNumber)  {
		log.info("Inside NoteAppServiceImpl.listUserNotesByTags:userName"+userName+":tags:"+tags+":pageNumber"+pageNumber+"PAGE_SIZE"+this.PAGE_SIZE);
		Pageable pageable = PageRequest.of(pageNumber, this.PAGE_SIZE);
		if(tags!=null && !tags.isEmpty()) {
			log.info("list by tags");
			return noteRepo.findByUserNameAndTagsIncludeTitleAndCreatedDateFieldsOrderByCreatedDateDesc(userName,tags,pageable);
		}
	    return noteRepo.findByUserNameWithTitleAndCreatedDateFieldsOrderByCreatedDateDesc(userName,pageable);
	}
	
	
	public List<Note> getNoteByUsernameAndTitle(String userName,String title) {
		log.info("Inside NoteAppServiceImpl.getNoteByTitle:userName"+userName+":title:"+title);
		return noteRepo.findNoteByUserNameAndTitle(userName,title);
	}

	
	
	
}
