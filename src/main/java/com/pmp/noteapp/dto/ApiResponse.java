package com.pmp.noteapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pmp.noteapp.utils.NoteAppConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
	private int responseCode;
	private String responseMsg;
	private T data;
	
	public static ApiResponse<?> success(){
		
		return ApiResponse.builder().responseCode(NoteAppConstants.RESPONSE_CODE_SUCCESS)
									.responseMsg(NoteAppConstants.RESPONSE_MSG_SUCCESS)
									.build();
	}

  public static ApiResponse<?> success(Object data) {
    return ApiResponse.builder().responseCode(NoteAppConstants.RESPONSE_CODE_SUCCESS)
        .responseMsg(NoteAppConstants.RESPONSE_MSG_SUCCESS)
        .data(data).build();

  }
  
  public static ApiResponse<?> noDataFound() {
	    return ApiResponse.builder().responseCode(NoteAppConstants.RESPONSE_CODE_NODATA)
	        .responseMsg(NoteAppConstants.RESPONSE_MSG_NODATA)
	        .build();

	  }
  
  public static ApiResponse<?> response(int responseCode,String responseMessage) {
	    return ApiResponse.builder().responseCode(responseCode)
	        .responseMsg(responseMessage)
	        .build();

	  }
}
