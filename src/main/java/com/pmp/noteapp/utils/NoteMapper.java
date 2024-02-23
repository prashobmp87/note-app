package com.pmp.noteapp.utils;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.pmp.noteapp.dto.NoteDTO;
import com.pmp.noteapp.entity.Note;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class NoteMapper {

	
	private ModelMapper modelMapper;
	
	

	public NoteDTO mapToDto(Note note) {
		NoteDTO noteDto = modelMapper.map(note, NoteDTO.class);
	    return noteDto;
	}
	
	public Note mapToEntity(NoteDTO noteDTO) {
	    Note note = modelMapper.map(noteDTO, Note.class);
	    note.setCreatedDate(LocalDateTime.now());
	    note.setUserName(NoteAppConstants.DEMO_USER);
	    log.info("note:"+note);
	    return note;
	}

}
