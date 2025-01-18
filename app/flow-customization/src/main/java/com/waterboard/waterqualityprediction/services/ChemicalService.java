package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.dto.ChemicalDTO;
import com.waterboard.waterqualityprediction.models.Chemical;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChemicalService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Chemical addChemical(ChemicalDTO chemicalDTO) {
        Chemical chemical = new Chemical();
        chemical.setChemicalName(chemicalDTO.getChemicalName());
        chemical.setChemicalType(chemicalDTO.getChemicalType());

        entityManager.persist(chemical); // Persisting the chemical manually
        return chemical;
    }
}
