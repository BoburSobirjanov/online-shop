package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.PaymentDto;
import uz.com.onlineshop.model.dto.response.PaymentForFrontDto;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.PaymentService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PutMapping("/pay-for-order")
    public StandardResponse<PaymentForFrontDto> payForOrder(
            @RequestBody PaymentDto paymentDto,
            Principal principal
            ){
        return paymentService.payForOrder(paymentDto,principal);
    }



    @GetMapping("/get-by-id/{id}")
    public StandardResponse<PaymentForFrontDto> getById(
            @PathVariable UUID id
            ){
        return paymentService.getById(id);
    }



    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
    ){
        return paymentService.deleteById(id, principal);
    }




    @DeleteMapping("/multi-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> multiDelete(
            @RequestBody List<String> id,
            Principal principal
    ){
       return paymentService.multiDeleteById(id, principal);
    }
}
