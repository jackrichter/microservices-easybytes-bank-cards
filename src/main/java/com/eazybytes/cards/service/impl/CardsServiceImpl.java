package com.eazybytes.cards.service.impl;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.entity.Cards;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardsMapper;
import com.eazybytes.cards.repository.CardsRepository;
import com.eazybytes.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;

    /**
     * Only one card with the same mobile number, meaning one card per customer, is allowed.
     *
     * @param mobileNumber Mobile Number of the Customer
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> storedCard = cardsRepository.findByMobileNumber(mobileNumber);
        if (storedCard.isPresent()) {
            throw  new CardAlreadyExistsException("Card already registered with given mobileNumber: " + mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    /**
     * Helper method to create a new card
     *
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();

        Long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);

        return newCard;
    }

    /**
     * @param mobileNumber Input mobile Number
     * @return
     */
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards card = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        return CardsMapper.mapToCardsDto(card, new CardsDto());
    }

    /**
     * @param cardDto - CardsDto Object
     * @return
     */
    @Override
    public boolean updateCard(CardsDto cardDto) {
        Cards card = cardsRepository.findByCardNumber(cardDto.getCardNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardDto.getCardNumber()));
        Cards updatedCard = CardsMapper.mapToCards(cardDto, card);
        cardsRepository.save(updatedCard);
        return true;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards card = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        cardsRepository.delete(card);
        return true;
    }
}
