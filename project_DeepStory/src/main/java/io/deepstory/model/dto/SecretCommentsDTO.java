package io.deepstory.model.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SecretCommentsDTO {
	
	private BigInteger commentsId;
	
	private String commentCreate;
	
	private String commentModify;
	
	private String commentData;
	
	private int accountId;
	
	private int secretPostId;
	
}
