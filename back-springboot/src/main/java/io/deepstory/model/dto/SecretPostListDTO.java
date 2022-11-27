package io.deepstory.model.dto;

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
public class SecretPostListDTO {
	
	private int secretPostId;
	
	private String secretPostName;
	
	private String secretPostContents;
	
	private int secretWriterId;
	
	private int secretFriendId;
    
    private String secretImage;
	
	


}
