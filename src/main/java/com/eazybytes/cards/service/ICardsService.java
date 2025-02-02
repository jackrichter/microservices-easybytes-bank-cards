package com.eazybytes.cards.service;

import com.eazybytes.cards.dto.CardsDto;

public interface ICardsService {

    /**
     * Create card.
     *
     * @param mobileNumber Mobile Number of the Customer
     */
    void createCard(String mobileNumber);

    /**
     * Fetch card details.
     *
     * @param mobileNumber Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);

    /**
     * Update card details.
     *
     * @param cardDto - CardsDto Object
     * @return indicating if the update of card details is successful or not
     */
    boolean updateCard(CardsDto cardDto);

    /**
     * Delete card.
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    boolean deleteCard(String mobileNumber);
}
