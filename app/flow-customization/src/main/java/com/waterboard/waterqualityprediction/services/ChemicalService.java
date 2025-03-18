package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.dto.ChemicalDTO;
import com.waterboard.waterqualityprediction.models.Chemical;
import com.waterboard.waterqualityprediction.models.Step;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChemicalService {

    @PersistenceContext
    private EntityManager entityManager;

    //function to add new chemical
    @Transactional
    public Chemical addChemical(ChemicalDTO chemicalDTO) {
        Chemical chemical = new Chemical();
        chemical.setChemicalName(chemicalDTO.getChemicalName());
        chemical.setChemicalType(chemicalDTO.getChemicalType());

        entityManager.persist(chemical); // Persisting the chemical manually
        return chemical;
    }

    public List<Chemical> getAllChemicals() {
        try {
            // Ensure that you use the correct JPQL query
            TypedQuery<Chemical> query = entityManager.createQuery("SELECT c FROM Chemical c", Chemical.class);
            List<Chemical> chemicals = query.getResultList();
            return chemicals;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
