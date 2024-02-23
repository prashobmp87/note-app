package com.pmp.noteapp.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pmp.noteapp.dto.ApiResponse;
import com.pmp.noteapp.dto.NoteDTO;
import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.service.NoteService;
import com.pmp.noteapp.utils.NoteMapper;
import com.pmp.noteapp.utils.NoteTag;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/note")
@Validated
public class NoteController {

	
	private NoteService noteAppService;
	private NoteMapper noteMapper;
	
	
	@PostMapping("/save")
	public ResponseEntity<?> saveNote(@RequestBody @Valid NoteDTO noteDTO){
		Note note=noteMapper.mapToEntity(noteDTO);
		noteAppService.saveNote(note);
        return ResponseEntity.ok(ApiResponse.success());

	}
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllNotes(){
		log.info("Inside NoteappController.getAllNotes");
		List<Note> notes=noteAppService.getAllNotes();
		if(notes!=null && !notes.isEmpty()) {
			List<NoteDTO> notesDTO=notes.stream().map(n->noteMapper.mapToDto(n)).collect(Collectors.toList());
			return new ResponseEntity<>(ApiResponse.success(notesDTO),HttpStatus.OK);
		}
		return new ResponseEntity<>(ApiResponse.noDataFound(),HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getNote(@PathVariable String id){
		log.info("Inside NoteappController.getAllNotes");
		Optional<Note> note = noteAppService.getNoteById(id);
        if (note.isPresent()) {
            NoteDTO noteDTO = noteMapper.mapToDto(note.get());
            return ResponseEntity.ok(ApiResponse.success(noteDTO));
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.noDataFound());
	}
	
	
	@GetMapping("/list")
	public ResponseEntity<?> listNotes(@RequestParam(required=false ,name="tags") List<NoteTag> tags,
										@RequestParam(required = true) int page){
		log.info("Inside NoteappController.listNotes");
		List<Note> notes=noteAppService.listNotesByTags(tags, page);
		if (notes != null && !notes.isEmpty()) {
            List<NoteDTO> notesDTO = notes.stream()
                                          .map(note->noteMapper.mapToDto(note))
                                          .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(notesDTO));
        } 
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.noDataFound());
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateNote(@PathVariable String id, @RequestBody NoteDTO noteDTO){
		log.info("Inside NoteappController.updateNote id"+id);
		Optional<Note> existingNote = noteAppService.getNoteById(id);
		
		if (existingNote.isPresent()) {
			    Note noteToUpdate=existingNote.get();
			    if(noteDTO.getText()!=null && !noteDTO.getText().isEmpty()) {
			    	noteToUpdate.setText(noteDTO.getText());
			    }
			    if(noteDTO.getTitle()!=null && !noteDTO.getTitle().isEmpty()) {
			    	noteToUpdate.setTitle(noteDTO.getTitle());
			    }
			    noteToUpdate.setTags(noteDTO.getTags());
			    noteAppService.saveNote(noteToUpdate);
			    return ResponseEntity.ok(ApiResponse.success());
	    } 
	   
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.noDataFound());
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?>  deleteNote(@PathVariable String id) {
		log.info("Inside NoteappController.deleteNote"+id);
		noteAppService.deleteNote(id);
        return ResponseEntity.ok(ApiResponse.success());
	}
	
	@GetMapping("/{id}/stat")
	public ResponseEntity<?>  getNoteStatistics(@PathVariable String id) {
		log.info("Inside NoteappController.getNoteStatistics"+id);
		Map<String,Integer> statisticsMap = noteAppService.getNoteStatistics(id);
		return new ResponseEntity<>(ApiResponse.success(statisticsMap),HttpStatus.OK);
	}
	
	
	
	
}
