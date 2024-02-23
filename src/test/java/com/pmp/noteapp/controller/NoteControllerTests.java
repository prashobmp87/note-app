package com.pmp.noteapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmp.noteapp.dto.NoteDTO;
import com.pmp.noteapp.service.NoteService;
import com.pmp.noteapp.utils.NoteMapper;
import com.pmp.noteapp.utils.NoteTag;

import net.minidev.json.JSONObject;


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
    private NoteDTO noteDTO;
    private JSONObject noteJSON;
    private NoteDTO noteDTOWithoutTitle;


    
    
    @BeforeEach
    public void init() {
    	noteDTO = NoteDTO.builder()	
				  .title("Test Title")	
				  .text("Test text")
				  .tags( Arrays.asList(NoteTag.BUSINESS)).build();
    	noteJSON =new JSONObject();
    	noteJSON.put("title","Test Title");
    	noteJSON.put("text","Test text");
    	noteJSON.put("tags","Test Title");
    	
    	noteDTOWithoutTitle = NoteDTO.builder()	
				  .title("")	
				  .text("Test text")
				  .tags( Arrays.asList(NoteTag.BUSINESS)).build();
    	
    	
    }

    @Test
    public void test_SaveNote() throws Exception {

        // Perform POST request
        mockMvc.perform(post("/note/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("200"))         
               ;
    }
    
    @Test
    public void test_SaveNoteWithWrongTags_shouldFail() throws Exception {
        // Perform POST request
        mockMvc.perform(post("/note/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(noteJSON.toJSONString()))
                .andExpect(status().isBadRequest());
    }
    
    
    @Test
    public void test_SaveWithoutTitle_shouldFail() throws Exception {
        // Perform POST request
        mockMvc.perform(post("/note/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(noteJSON.toJSONString()))
                .andExpect(status().isBadRequest());                            
    }
}