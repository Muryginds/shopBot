package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.enums.OrderStatus;
import org.telegram.galacticMiniatures.bot.model.ListingCart;
import org.telegram.galacticMiniatures.bot.model.Order;
import org.telegram.galacticMiniatures.bot.model.OrderedListing;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.repository.OrderRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderedListingService orderedListingService;
    private final CartService cartService;
    private final UserService userService;

    public Page<Order> findPageOrderByChatId(Long chatId, Pageable pageable) {
        return orderRepository.findByUser_ChatId(chatId.toString(), pageable);
    }

    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAllByChatId(String chatId){
        return orderRepository.findAllByUser_ChatId(chatId, Sort.by("created").descending());
    }

    @Transactional
    public void createNewOrderWithListings(Long chatId) {
        User user = userService.findUser(chatId).orElse(new User(chatId.toString(), chatId.toString()));
        Order order = new Order(user,
                OrderStatus.CREATED,
                LocalDateTime.now(),
                cartService.getCartSummaryByChatId(chatId).orElse(0));
        orderRepository.save(order);
        List<ListingCart> listingCartList = cartService.findAllByUser(user);
        List<OrderedListing> orderedListingsList = new ArrayList<>();
        for (ListingCart cartElement : listingCartList) {
            orderedListingsList.add(new OrderedListing(
                    new OrderedListing.Key(order,
                            cartElement.getId().getListing(),
                            cartElement.getId().getOption()),
                            cartElement.getQuantity()));
        }
        orderedListingService.saveAll(orderedListingsList);
        cartService.deleteAllByChatId(user);
    }
}