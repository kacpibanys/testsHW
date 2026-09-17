package com.example.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*

    4.Powinien rzucić wyjątek dla pustego koszyka
    5.Powinien rzucić wyjątek gdy produkt nie istnieje
*/
@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountService discountService;

//    @Spy
//    private List<CartItem> items = new ArrayList<>();

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    //1.Powinien obliczyć cenę dla jednego produktu bez rabatu
    @Test
    void shouldCalculateOneProductsPriceWithoutDiscount(){
        //Arrange
        CartItem koszykNaJeden = new CartItem("jd-67", 1);

        List<CartItem> items = List.of(koszykNaJeden);

        String customerId = "klient-pan";

        when(productRepository.findById("jd-67"))
                .thenReturn(Optional.of(new Product("jd-67", "JedenProduct", 13.67)));
        when(discountService.getDiscountForCustomer("klient-pan"))
                .thenReturn(0.0);  // brak rabatu

        //Act
        double total = shoppingCartService.calculateTotal(customerId, items);

        //Assert
        assertEquals(13.67, total);
        verify(productRepository).findById("jd-67");
        verify(discountService).getDiscountForCustomer(customerId);
    }

    //2.Powinien zastosować 10% rabatu dla klienta VIP
    @Test
    void shouldApplyTenPercentDiscountForAVipCustomer(){
        //Arrange
        CartItem koszykVip = new CartItem("vip-prod", 1);

        List<CartItem> items = List.of(koszykVip);

        String customerId = "klient-vip";

        when(productRepository.findById("vip-prod"))
                .thenReturn(Optional.of(new Product("vip-prod", "ProduktPremium", 10.00)));
        when(discountService.getDiscountForCustomer("klient-vip"))
                .thenReturn(0.10);  // brak rabatu

        //Act
        double total = shoppingCartService.calculateTotal(customerId, items);

        //Assert
        assertEquals(9.00, total);
        verify(productRepository).findById("vip-prod");
        verify(discountService).getDiscountForCustomer(customerId);
    }

    //3.Powinien obliczyć cenę dla wielu produktów
    @Test
    void shouldCalculateForMultipleProducts(){
        //Arrange
        CartItem item1 = new CartItem("prod-A", 2);
        CartItem item2 = new CartItem("prod-B", 3);

        List<CartItem> items = List.of(item1, item2);

        String customerId = "klient-testowy";

        when(productRepository.findById("prod-A"))
                .thenReturn(Optional.of(new Product("prod-A", "Produkt A", 15.00)));
        when(productRepository.findById("prod-B"))
                .thenReturn(Optional.of(new Product("prod-B", "Produkt B", 10.00)));

        when(discountService.getDiscountForCustomer(customerId))
                .thenReturn(0.0);
        //Act
        double total = shoppingCartService.calculateTotal(customerId, items);

        //Assert
        assertEquals(60.00, total);
        verify(productRepository).findById("prod-A");
        verify(productRepository).findById("prod-B");
        verify(discountService).getDiscountForCustomer(customerId);
    }

    //4.Powinien rzucić wyjątek dla pustego koszyka
    @Test
    void shouldThrowExceptionForEmptyCart() {
        //Arrange
        List<CartItem> emptyItems = new ArrayList<>();
        String customerId = "dowolny-klient";

        //Act

        //Assert
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.calculateTotal(customerId, emptyItems);
        });
        verifyNoInteractions(productRepository);
        verifyNoInteractions(discountService);
    }

    //5.Powinien rzucić wyjątek gdy produkt nie istnieje
    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {
        //Arrange
        String missingProductId = "nieistniejacy-produkt";
        CartItem missingItem = new CartItem(missingProductId, 1);
        List<CartItem> items = List.of(missingItem);
        String customerId = "dowolny-klient";

        when(productRepository.findById(missingProductId))
                .thenReturn(Optional.empty());

        //Act
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            shoppingCartService.calculateTotal(customerId, items);
        });

        //Assert
        assertEquals(missingProductId, exception.getProductId());
        assertEquals("Produkt nie znaleziony: " + missingProductId, exception.getMessage());

        verify(productRepository).findById(missingProductId);
    }
}