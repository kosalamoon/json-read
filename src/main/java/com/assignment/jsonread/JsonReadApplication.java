package com.assignment.jsonread;

import com.assignment.jsonread.model.Boundary;
import com.assignment.jsonread.model.Response;
import com.assignment.jsonread.model.SortedResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class JsonReadApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonReadApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Response>> typeReference = new TypeReference<List<Response>>() {
			};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/invoice.json");
			List<Response> responseList = mapper.readValue(inputStream, typeReference); // reading json file and put it into POJO list
			List<SortedResponse> sortedResponseList = sort(responseList); // sort the entire response list
			// sout logic goes here.. but this might not be efficient
			int rowCoordinate = 0;
			for (int i = 0; i < sortedResponseList.size(); i++) {
				SortedResponse response = sortedResponseList.get(i);
				if (rowCoordinate == 0)
					rowCoordinate = response.getBoundary().getY();

				if (rowCoordinate == response.getBoundary().getY() || rowCoordinate == response.getBoundary().getY() + 1) {
					System.out.print(response.getDescription() + " ");
					rowCoordinate = response.getBoundary().getY();

				} else {
					System.out.println();
					System.out.print(response.getDescription() + " ");
					rowCoordinate = 0;
				}
			}


		};
	}

	public Boundary calculateCenter(Response response) {
		List<Boundary> boundaries = response.getBoundaries();
		int x1 = boundaries.get(0).getX();
		int x2 = boundaries.get(1).getX();
		int y1 = boundaries.get(1).getY();
		int y2 = boundaries.get(2).getY();
		int x = ((x2 - x1) / 2) + x1;
		int y = ((y2 - y1) / 2) + y1;
//		System.out.print("X: " + x + " Y: " + y + " ");
		return new Boundary(x, y);
	}

	public List<SortedResponse> sort(List<Response> response) {
		List<SortedResponse> sortedResponseList = response.stream()
				.map(response1 -> {
					Boundary boundary = calculateCenter(response1); // get the center coordinates
					return new SortedResponse(response1.getDescription(), boundary); // return it with new object but it still isn't sorted yet
				})
				.sorted(
						Comparator.comparing((SortedResponse o) -> o.getBoundary().getY()) // sorting based on the Y
								.thenComparing((SortedResponse o) -> o.getBoundary().getX()))// from above SORTED list sorting based on the X
				.collect(Collectors.toList()); // collect the list assign it to sortedResponseList

		sortedResponseList.forEach(sortedResponse -> {
			System.out.println(sortedResponse.getDescription());
			System.out.print("\tX: " +sortedResponse.getBoundary().getX());
			System.out.println("\tY: " + sortedResponse.getBoundary().getY());
		});

		return sortedResponseList;

	}
}
