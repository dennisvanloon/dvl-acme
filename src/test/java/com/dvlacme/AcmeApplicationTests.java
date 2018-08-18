package com.dvlacme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.dvlacme.controller.ControllerValues.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AcmeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testHome() throws Exception {
		mockMvc.perform(get("/")
				.contentType(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEW_UPLOAD));
	}

	@Test
	public void testHandleUploadXml() throws Exception {
		MockMultipartFile file = createMockMultipartFile("records.xml", "application/xml");
		mockMvc.perform(MockMvcRequestBuilders.multipart("/")
				.file(file))
				.andExpect(status().is(200))
				.andExpect(view().name(VIEW_UPLOAD))
				.andExpect(model().attribute(ATTRIBUTE_ERROR_RECORDS, hasSize(2)));
	}

	@Test
	public void testHandleUploadIncorrectXml() throws Exception {
		MockMultipartFile file = createMockMultipartFile("records-incorrect.xml", "application/xml");
		mockMvc.perform(MockMvcRequestBuilders.multipart("/")
				.file(file))
				.andExpect(status().is(200))
				.andExpect(view().name(VIEW_ERROR))
				.andExpect(model().attribute(ATTRIBUTE_ERROR, is("Found invalid content during xml processing")));
	}

	@Test
	public void testHandleUploadCsv() throws Exception {
		MockMultipartFile file = createMockMultipartFile("records.csv", "application/text");
		mockMvc.perform(MockMvcRequestBuilders.multipart("/")
				.file(file))
				.andExpect(status().is(200))
				.andExpect(view().name(VIEW_UPLOAD))
				.andExpect(model().attribute(ATTRIBUTE_ERROR_RECORDS, hasSize(3)));
	}

	@Test
	public void testHandleUploadIncorrectCsv() throws Exception {
		MockMultipartFile file = createMockMultipartFile("records-incorrect.csv", "application/text");
		mockMvc.perform(MockMvcRequestBuilders.multipart("/")
				.file(file))
				.andExpect(status().is(200))
				.andExpect(status().is(200))
				.andExpect(view().name(VIEW_ERROR))
				.andExpect(model().attribute(ATTRIBUTE_ERROR, is("Found invalid content during csv processing")));
	}

	private MockMultipartFile createMockMultipartFile(String filename, String contentType) throws IOException {
		InputStream fileInputStream = new FileInputStream(new File(getClass().getClassLoader().getResource(filename).getFile()));
		return new MockMultipartFile("file", filename, contentType, fileInputStream);
	}

}
