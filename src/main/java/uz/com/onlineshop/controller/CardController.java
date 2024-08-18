package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.com.onlineshop.model.dto.request.CardDto;
import uz.com.onlineshop.model.dto.response.CardForFront;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.CardService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {



    private final CardService cardService;



    @PostMapping("/save-card")
    public StandardResponse<CardForFront> save(
            @RequestBody CardDto cardDto,
            Principal principal
            ){
        return cardService.save(cardDto, principal);
    }
}
