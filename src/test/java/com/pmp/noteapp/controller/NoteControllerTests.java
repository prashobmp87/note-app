package com.pmp.noteapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmp.noteapp.dto.NoteDTO;
import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.service.NoteService;
import com.pmp.noteapp.utils.NoteAppConstants;
import com.pmp.noteapp.utils.NoteMapper;
import com.pmp.noteapp.utils.NoteTag;



@WebMvcTest(controllers=NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class NoteControllerTests {

	
	@MockBean
    private NoteMapper noteMapper;
    @MockBean
    private NoteService noteService;
    @Autowired
    private ObjectMapper objectMapper;   
    @InjectMocks
    private NoteController noteController;
    @Autowired
    private MockMvc mockMvc;



    
    
    

    @Test
    public void test_SaveNote() throws Exception {

    	NoteDTO noteDTO = NoteDTO.builder()	
				  .title("MongoDB")	
				  .text("Test text")
				  .tags( Arrays.asList(NoteTag.BUSINESS)).build();
        // Perform POST request
        mockMvc.perform(post("/note/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("200"))         
               ;
    }
    
    
    
    
    @Test
    public void test_SaveWithoutTitle_shouldFail() throws Exception {
        // Perform POST request
    	NoteDTO noteDTO = NoteDTO.builder()	
				  .title("")	
				  .text("Test text")
				  .tags( Arrays.asList(NoteTag.BUSINESS)).build();
        ResultActions response =  mockMvc.perform(post("/note/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDTO)));
                
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(MockMvcResultMatchers.jsonPath("$.responseCode", CoreMatchers.is(NoteAppConstants.RESPONSE_CODE_MISSING_PARAM)));
      
    }
    
    
    @Test
    public void test_SaveWithoutText_shouldFail() throws Exception {
        // Perform POST request
    	NoteDTO noteDTO = NoteDTO.builder()	
				  .title("Title")	
				  .text("")
				  .tags( Arrays.asList(NoteTag.BUSINESS)).build();
        ResultActions response =  mockMvc.perform(post("/note/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDTO)));
                
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(MockMvcResultMatchers.jsonPath("$.responseCode", CoreMatchers.is(NoteAppConstants.RESPONSE_CODE_MISSING_PARAM)));
      
    }
    
    
   // @Test
    public void test_listNotes() throws Exception {
        // Perform POST request
    	Note returnNote1= Note.builder().id("123").title("Title1").text("Text1").build();
    	Note returnNote2= Note.builder().id("123").title("Title1").text("Text1").build();

    	List<Note> returnDtos =List.of(returnNote1,returnNote2);
    	when(noteService.listUserNotesByTags("DEMO", null, 0)).thenReturn(returnDtos);
    	
        ResultActions response =  mockMvc.perform(get("/note/list")
                .queryParam("tags","BUSINESS")
                .queryParam("page", "0"));
                
        response.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(returnDtos.size())));
      
    }
    
    @Test
    public void testListUserNotes_ReturnsData() throws Exception {
        // Given
        List<NoteTag> tags = new ArrayList<>();
        int page = 0;
        Note returnNote1= Note.builder().id("123").title("Title1").text("Text1").build();
    	Note returnNote2= Note.builder().id("123").title("Title1").text("Text1").build();

        List<Note> returNotes =List.of(returnNote1,returnNote2);


        when(noteService.listUserNotesByTags(anyString(), anyList(), anyInt())).thenReturn(returNotes);
        when(noteMapper.mapToDto(new Note())).thenReturn(new NoteDTO());

        // When/Then
        mockMvc.perform(get("/note/list")
                .param("tags", "BUSINESS")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(2));
                // Add more assertions based on your implementation
    }
    
    
    @Test
    public void test_update() throws Exception {
        // Perform POST request
    	NoteDTO noteDTO = NoteDTO.builder()	
				  .title("Title")	
				  .text("")
				  .tags( Arrays.asList(NoteTag.BUSINESS)).build();
        ResultActions response =  mockMvc.perform(post("/note/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDTO)));
                
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(MockMvcResultMatchers.jsonPath("$.responseCode", CoreMatchers.is(NoteAppConstants.RESPONSE_CODE_MISSING_PARAM)));
      
    }
}