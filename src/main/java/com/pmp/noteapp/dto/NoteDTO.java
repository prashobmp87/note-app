package com.pmp.noteapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pmp.noteapp.utils.NoteTag;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class NoteDTO {

	private String id;
	@NotBlank
	private String title;
	@NotBlank
	private String text;
	private LocalDateTime createdDate;
	private List<NoteTag> tags;

}
