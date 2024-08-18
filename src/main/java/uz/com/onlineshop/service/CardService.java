package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataHasAlreadyExistException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.model.dto.request.CardDto;
import uz.com.onlineshop.model.dto.response.CardForFront;
import uz.com.onlineshop.model.entity.card.CardEntity;
import uz.com.onlineshop.model.entity.card.CardType;
import uz.com.onlineshop.repository.CardRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public StandardResponse<CardForFront> save(CardDto cardDto, Principal principal){
        checkHasCard(cardDto.getCardNumber());
        CardEntity card = modelMapper.map(cardDto, CardEntity.class);
        card.setCardNumber(cardDto.getCardNumber());
        card.setCardBalance(cardDto.getCardBalance());
        try {
            card.setCardType(CardType.valueOf(cardDto.getCardType()));
        }catch (Exception e){
            throw new NotAcceptableException("Wrong card type!");
        }
        card.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        CardEntity save = cardRepository.save(card);
        CardForFront cardForFront = modelMapper.map(save, CardForFront.class);
        return StandardResponse.<CardForFront>builder()
                .data(cardForFront)
                .status(Status.SUCCESS)
                .message("Card added!")
                .build();
    }



    public void checkHasCard(String number){
        CardEntity card = cardRepository.findCardEntityByCardNumber(number);
        if (card!=null){
            throw new DataHasAlreadyExistException("Card has already added!");
        }
    }
}
