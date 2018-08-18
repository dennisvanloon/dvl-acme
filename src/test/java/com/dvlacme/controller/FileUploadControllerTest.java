package com.dvlacme.controller;

import com.dvlacme.DataFactory;
import com.dvlacme.domain.ErrorRecord;
import com.dvlacme.domain.Record;
import com.dvlacme.exception.AcmeApplicationException;
import com.dvlacme.service.FileProcessor;
import com.dvlacme.service.RecordService;
import com.dvlacme.validation.RecordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dvlacme.controller.ControllerValues.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileProcessor fileProcessor;

    @MockBean
    private RecordValidator recordValidator;

    @MockBean
    private RecordService recordService;

    private Record record1;

    private Record record2;

    private Record record3;

    private ErrorRecord errorRecord1;

    private List<Record> records;

    private String filename;

    private MockMultipartFile file;

    @Before
    public void setUp() throws IOException {
        record1 = DataFactory.makeReferenceRecord1();
        record2 = DataFactory.makeReferenceRecord2();
        record3 = DataFactory.makeReferenceRecord3();

        records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        records.add(record3);

        errorRecord1 = new ErrorRecord(record1, Stream.of("errorMessage").collect(Collectors.toSet()));

        filename = "records.xml";
        final InputStream fileInputStream = new FileInputStream(new File(getClass().getClassLoader().getResource(filename).getFile()));
        file = new MockMultipartFile("file", filename, "application/xml", fileInputStream);
    }

    @Test
    public void testHome() throws Exception {
        mvc.perform(get("/")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_UPLOAD));
        Mockito.verifyNoMoreInteractions(fileProcessor, recordService, recordValidator);
    }

    @Test
    public void testHandleUpload() throws Exception {
        Mockito.when(fileProcessor.processFile(eq(filename), any(InputStream.class))).thenReturn(records);
        Mockito.when(recordService.addAll(records)).thenReturn(Collections.singletonList(errorRecord1));

        mvc.perform(MockMvcRequestBuilders.multipart("/")
                .file(file))
                .andExpect(status().is(200))
                .andExpect(view().name(VIEW_UPLOAD))
                .andExpect(model().attribute(ATTRIBUTE_ERROR_RECORDS, hasSize(1)))
                .andExpect(model().attribute(ATTRIBUTE_ERROR_RECORDS, contains(errorRecord1)));
    }

    @Test
    public void testHandleUploadIOException() throws Exception {
        Mockito.when(fileProcessor.processFile(eq(filename), any(InputStream.class))).thenThrow(IOException.class);

        mvc.perform(MockMvcRequestBuilders.multipart("/")
                .file(file))
                .andExpect(status().is(200))
                .andExpect(view().name(VIEW_ERROR));
    }

    @Test
    public void testHandleAcmeApplicationException() throws Exception {
        String message = "invalid content";
        Mockito.when(fileProcessor.processFile(eq(filename), any(InputStream.class)))
                .thenThrow(new AcmeApplicationException(message));

        mvc.perform(MockMvcRequestBuilders.multipart("/")
                .file(file))
                .andExpect(status().is(200))
                .andExpect(view().name(VIEW_ERROR))
                .andExpect(model().attribute(ATTRIBUTE_ERROR, message));
    }

    @Test
    public void testHistory() throws Exception {
        Mockito.when(recordService.getAll()).thenReturn(Arrays.asList(record1, record2));

        mvc.perform(get("/history")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_HISTORY))
                .andExpect(model().attribute(ATTRIBUTE_RECORDS, contains(record1, record2)));
        Mockito.verify(recordService, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(fileProcessor, recordService, recordValidator);
    }

    @Test
    public void testClear() throws Exception {
        mvc.perform(get("/clearhistory")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_HISTORY));
        Mockito.verify(recordService, Mockito.times(1)).clear();
        Mockito.verifyNoMoreInteractions(fileProcessor, recordService, recordValidator);
    }

}
