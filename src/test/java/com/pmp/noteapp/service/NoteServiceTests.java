package com.pmp.noteapp.service;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.repo.NoteRepo;
import com.pmp.noteapp.utils.NoteTag;


@ExtendWith(MockitoExtension.class)
public class NoteServiceTests {

	
	@Mock
	private NoteRepo noteRepo;
	
	@InjectMocks
	private NoteServiceImpl noteService;
	
	@Test
	public void test_saveNoteTest() {
		
		Note note = Note.builder()
						.title("Test Title")
						.createdDate(LocalDateTime.now())
						.text("Test Text")
						.tags(List.of(NoteTag.BUSINESS))
						.build();
		
		when(noteRepo.save(Mockito.any(Note.class))).thenReturn(note);
		
		noteRepo.save(note);
		



		Assertions.assertThat(note).isNotNull();
		
		
	}
}
