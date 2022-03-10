package edu.tus.winemanager.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.tus.winemanager.entity.Wine;
import edu.tus.winemanager.enums.Rating;
import edu.tus.winemanager.exceptions.CouldNotAddResourceException;
import edu.tus.winemanager.exceptions.ResourceNotFoundException;
import edu.tus.winemanager.repository.WineRepository;
import edu.tus.winemanager.utils.BeanCopyUtitliy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class WineController {

    @Autowired
    WineRepository wineRepository;

    @GetMapping("/wines")
    public ResponseEntity<Page<Wine>> getAllWines(@PageableDefault(page = 0, value = Integer.MAX_VALUE) Pageable pageable){
        System.out.println(pageable);
//        if(pageable == null){
//            System.out.println("HERERERE");
//            pageable = PageRequest.of(0,Integer.MAX_VALUE);
//        }
        return new ResponseEntity<>(wineRepository.findAll(pageable), HttpStatus.OK);
    }


    @GetMapping("/wine/{id}")
    public ResponseEntity<Wine> getAllWines(@PathVariable Long id){
        Optional<Wine> wine = wineRepository.findById(id);
        if(wine.isPresent()){
            return new ResponseEntity<>(wine.get(), HttpStatus.OK);
        }else{
            throw new ResourceNotFoundException();
        }
    }

    @GetMapping("/wine")
    public List<Wine> getWinesByRating(@RequestParam(required = false) Rating rating, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiry_date){
        if(rating!=null && expiry_date!=null){
            Optional<List<Wine>> wine = wineRepository.findByRatingAndExpiryDateLessThanEqual(rating,expiry_date);
            if (wine.isPresent()) {
                return wine.get();
            } else {
                throw new ResourceNotFoundException();
            }
        }
        else if(rating!=null) {
            Optional<List<Wine>> wine = wineRepository.findByRating(rating);
            if (wine.isPresent()) {
                return wine.get();
            } else {
                throw new ResourceNotFoundException();
            }
        }else{
//            Optional<List<Wine>> wine = wineRepository.findByExpiryDate(expiry_date);
            Optional<List<Wine>> wine = wineRepository.findByExpiryDateLessThanEqual(expiry_date);

            if (wine.isPresent()) {
                return wine.get();
            } else {
                throw new ResourceNotFoundException();
            }
        }
    }

//    @GetMapping("/wine")
//    public List<Wine> getWinesByExpiryDate(@RequestParam LocalDate expiry_date){
//        Optional<List<Wine>> wine = wineRepository.findByExpiryDate(expiry_date);
//        if(wine.isPresent()){
//            return wine.get();
//        }else{
//            throw new ResourceNotFoundException();
//        }
//    }


    @PostMapping("/wine")
    public ResponseEntity<Wine> addWine(@Valid @RequestBody Wine wine){
        try{
            wine = wineRepository.save(wine);
            return new ResponseEntity<>(wine, HttpStatus.CREATED);
        }catch (Exception e){
            throw new CouldNotAddResourceException();
        }
    }

    @PutMapping("/wine/{id}")
    public Wine editWine(@PathVariable Long id, @RequestBody Wine wine){
        try{
            Optional<Wine> wineFromRepo = wineRepository.findById(id);
            Wine wineNew = wineFromRepo.get();
            BeanUtils.copyProperties(wine, wineNew, BeanCopyUtitliy.getNullPropertyNames(wine));
            wineNew.setId(id);
            Wine save = wineRepository.save(wineNew);
            return save;
        }catch (Exception e){
            throw new CouldNotAddResourceException();
        }
    }


    @DeleteMapping("/wine/{id}")
    public Wine deleteWine(@PathVariable Long id){
        try{
            Optional<Wine> wine = wineRepository.findById(id);
            wineRepository.delete(wine.get());
            return wine.get();
        }catch (Exception e){
            throw new ResourceNotFoundException();
        }
    }


//    @GetMapping("/wine/pages")
//    public List<Wine> getWinesByPages(RequestParam Pageable pageable){
//
//    }

}
