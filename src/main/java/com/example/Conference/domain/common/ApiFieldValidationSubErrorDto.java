package com.example.Conference.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiFieldValidationSubErrorDto extends ApiSubErrorDto {
	@JsonProperty("Object")
	private String object;

	@JsonProperty("Field")
	private String field;

	@JsonProperty("RejectedValue")
	private  String rejectedValue;

	@JsonProperty("Message")
	private String message;
}