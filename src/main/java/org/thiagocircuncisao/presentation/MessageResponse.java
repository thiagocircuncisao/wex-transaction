package org.thiagocircuncisao.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
	private String message;

	private String path;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private LocalDateTime timestamp;

	public MessageResponse(String message) {
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	public MessageResponse(String message, String path) {
		this.message = message;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}

}
