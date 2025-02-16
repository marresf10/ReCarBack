package com.recar.controller;

import com.recar.dto.AutoDTO;
import com.recar.entity.Auto;
import com.recar.repository.AutoRepository;
import com.recar.service.AutoService;
import com.recar.entity.AutoImagenes;
import java.util.HashMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

@RestController
@CrossOrigin(origins = "http://localhost:5174") //problemas de cors
@RequestMapping("/api")
public class AutoController {
    @Autowired
    AutoRepository autoRepository;

    @GetMapping("/getAllAutos")
    public List<AutoDTO> getAllAutos() {
        List<Auto> autos = autoRepository.findByEstatus("DISPONIBLE");

        return autos.stream().map(auto -> {
            AutoDTO autoDTO = new AutoDTO();
            autoDTO.setId(auto.getId());
            autoDTO.setNoInv(auto.getNo_inv());
            autoDTO.setSubmarca(auto.getSubmarca());
            autoDTO.setTransm(auto.getTransm());
            autoDTO.setModelo(auto.getModelo());
            autoDTO.setColor(auto.getColor());
            autoDTO.setKm(auto.getKm());
            autoDTO.setSerie(auto.getSerie());
            autoDTO.setEstatus(auto.getEstatus());
            autoDTO.setPrecio(auto.getPrecio());
            autoDTO.setUbicacion(auto.getUbicacion());
            autoDTO.setDescripcion(auto.getDescripcion());
            autoDTO.setImagen(auto.getImagen());
            autoDTO.setActivo(auto.isActivo());
            autoDTO.setFechaRegistro(auto.getFechaRegistro());

            List<String> imagenUrls = auto.getImagenes().stream()
                                            .map(AutoImagenes::getUrl)
                                            .collect(Collectors.toList());
            autoDTO.setImagenes(imagenUrls);

            return autoDTO;
        }).collect(Collectors.toList());
    }
    
    @GetMapping("/getAuto/{id}")
    public ResponseEntity<Map<String, Object>> getAuto(@PathVariable String id) {
        Optional<Auto> optionalAuto = autoRepository.findAutoWithImages(id);

        if (optionalAuto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Auto no encontrado"));
        }

        Auto auto = optionalAuto.get();
        Map<String, Object> response = new HashMap<>();

        response.put("auto_id", auto.getId());
        response.put("submarca", auto.getSubmarca());
        response.put("modelo", auto.getModelo());
        response.put("precio", auto.getPrecio());
        response.put("color", auto.getColor());
        response.put("km", auto.getKm());
        response.put("ubicacion", auto.getUbicacion());
        response.put("transm", auto.getTransm());
        response.put("estatus", auto.getEstatus());
        response.put("serie", auto.getSerie());
        response.put("descripcion", auto.getDescripcion());
        response.put("imagen", auto.getImagen());

        List<String> imagenesUrls = auto.getImagenes().stream()
                .map(AutoImagenes::getUrl)
                .collect(Collectors.toList());

        if (imagenesUrls.isEmpty()) {
            imagenesUrls = List.of(auto.getImagen());
        }

        response.put("imagenes", imagenesUrls);

        return ResponseEntity.ok(response);
    }

}
