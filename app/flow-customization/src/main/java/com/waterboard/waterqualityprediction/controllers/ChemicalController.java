package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.dto.ChemicalDTO;
import com.waterboard.waterqualityprediction.models.Chemical;
import com.waterboard.waterqualityprediction.services.ChemicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chemicals")
public class ChemicalController {

    @Autowired
    private ChemicalService chemicalService;

    @PostMapping("/create")
    public Chemical addChemical(@RequestBody ChemicalDTO chemicalDTO) {
        return chemicalService.addChemical(chemicalDTO);
    }

}
