package uz.com.onlineshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.exception.RequestValidationException;
import uz.com.onlineshop.model.dto.request.OrderDto;
import uz.com.onlineshop.model.dto.response.OrderForFront;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.OrderService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;



    @PostMapping("/save")
    public ResponseEntity<StandardResponse<OrderForFront>> save(
            @Valid
            @RequestBody OrderDto orderDto,
            Principal principal, BindingResult bindingResult
            )throws RequestValidationException {
        if (bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
      return  ResponseEntity.ok(orderService.save(orderDto, principal));
    }




    @GetMapping("/get-by-id/{id}")
    public StandardResponse<OrderForFront> getById(
            @PathVariable UUID id
            ){
        return orderService.getById(id);
    }




    @DeleteMapping("/{id}/delete-by-id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public StandardResponse<String> deleteById(
            @PathVariable UUID id,
            Principal principal
    ){
        return orderService.delete(id, principal);
    }



    @PutMapping("/{id}/cancel")
    public StandardResponse<OrderForFront> cancel(
            @PathVariable UUID id
    ){
        return orderService.cancelOrder(id);
    }




    @PutMapping("/update-order/{id}")
    public StandardResponse<OrderForFront> update(
            @PathVariable UUID id,
            @RequestBody OrderDto orderDto,
            Principal principal
    ){
        return orderService.updateOrder(id, orderDto, principal);
    }



    @GetMapping("/get-cancelled-orders")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public Page<OrderForFront> getCancelled(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getCancelledOrders(pageable);
    }



    @GetMapping("/get-my-orders")
    public Page<OrderForFront> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ){
        Pageable pageable = PageRequest.of(page,size);
      return  orderService.getMyOrders(pageable,principal);
    }





    @PutMapping("/{id}/delivered")
    public StandardResponse<String> changeOrderToDelivered(
            @PathVariable UUID id
    ){
        return orderService.changeOrderToDelivered(id);
    }




    @PutMapping("/{id}/pay-for-order")
    public StandardResponse<OrderForFront> payForOrder(
            @PathVariable UUID id,
            @RequestParam String cardId
    ){
        return orderService.payForOrder(id, cardId);
    }
}
