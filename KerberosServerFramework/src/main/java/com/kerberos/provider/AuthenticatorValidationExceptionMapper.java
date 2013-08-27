package com.kerberos.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.kerberos.exceptions.AuthenticatorValidationException;
import com.kerberos.exceptions.ErrorResponse;

@Provider
public class AuthenticatorValidationExceptionMapper implements ExceptionMapper<AuthenticatorValidationException>{

	@Override
	public Response toResponse(AuthenticatorValidationException exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorId(exception.getStatusCode());
		errorResponse.setErrorMessage(exception.getMessage());
		return Response.status(exception.getStatusCode()).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
	}

	
}
