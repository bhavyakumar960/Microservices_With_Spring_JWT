package com.iam.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@Component
public class BaseResponse<X> {

	String message;
	Integer status;
	X data;
	List<X> dataList;

	public <T> ResponseEntity<?> sendResponse(HttpStatus httpStatus, String message, T data, List<T> dataList) {
		BaseResponse<T> response = BaseResponse.<T>builder()
									.status(httpStatus.value())
									.message(message)
									.data(data) // Cast data to the specified class type
									.dataList(dataList)
									.build();
		return new ResponseEntity<>(response, httpStatus);
	}
}
