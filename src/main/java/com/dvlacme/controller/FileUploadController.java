package com.dvlacme.controller;

import com.dvlacme.domain.ErrorRecord;
import com.dvlacme.domain.Record;
import com.dvlacme.service.FileProcessor;
import com.dvlacme.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.dvlacme.controller.ControllerValues.*;

@Controller
@SuppressWarnings("unused")
public class FileUploadController {

    private final Logger LOG = LoggerFactory.getLogger(FileUploadController.class);

    private final FileProcessor fileProcessor;

    private final RecordService recordService;

    @Value("${error.general}")
    private String generalError;

    @Value("${processed.ok}")
    private String processOk;

    @Value("${processed.errors}")
    private String processErrors;

    @Autowired
    public FileUploadController(FileProcessor fileProcessor, RecordService recordService) {
        this.fileProcessor = fileProcessor;
        this.recordService = recordService;
    }

    @GetMapping("/")
    public String home() {
        return VIEW_UPLOAD;
    }

    @PostMapping("/")
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        List<Record> records = fileProcessor.processFile(file.getOriginalFilename(), file.getInputStream());
        LOG.info(String.format("Processing %d records", records.size()));

        int numberOfRecords = records.size();
        Collection<ErrorRecord> errorRecords = recordService.addAll(records);

        ModelAndView modelAndView = new ModelAndView(VIEW_UPLOAD);
        modelAndView.getModelMap().addAttribute(ATTRIBUTE_MESSAGE, errorRecords.isEmpty() ?
                String.format(processOk, numberOfRecords) : String.format(processErrors, errorRecords.size(), numberOfRecords));
        modelAndView.getModelMap().addAttribute(ATTRIBUTE_ERROR_RECORDS, errorRecords);

        return modelAndView;
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute(ATTRIBUTE_RECORDS, recordService.getAll());
        return VIEW_HISTORY;
    }

    @GetMapping("/clearhistory")
    public String clearHistory() {
        recordService.clear();
        return VIEW_HISTORY;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception e) {
        LOG.error("Request: " + request.getRequestURL() + " raised " + e);

        ModelAndView modelAndView = new ModelAndView(VIEW_ERROR);
        modelAndView.addObject(ATTRIBUTE_ERROR, e.getMessage());
        return modelAndView;
    }

}
