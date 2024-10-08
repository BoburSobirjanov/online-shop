package uz.com.onlineshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.CardDto;
import uz.com.onlineshop.model.dto.response.CardForFront;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.CardService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {



    private final CardService cardService;



    @PostMapping("/save-card")
    public StandardResponse<CardForFront> save(
            @Valid @RequestBody CardDto cardDto,
            Principal principal
            ){
        return cardService.save(cardDto, principal);
    }




    @DeleteMapping("/{id}/remove-card")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
            ){
        return cardService.delete(id, principal);
    }




    @GetMapping("/get-all-by-type")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public Page<CardForFront> getByType(
            @RequestParam String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return cardService.getCardsByType(pageable,type);
    }





    @GetMapping("/get-all-cards")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Page<CardForFront> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return cardService.getAllCards(pageable);
    }





    @GetMapping("/get-by-id/{id}")
    public StandardResponse<CardForFront> getById(
            @PathVariable UUID id
    ){
        return cardService.getById(id);
    }



    @PutMapping("/{id}/filled-balance")
    public StandardResponse<String> filledBalance(
            @PathVariable UUID id,
            @RequestParam Double balance
    ){
        return cardService.fillCardBalance(id, balance);
    }

}
