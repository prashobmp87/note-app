package com.pmp.noteapp.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pmp.noteapp.utils.NoteTag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class Note {

	@Id
	private String id;
	private String title;
	private LocalDateTime createdDate;
	private String text;
	private List<NoteTag> tags;
	private String  userName;

	

}
