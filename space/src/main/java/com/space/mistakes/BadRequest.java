package com.space.mistakes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "parameter went wrong")
public class BadRequest extends RuntimeException{
}
