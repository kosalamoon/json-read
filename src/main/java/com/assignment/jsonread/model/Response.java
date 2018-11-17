package com.assignment.jsonread.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Response {

	private String description;
	private List<Boundary> boundaries;

	public Response() {
		boundaries = new ArrayList<>();
	}
}
