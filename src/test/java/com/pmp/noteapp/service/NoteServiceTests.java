package com.pmp.noteapp.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.exception.NoteAppException;
import com.pmp.noteapp.repo.NoteRepo;
import com.pmp.noteapp.utils.NoteTag;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTests {

	  @Mock
	  private NoteRepo noteRepo;
	
	  @InjectMocks
	  private NoteServiceImpl noteService;
	
	  //@Test
	  public void can_getNote() {
	    //given
	    String id = "12345";
	    //when
	    noteService.getNoteById(id);
	    //then
	    verify(noteRepo).findById(id);
	  }
	
	  @Test
	  public void test_CreateNote_success() {
	    //given
	    Note toSaveNote = Note.builder()
	      .title("Test Title")
	      .createdDate(LocalDateTime.now())
	      .text("Test Text")
	      .tags(List.of(NoteTag.BUSINESS))
	      .build();
	
	    Note savedNote = Note.builder()
	      .id("123")
	      .title("Test Title")
	      .createdDate(LocalDateTime.now())
	      .text("Test Text")
	      .tags(List.of(NoteTag.BUSINESS))
	      .build();
	
	    //
	    when(noteRepo.findNoteByUserNameAndTitle(toSaveNote.getUserName(), toSaveNote.getTitle())).thenReturn(null);
	    when(noteRepo.save(any(Note.class))).thenReturn(savedNote);
	
	    //when
	    savedNote = noteService.createNote(savedNote);
	
	    //then
	
	    Assertions.assertThat(savedNote.getId().equals("123"));
	
	  }
	
	  @Test
	  public void test_CreateNoteWithSameTitle_ThrowsException() {
	    //given
	    Note toSaveNote = Note.builder()
	      .title("Test Title")
	      .createdDate(LocalDateTime.now())
	      .text("Test Text")
	      .tags(List.of(NoteTag.BUSINESS))
	      .build();
	
	    Note savedNote = Note.builder()
	      .id("123")
	      .title("Test Title")
	      .createdDate(LocalDateTime.now())
	      .text("Test Text")
	      .tags(List.of(NoteTag.BUSINESS))
	      .build();
	
	    //
	    when(noteRepo.findNoteByUserNameAndTitle(toSaveNote.getUserName(), toSaveNote.getTitle())).thenReturn(List.of(savedNote));
	
	    assertThatThrownBy(() -> noteService.createNote(toSaveNote))
	      .isInstanceOf(NoteAppException.class)
	      .hasMessageContaining("Note already exists with same title");
	
	    // Verify that the repository method was called
	    verify(noteRepo, times(1)).findNoteByUserNameAndTitle(toSaveNote.getUserName(), toSaveNote.getTitle());
	    // Ensure that save method is not called
	    verify(noteRepo, never()).save(any(Note.class));
	
	  }
	
	  @Test
	  public void test_UpdateNote_sucess() {
	    // Given
	
	    Note input = Note.builder().id("1")
	      .text("New Text")
	      .tags(Arrays.asList(NoteTag.BUSINESS)).build();
	    Note existingNote = Note.builder().id("1")
	      .userName("user1")
	      .title("Note 1")
	      .text("Text")
	      .tags(Arrays.asList(NoteTag.BUSINESS)).build();
	
	    Note updatedNote = Note.builder().id("1")
	      .userName("user1")
	      .title("Note 1")
	      .text("New Text")
	      .tags(Arrays.asList(NoteTag.BUSINESS)).build();
	
	    List < Note > existingNotes = new ArrayList < > ();
	    existingNotes.add(existingNote);
	
	    when(noteRepo.findNoteByIdOrUserNameAndTitle("1", null, null)).thenReturn(List.of(existingNote));
	    when(noteRepo.save(any(Note.class))).thenReturn(updatedNote);
	
	    // When
	    Note result = noteService.updateNote(input);
	
	    verify(noteRepo, times(1)).findNoteByIdOrUserNameAndTitle("1", null, null);
	    verify(noteRepo, times(1)).save(any(Note.class));
	
	    assertEquals("New Text", result.getText());
	  }
	
	  @Test
	  public void test_UpdateNote_ThrowsExceptionWhenMultipleNotesWithSameTitle() {
	
	    Note toSaveNote = Note.builder().id("1")
	      .text("New Text")
	      .tags(Arrays.asList(NoteTag.BUSINESS)).build();
	    Note existingNote = Note.builder().id("1")
	      .userName("user1")
	      .title("Note 1")
	      .text("Text")
	      .tags(Arrays.asList(NoteTag.BUSINESS)).build();
	
	    Note existingNote1 = Note.builder().id("2")
	      .userName("user1")
	      .title("Note 1")
	      .text("Text")
	      .tags(Arrays.asList(NoteTag.BUSINESS)).build();
	
	    when(noteRepo.findNoteByIdOrUserNameAndTitle("1", null, null)).thenReturn(List.of(existingNote, existingNote1));
	

	    assertThatThrownBy(() -> noteService.updateNote(toSaveNote))
	      .isInstanceOf(NoteAppException.class)
	      .hasMessageContaining("Multiple note found with same title.");
	
	    verify(noteRepo, times(1)).findNoteByIdOrUserNameAndTitle(toSaveNote.getId(), toSaveNote.getUserName(), toSaveNote.getTitle());
	    verify(noteRepo, never()).save(any(Note.class));
	
	  }
	
	  @Test
	  public void test_getNoteById() {
	    String id = "123";
	    Note note = Note.builder()
	      .title("Test Title")
	      .createdDate(LocalDateTime.now())
	      .text("Test Text")
	      .tags(List.of(NoteTag.BUSINESS))
	      .build();
	
	    when(noteRepo.findById(id)).thenReturn(Optional.of(note));
	
	    Optional < Note > retrievedNoteOpt = noteService.getNoteById(id);
	
	    Assertions.assertThat(retrievedNoteOpt).isNotNull();
	
	  }

	  
	  @Test
	  public void test_getNoteStat() {
		  //Given
		  Note existingNote= Note.builder().id("123").text("Note note note test test note hi how are you ").build();
		  
		  Map<String, Integer> expectedMap=Map.of("Note",1,"note",3,"test",2,"hi",1,"how",1,"are",1,"you",1);
		  
		  when(noteRepo.findById("123")).thenReturn(Optional.of(existingNote));
		  
		   Map<String, Integer> statSortedMap= noteService.getNoteStatistics("123");
		  
		   Assertions.assertThat(statSortedMap).isEqualTo(expectedMap);

	  }
	  
}