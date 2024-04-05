package com.tathvatech.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestAppException extends Exception implements ExceptionMapper<RestAppException>
{

	private static final long serialVersionUID = 1L;

	public RestAppException()
	{
	}

	public RestAppException(String errorMessage)
	{
		super(errorMessage);
	}

	@Override
	public Response toResponse(RestAppException e)
	{
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/Json").build();
	}

}
