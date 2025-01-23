package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class CreateAdUserDto {

	private String samAccountName;
	private String userPrincipalName;
	private String givenName;
	private String firstName;
	private String lastName;
	private String email;
	
}
