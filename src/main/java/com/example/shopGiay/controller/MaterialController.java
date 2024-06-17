package com.example.shopGiay.controller;


import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    MaterialService materialService;
    @GetMapping
    public String listMaterials(Model model, @RequestParam(defaultValue = "0") int page) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of materials
        Page<Material> materialPage = materialService.getAllMaterialsPaginated(pageable);

        // Add pagination information to the model
        model.addAttribute("materials", materialPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", materialPage.getTotalPages());

        return "material/list";
    }

    @GetMapping("/new")
    public String createMaterialForm(Model model) {
        Material material = new Material();
        model.addAttribute("material", material);
        return "material/new";
    }

    @PostMapping
    public String saveBrand(@ModelAttribute("material") Material material) {
        materialService.saveMaterial(material);
        return "redirect:/material";
    }

    @GetMapping("/edit/{id}")
    public String editMaterialForm(@PathVariable Integer id, Model model) {
        Material material = materialService.getMaterialById(id);
        model.addAttribute("material", material);
        return "material/edit";
    }

    @PostMapping("/{id}")
    public String updateBrand(@PathVariable Integer id, @ModelAttribute("material") Material material, Model model) {
        Material existingMaterial = materialService.getMaterialById(id);
        existingMaterial.setId(id);
        existingMaterial.setName(material.getName());
        existingMaterial.setCode(material.getCode());
        existingMaterial.setCreateDate(material.getCreateDate());
        existingMaterial.setUpdateDate(material.getUpdateDate());
        existingMaterial.setStatus(material.getStatus());
        materialService.saveMaterial(existingMaterial);
        return "redirect:/material";
    }

    @GetMapping("/{id}")
    public String deleteMaterial(@PathVariable Integer id) {
        Material material =materialService.getMaterialById(id);
        material.setStatus(0);
        materialService.saveMaterial(material);
        return "redirect:/material";
    }

}

