package com.example.Plateforme_livraison.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Plateforme_livraison.repository.PartenaireRepository;
import com.example.Plateforme_livraison.service.PartenaireService;
import com.example.Plateforme_livraison.service.UserRegistrationException;

import jakarta.validation.Valid;

import com.example.Plateforme_livraison.Models.Partenaire;
import com.example.Plateforme_livraison.Models.User;

@RestController
@CrossOrigin(origins = "/")
@RequestMapping("public/Partenaire")
public class ParteanireController {

    private final PartenaireService partenaireService;

    public ParteanireController(PartenaireService partenaireService) {
        this.partenaireService = partenaireService;
    }

    @Autowired
    private PartenaireRepository partenaireRepository;




    // Cette methode return liste des partenaires 
    @GetMapping("/ListPartenaire")
    public ResponseEntity<List<Partenaire>> getAllpartenaire() {
        try {
            return partenaireService.getAllPartenaire();


        } catch (Exception EX) {
            EX.fillInStackTrace();

        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // cette methode ajouter une partenaire dans la bas de donner 
    @PostMapping("/RegisterPartenaire")
    public ResponseEntity<?> addPartenaire(@Valid @RequestBody Partenaire partenaire, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ErrorResponse> errors = new ArrayList<>();

            for (FieldError fieldError : fieldErrors) {
                errors.add(new ErrorResponse("Validation Error",
                        fieldError.getField() + " " + fieldError.getDefaultMessage()));
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }

        try {
            Partenaire partenaireregister = partenaireService.registerUser(partenaire);
            return ResponseEntity.ok(partenaireregister);
        } catch (UserRegistrationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error", e.getMessage()));
        }

    }

    //cette methode get un partenaire avec id
      @GetMapping("/ListPartenaire/{id}")
      public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {

        try {
            return partenaireService.getAllPartenaireById(id);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Partenaire(),HttpStatus.INTERNAL_SERVER_ERROR);
      
      }
      
      
      @PutMapping("/updatepartenaire/{id}")
      public ResponseEntity<String> updatePartenaire(@RequestBody Partenaire partenaire,@PathVariable Long id) {
        try {
            
            return partenaireService.updatePartnaire(partenaire, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
      
      }
     
      @DeleteMapping("/deletePartenaire/{id}")
      public ResponseEntity<String> deletePartenaire(@PathVariable Long id) {
        return partenaireService.deletePartenaire(id);
      
     }
     

    private static class ErrorResponse {
        private final String error;
        private final String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }

}
