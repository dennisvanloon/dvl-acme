package com.example.dvlacme.controller;

import com.example.dvlacme.domain.ErrorRecord;
import com.example.dvlacme.domain.Record;
import com.example.dvlacme.service.FileProcessor;
import com.example.dvlacme.parser.InvalidFileContentException;
import com.example.dvlacme.parser.InvalidFileExtensionException;
import com.example.dvlacme.service.RecordService;
import com.example.dvlacme.validation.RecordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final Logger LOG = LoggerFactory.getLogger(FileUploadController.class);

    private final FileProcessor fileProcessor;

    private final RecordValidator recordValidator;

    private final RecordService recordService;

    @Autowired
    public FileUploadController(FileProcessor fileProcessor, RecordValidator recordValidator, RecordService recordService) {
        this.fileProcessor = fileProcessor;
        this.recordValidator = recordValidator;
        this.recordService = recordService;
    }

    @GetMapping("/")
    public String home() {
        return "uploadForm";
    }

    @PostMapping("/")
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView("uploadForm");
        List<Record> records;
        try {
            records = fileProcessor.processFile(file.getOriginalFilename(), file.getInputStream());
            LOG.info(String.format("Processing %d records", records.size()));
        } catch (InvalidFileContentException | InvalidFileExtensionException e) {
            modelAndView.getModelMap().addAttribute("error", e.getMessage());
            return modelAndView;
        } catch (IOException e) {
            modelAndView.getModelMap().addAttribute("error", "Oops, something unexpected happened, try again!");
            return modelAndView;
        }

        final Collection<ErrorRecord> errorRecords = recordValidator.validateRecords(records);
        LOG.info(String.format("Found %d error records", errorRecords.size()));
        modelAndView.getModelMap().addAttribute("message", errorRecords.isEmpty() ?
                "All records in the file were succesfully processed" :
                "The file contained " + errorRecords.size() + " records that could not be processed, details are given below");
        modelAndView.getModelMap().addAttribute("errorRecords", errorRecords);

        records.removeAll(errorRecords.stream().map(errorRecord -> errorRecord.getRecord()).collect(Collectors.toSet()));
        recordService.addAll(records);
        LOG.info(String.format("Stored %d records", records.size()));

        return modelAndView;
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("records", recordService.getAll());
        return "history";
    }

    @GetMapping("/clearhistory")
    public String clearHistory() {
        recordService.clear();
        return "history";
    }

}
