package com.example.dvlacme;

//import hello.storage.StorageFileNotFoundException;
//import hello.storage.StorageService;
import com.example.dvlacme.domain.ErrorRecord;
import com.example.dvlacme.domain.Record;
import com.example.dvlacme.service.FileProcessor;
import com.example.dvlacme.service.InvalidFileContentException;
import com.example.dvlacme.service.InvalidFileExtensionException;
import com.example.dvlacme.service.RecordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final FileProcessor fileProcessor;

    private final RecordValidator recordValidator;

    @Autowired
    public FileUploadController(FileProcessor fileProcessor, RecordValidator recordValidator) {
        this.fileProcessor = fileProcessor;
        this.recordValidator = recordValidator;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

//        model.addAttribute("files", storageService.loadAll().map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString())
//                .collect(Collectors.toList()));
        //model.addAttribute("errorRecords", new ArrayList<ErrorRecord>());

        return "uploadForm";
    }

//    @GetMapping("/files/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//        Resource file = storageService.loadAsResource(filename);
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

    @PostMapping("/")
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        List<Record> records;
        try {
            records = fileProcessor.processFile(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Oops, something unexpected happened, try again!");
            return new ModelAndView("redirect:/");
        } catch (InvalidFileContentException | InvalidFileExtensionException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ModelAndView("redirect:/");
        }

        final Collection<ErrorRecord> errorRecords = recordValidator.validateRecords(records);

        if (errorRecords.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        } else {
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + " but it contains " + errorRecords.size() + " error!");
        }

        ModelAndView modelAndView = new ModelAndView("uploadForm");
        modelAndView.getModelMap().addAttribute("errorRecords", errorRecords);
        return modelAndView;
        //return "redirect:/";
    }

//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }

}
