package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.dto.TestDTO;
import com.waterboard.waterqualityprediction.models.Test;
import com.waterboard.waterqualityprediction.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;
    //function to add test
    @PostMapping("/create")
    public ResponseEntity<Test> createTest(@RequestBody TestDTO testDTO) {
        Test test = testService.addTest(testDTO);
        return ResponseEntity.ok(test);
    }

    //function to fetch all test
    @GetMapping("/get/all-tests")
    public ResponseEntity<List<Test>> getAllTests() {
        List<Test> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }
}
