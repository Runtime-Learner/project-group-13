package ca.mcgill.ecse321.projectgroup13.dao;


import ca.mcgill.ecse321.projectgroup13.model.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration

/**
 *  The TestArt class implements JUnit for reading and writing carts to the database
 *
 */

public class TestCart {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    
    // this is to clear database prior to every run
    
    /**
     *  Deletes all information from cartRepository and
     *  userRepository
     */
 
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	userRepository.deleteAll();
    	cartRepository.deleteAll();
    }
 
    /**
     * Initialize database creates instances of user and cart
     * populates them with test information, saves them to the database
     * and tests that they were successfully saved. 
     */
    
    @Test
    public void testPersistAndLoadCart() {
    	//need instances of these classes
	    User user = new User();
	    Cart cart = new Cart();
	    
	    
	    //parameters for user
	    String username = "testUser21";
	    String password = "passW0rd";
	    String email = "me@home.com";
	    String profilePicURL = "//yes.com/img.jpg";
	    Set<Address> addrs= new HashSet<>();
	    Set<Artwork> artworks= new HashSet<>();
	    Set<Order> orders= new HashSet<>();
	    	
	    	
	    //parameters for cart
	    //Integer cartID = "TESTcartID".hashCode();
	    double totalCost = 100.1;
	    Set<Artwork> artworksInCart = new HashSet<>();
	    
	    //set user parameters
	    user.setUsername(username);
	    user.setPassword(password);
	    user.setEmail(email);
	    user.setProfilePictureURL(profilePicURL);
	    //user.setCart(cart);
	    user.setAddress(addrs);
	    user.setOrder(orders);
	    user.setArtwork(artworks);
	    
	    
	    //set cart parameters
	    //cart.setCartID(cartID);
	    cart.setTotalCost(totalCost);
	    cart.setUser(user);
	    cart.setArtwork(artworksInCart);
	    
	    //save instances to database 
	    user = userRepository.save(user);
	    cart = cartRepository.save(cart);

		System.out.println(cart.getCartID());
        //restore address instance from database
        Cart cartPersisted = cartRepository.findCartByCartID(cart.getCartID());
		System.out.println(cartPersisted.getCartID());
        //assert if instance retrieved from database equals the original
        assertEquals(true, cart.equals(cartPersisted));
    }
    
    
}