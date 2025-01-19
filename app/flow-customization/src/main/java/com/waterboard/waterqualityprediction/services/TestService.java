package com.waterboard.waterqualityprediction.services;


import com.waterboard.waterqualityprediction.dto.TestDTO;
import com.waterboard.waterqualityprediction.models.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Test addTest(TestDTO testDTO) {
        Test test = new Test();
        test.setTestName(testDTO.getTestName());
        test.setTestValue(testDTO.getTestValue());
        test.setTestDescription(testDTO.getTestDescription());

        entityManager.persist(test);
        return test;
    }
}
