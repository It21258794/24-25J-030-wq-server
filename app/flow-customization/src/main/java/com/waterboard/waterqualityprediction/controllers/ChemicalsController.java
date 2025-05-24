package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.dto.ChemicalDTO;
import com.waterboard.waterqualityprediction.models.Chemical;
import com.waterboard.waterqualityprediction.services.ChemicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chemicals")
public class ChemicalsController {

    @Autowired
    private ChemicalService chemicalService;

    //function to add new chemical
    @PostMapping("/create")
    public Chemical addChemical(@RequestBody ChemicalDTO chemicalDTO) {
        return chemicalService.addChemical(chemicalDTO);
    }

    //function to fetch all chemicals

    @GetMapping ("/get/all-chemicals")
    public List<Chemical> getAllSteps() {
        return chemicalService.getAllChemicals();
    }


}
