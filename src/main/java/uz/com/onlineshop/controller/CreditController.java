package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.CreditDto;
import uz.com.onlineshop.model.dto.response.CreditForFront;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.CreditService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;


    @PostMapping("/save")
    public StandardResponse<CreditForFront> save(
            @RequestBody CreditDto creditDto,
            Principal principal
            ){
        return creditService.save(creditDto, principal);
    }




    @GetMapping("get-by-id/{id}")
    public StandardResponse<CreditForFront> getById(
            @PathVariable UUID id
            ){
        return creditService.getById(id);
    }




    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
    ){
        return creditService.deleteById(id, principal);
    }




    @PutMapping("/{id}/pay-for-this/")
    public StandardResponse<CreditForFront> payForCredit(
            @PathVariable UUID id,
            @RequestParam Double amount,
            @RequestParam String cardId
    ){
        return creditService.payForCredit(id, amount, cardId);
    }
}
