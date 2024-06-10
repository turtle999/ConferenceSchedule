package com.example.Conference.domain.common;


import com.example.Conference.helper.Enums;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorDto implements Serializable {
	@JsonProperty("Status")
	@Builder.Default
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	@JsonProperty("Timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ssa")
	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();

	@JsonProperty("Message")
	@Builder.Default
	private String message = "Undefined error";

	@JsonProperty("ApiErrorCode")
	@Builder.Default
	private Enums.ApiErrorCodes errorCode = Enums.ApiErrorCodes.INTERNAL_SERVER_ERROR;

	@JsonProperty("ApiSubErrors")
	@Builder.Default
	private List<ApiSubErrorDto> subErrors = new ArrayList<>();

}
