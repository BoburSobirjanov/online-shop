package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.CreditDto;
import uz.com.onlineshop.model.dto.response.CreditForFrontDto;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.CreditService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit")
@CrossOrigin
public class CreditController {

    private final CreditService creditService;


    @PostMapping("/save")
    public StandardResponse<CreditForFrontDto> save(
            @RequestBody CreditDto creditDto,
            Principal principal
    ) {
        return creditService.save(creditDto, principal);
    }


    @GetMapping("get-by-id/{id}")
    public StandardResponse<CreditForFrontDto> getById(
            @PathVariable UUID id
    ) {
        return creditService.getById(id);
    }


    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
    ) {
        return creditService.deleteById(id, principal);
    }


    @PutMapping("/{id}/pay-for-this/")
    public StandardResponse<CreditForFrontDto> payForCredit(
            @PathVariable UUID id,
            @RequestParam Double amount,
            @RequestParam String cardId
    ) {
        return creditService.payForCredit(id, amount, cardId);
    }


    @GetMapping("/get-all-credits")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CreditForFrontDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return creditService.findAll(pageable);
    }


    @GetMapping("/get-my-credits")
    public Page<CreditForFrontDto> getMyCredits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return creditService.getMyCredits(principal, pageable);
    }


    @GetMapping("/get-user-credit")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CreditForFrontDto> getUsersCredit(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam UUID userId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return creditService.getUsersCredit(userId, pageable);
    }
}
