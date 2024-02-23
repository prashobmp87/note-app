package com.pmp.noteapp.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.repo.NoteRepo;
import com.pmp.noteapp.utils.NoteTag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class NoteServiceImpl implements NoteService{

	private NoteRepo noteRepo;
	private static final int PAGE_SIZE=5;
	
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
	
	@Override
	public Note saveNote(Note note)  {
		log.info("Inside NoteAppServiceImpl.saveNote:"+note);
		var existingNote = noteRepo.findNoteById(note.getId());			
		log.info("B4 leaving NoteAppServiceImpl.saveNote:"+existingNote);
		return noteRepo.save(note);
	}
	
	@Override
	public List<Note> listNotesByTags(List<NoteTag> tags,int pageNumber)  {
		log.info("Inside NoteAppServiceImpl.listAllNotes:"+tags);
		Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
		if(tags!=null && !tags.isEmpty()) {
			return noteRepo.findByTagsIncludeTitleAndCreatedDateFieldsOrderByCreatedDateDesc(tags,pageable);
		}
	    return noteRepo.findAllWithTitleAndCreatedDateFieldsOrderByCreatedDateDesc(pageable);
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
}
