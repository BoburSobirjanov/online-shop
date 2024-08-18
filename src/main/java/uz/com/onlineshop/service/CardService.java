package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataHasAlreadyExistException;
import uz.com.onlineshop.exception.DataNotFoundException;
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
import java.time.LocalDateTime;
import java.util.UUID;

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
        card.setExpireDate(cardDto.getExpireDate());
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








    public StandardResponse<String> delete(UUID id, Principal principal){
        CardEntity card = cardRepository.findCardEntityById(id);
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        card.setDeleted(true);
        card.setDeletedTime(LocalDateTime.now());
        card.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        cardRepository.save(card);

        return StandardResponse.<String>builder()
                .data("DELETED")
                .status(Status.SUCCESS)
                .message("Card has deleted!")
                .build();
    }







    public Page<CardForFront> getCardsByType(Pageable pageable, String type){
        Page<CardEntity> cardEntities = cardRepository.findCardEntityByCardType(pageable, type);
        return cardEntities.map(cardEntity -> new CardForFront(cardEntity.getId(), cardEntity.getCardNumber(),
                cardEntity.getCardType(), cardEntity.getExpireDate()));
    }






    public Page<CardForFront> getAllCards(Pageable pageable){
        Page<CardEntity> cardEntities = cardRepository.findAllCards(pageable);
        return cardEntities.map(cardEntity -> new CardForFront(cardEntity.getId(), cardEntity.getCardNumber(),
                cardEntity.getCardType(), cardEntity.getExpireDate()));
    }






    public StandardResponse<CardForFront> getById(UUID id){
        CardEntity card = cardRepository.findCardEntityById(id);
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        CardForFront cardForFront = modelMapper.map(card, CardForFront.class);
        return StandardResponse.<CardForFront>builder()
                .data(cardForFront)
                .status(Status.SUCCESS)
                .message("This is card")
                .build();
    }





    public StandardResponse<String> fillCardBalance(UUID id,Double balance){
        CardEntity card = cardRepository.findCardEntityById(id);
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        card.setCardBalance(card.getCardBalance()+balance);
        return StandardResponse.<String>builder()
                .data("FILLED BALANCE")
                .status(Status.SUCCESS)
                .message("Card's balance filled!")
                .build();
    }

}
