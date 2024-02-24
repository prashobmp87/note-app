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
	
    List<Note> findNoteByIdOrUserNameAndTitle(String id,String userName,String title);

	

	@Query(value = "{ 'tags' : {$in: ?0} }", fields = "{ 'id' : 0 ,'title' : 1, 'createdDate' : 1 }")
	List<Note> findByTagsIncludeTitleAndCreatedDateFields(List<NoteTag> tags);

	
	@Query(value = "{ 'tags' : {$in: ?0} }", fields = "{ 'id' : 1 ,'title' : 1, 'createdDate' : 1 }", sort = "{'createdDate': -1}")
	List<Note> findByTagsIncludeTitleAndCreatedDateFieldsOrderByCreatedDateDesc(List<NoteTag> tags,Pageable pageable);

	
	@Query(value = "{}", fields = "{ 'id' : 1 ,'title' : 1, 'createdDate' : 1}",sort = "{'createdDate': -1}")
    List<Note> findAllWithTitleAndCreatedDateFieldsOrderByCreatedDateDesc(Pageable pageable);

	
	List<Note> findNoteByUserNameAndTitle(String username,String title);
	
	
	@Query(value = "{ 'userName' : ?0, 'tags' : {$in: ?1} }", fields = "{ 'id' : 1 ,'title' : 1, 'createdDate' : 1 }", sort = "{'createdDate': -1}")
	List<Note> findByUserNameAndTagsIncludeTitleAndCreatedDateFieldsOrderByCreatedDateDesc(String userName, List<NoteTag> tags,Pageable pageable);

	
	@Query(value = "{ 'userName' : ?0}", fields = "{ 'id' : 1 ,'title' : 1, 'createdDate' : 1}",sort = "{'createdDate': -1}")
    List<Note> findByUserNameWithTitleAndCreatedDateFieldsOrderByCreatedDateDesc(String userName, Pageable pageable);
	
	
	
	

}
