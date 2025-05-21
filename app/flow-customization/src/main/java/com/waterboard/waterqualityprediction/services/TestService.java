package com.waterboard.waterqualityprediction.services;


import com.waterboard.waterqualityprediction.dto.TestDTO;
import com.waterboard.waterqualityprediction.models.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {

    @PersistenceContext
    private EntityManager entityManager;

    //function to add test
    @Transactional
    public Test addTest(TestDTO testDTO) {
        Test test = new Test();
        test.setTestName(testDTO.getTestName());
        test.setTestValue(testDTO.getTestValue());
        test.setTestDescription(testDTO.getTestDescription());

        entityManager.persist(test);
        return test;
    }

    //function to fetch all test
    public List<Test> getAllTests() {
        // Using JPQL to fetch all records from the Test table
        TypedQuery<Test> query = entityManager.createQuery("SELECT t FROM Test t", Test.class);
        return query.getResultList();
    }
}
