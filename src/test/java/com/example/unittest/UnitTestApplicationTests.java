package com.example.unittest;

import com.example.unittest.Controller.UsersController;
import com.example.unittest.Entity.Users;
import com.example.unittest.Repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UnitTestApplicationTests {
	@Autowired
	private UsersController usersController;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@Test
	void contextLoad(){
		assertThat(usersController).isNotNull();
	}

	@Test
	public void createUsers() throws Exception{
		Users users = new Users(1L, "Paolo", "Bianchi", "ddd@hhh");
		mockMvc.perform(post("/users/new")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(users)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.nome").value("Paolo"))
				.andExpect(jsonPath("$.cognome").value("Bianchi"))
				.andExpect(jsonPath("$.email").value("ddd@hhh"));

	}
	@Test
	public void getUsersById() throws Exception{
		Users users = new Users(1L, "Paolo", "Bianchi", "ddd@hhh");
		Users usersSaved = usersRepository.save(users);
		mockMvc.perform(get("/users/{id}", users.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(users.getId()));
	}
	@Test
	public void updateUsers() throws Exception{
		Users users = new Users(1L, "Paolo", "Bianchi", "ddd@hhh");
		Users usersSaved = usersRepository.save(users);
		Users users2 = new Users(1L, "Giulia", "Verdi", "ddd@hhh");

		mockMvc.perform(put("/users/update/{id}", users.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(users2)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(users.getId()))
				.andExpect(jsonPath("$.nome").value("Giulia"))
				.andExpect(jsonPath("$.cognome").value("Verdi"))
				.andExpect(jsonPath("$.email").value("ddd@hhh"));
	}
	@Test
	public void deleteUsers() throws Exception{
		Users users = new Users(1L, "Paolo", "Bianchi", "ddd@hhh");
		users = usersRepository.save(users);

		mockMvc.perform(delete("/users/delete/{id}", users.getId())).andExpect(status().isOk());

		mockMvc.perform(get("/users/{id}", users.getId()))
				.andExpect(status().isInternalServerError());

	}

}
