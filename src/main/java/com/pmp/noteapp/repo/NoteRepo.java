package com.pmp.noteapp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pmp.noteapp.entity.Note;
import com.pmp.noteapp.utils.NoteTag;


public interface NoteRepo extends MongoRepository<Note, String> {

	
	List<Note>  findNoteByTags(List<NoteTag> tags);
	
	List<Note> findNoteByTitle(String title);
	
	Optional<Note> findNoteById(String id);
	
	
	@Query(value = "{ 'tags' : {$in: ?0} }", fields = "{ 'id' : 0 ,'title' : 1, 'createdDate' : 1 }")
	List<Note> findByTagsIncludeTitleAndCreatedDateFields(List<NoteTag> tags);

	
	@Query(value = "{ 'tags' : {$in: ?0} }", fields = "{ 'id' : 1 ,'title' : 1, 'createdDate' : 1 }", sort = "{'createdDate': -1}")
	List<Note> findByTagsIncludeTitleAndCreatedDateFieldsOrderByCreatedDateDesc(List<NoteTag> tags,Pageable pageable);

	
	@Query(value = "{}", fields = "{ 'id' : 1 ,'title' : 1, 'createdDate' : 1}",sort = "{'createdDate': -1}")
    List<Note> findAllWithTitleAndCreatedDateFieldsOrderByCreatedDateDesc(Pageable pageable);

}
