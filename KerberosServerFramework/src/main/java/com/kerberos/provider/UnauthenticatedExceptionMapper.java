package com.kerberos.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.kerberos.exceptions.ErrorResponse;
import com.kerberos.exceptions.UnauthenticatedException;

@Provider
public class UnauthenticatedExceptionMapper implements ExceptionMapper<UnauthenticatedException>{

	@Override
	public Response toResponse(UnauthenticatedException exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorId(exception.getStatusCode());
		errorResponse.setErrorMessage(exception.getMessage());
		return Response.status(exception.getStatusCode()).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
	}

	
}
