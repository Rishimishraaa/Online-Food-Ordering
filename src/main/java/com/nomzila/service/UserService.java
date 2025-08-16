package com.nomzila.service;

import com.nomzila.model.User;

public interface UserService {

	public User findUserByJwtToken(String jwt)throws Exception;
	public User findUserByEmail(String email)throws Exception;
}
