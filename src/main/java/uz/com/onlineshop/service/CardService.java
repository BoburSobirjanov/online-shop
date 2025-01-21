package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataHasAlreadyExistException;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.mapper.CardMapper;
import uz.com.onlineshop.model.dto.request.CardDto;
import uz.com.onlineshop.model.dto.response.CardForFrontDto;
import uz.com.onlineshop.model.entity.card.CardEntity;
import uz.com.onlineshop.model.enums.CardType;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.repository.CardRepository;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.standard.StandardResponse;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CardMapper cardMapper;


    public StandardResponse<CardForFrontDto> save(CardDto cardDto, Principal principal){
        checkHasCard(cardDto.getCardNumber());
        CardEntity card = modelMapper.map(cardDto, CardEntity.class);
        if (cardDto.getCardNumber()!=null && cardDto.getCardNumber().length()>16){
            throw new NotAcceptableException("Field value cannot exceed 16 characters!");
        }
        card.setCardNumber(cardDto.getCardNumber());
        card.setExpireDate(cardDto.getExpireDate());
        if (cardDto.getCardBalance()<0){
            throw new NotAcceptableException("Balance can not be less than 0");
        }
        card.setCardBalance(cardDto.getCardBalance());
        try {
            card.setCardType(CardType.valueOf(cardDto.getCardType()));
        }catch (Exception e){
            throw new NotAcceptableException("Wrong card type!");
        }
        card.setUserId(userRepository.findUserEntityByEmail(principal.getName()));
        CardEntity save = cardRepository.save(card);
        CardForFrontDto cardForFrontDto = modelMapper.map(save, CardForFrontDto.class);
        return StandardResponse.ok("Card added!",cardForFrontDto);
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

        return StandardResponse.ok("Card deleted","DELETED");
    }







    public Page<CardForFrontDto> getCardsByType(Pageable pageable, String type){
        Page<CardEntity> cardEntities = cardRepository.findCardEntityByCardType(pageable, type);
        return cardEntities.map(cardMapper::toDto);
    }






    public Page<CardForFrontDto> getAllCards(Pageable pageable){
        Page<CardEntity> cardEntities = cardRepository.findAllCards(pageable);
        return cardEntities.map(cardMapper::toDto);
    }






    public StandardResponse<CardForFrontDto> getById(UUID id){
        CardEntity card = cardRepository.findCardEntityById(id);
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        CardForFrontDto cardForFrontDto = modelMapper.map(card, CardForFrontDto.class);
        return StandardResponse.ok("This is card!",cardForFrontDto);
    }





    public StandardResponse<String> fillCardBalance(UUID id,Double balance){
        CardEntity card = cardRepository.findCardEntityById(id);
        if (card==null){
            throw new DataNotFoundException("Card not found!");
        }
        card.setCardBalance(card.getCardBalance()+balance);
        cardRepository.save(card);
        return StandardResponse.ok("Card's balance filled","FILLED BALANCE");
    }





    public Page<CardForFrontDto> getMyCards(Pageable pageable, Principal principal){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        Page<CardEntity> cards = cardRepository.findAllByUserId(pageable,user);

        return cards.map(cardMapper::toDto);
    }



    public Page<CardForFrontDto> getUsersCards(Pageable pageable, UUID userId){
        UserEntity user = userRepository.findUserEntityById(userId);
        if (user==null){
            throw new DataNotFoundException("User not found!");
        }
        Page<CardEntity> cardEntityByUserId = cardRepository.findCardEntityByUserId(pageable,user);

        return cardEntityByUserId.map(cardMapper::toDto);
    }

}
