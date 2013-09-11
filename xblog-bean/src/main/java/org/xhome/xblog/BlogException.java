package org.xhome.xblog;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 11, 201312:55:38 AM
 * @describe 
 */
public class BlogException extends Exception {
	
	private static final long serialVersionUID = 1895394882621771751L;
	private short status;
	
	public BlogException() {
		super();
	}
	
	public BlogException(short status) {
		super();
		this.setStatus(status);
	}
	
	public BlogException(String message) {
		super(message);
	}
	
	public BlogException(short status, String message) {
		super(message);
		this.setStatus(status);
	}
	
	public BlogException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BlogException(short status, String message, Throwable cause) {
		super(message, cause);
		this.setStatus(status);
	}
	
	public BlogException(Throwable cause) {
		super(cause);
	}
	
	public BlogException(short status, Throwable cause) {
		super(cause);
		this.setStatus(status);
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public short getStatus() {
		return status;
	}
	
}
