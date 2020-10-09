package ca.mcgill.ecse321.projectgroup13.doa;

import ca.mcgill.ecse321.projectgroup13.dao.AddressRepository;
import ca.mcgill.ecse321.projectgroup13.dao.ArtworkRepository;
import ca.mcgill.ecse321.projectgroup13.dao.CartRepository;
import ca.mcgill.ecse321.projectgroup13.dao.UserRepository;
import ca.mcgill.ecse321.projectgroup13.model.Address;
import ca.mcgill.ecse321.projectgroup13.model.Artwork;
import ca.mcgill.ecse321.projectgroup13.model.Cart;
import ca.mcgill.ecse321.projectgroup13.model.User;

import java.util.Iterator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration

public class UserTest {
    @Autowired
    private UserRepository userRepository;
    //Remove this
    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CartRepository cartRepository;
    //Remove ends here
    

    // this is to clear database prior to every run
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        addressRepository.deleteAll();
        artworkRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }
    
//    public void initDatabase() {
//    	System.out.println("test1");
//        String username = "TestUser";
//        String cartID = "123";
//
//        // First example for object save/load
//        User user = new User();
//        user.setUsername(username);
//       // userRepository.save(user);
//        //create address
////        Address address = new Address();
////        address.setCity("Montreal");
////        address.setCountry("Canada");
////        address.setStreetAddress1("3302 St-Catherine");
////        address.setAddressID("3732St-Catherine");
////        address.setUser(user);
////        Set<Address> addrs= new HashSet<>();
////        addrs.add(address);
////        user.setAddress(addrs);
////        addressRepository.save(address);
//        
//        
//        
//        //create some art
//        
////        //create the cart
//        Cart cart = new Cart();
//        cart.setCartID(cartID);
//        cart.setUser(user);
//        user.setCart(cart);
//        userRepository.save(user);
//        cartRepository.save(cart);
//    }
    
    public void initDatabase() {
    	//    Artwork<---User-->Address
    	//create address
    	
    	User artist = new User();
    	artist.setUsername("Julius Cesar Arnouk");
    	artist.setEmail("JCesar@RussianBrides.com");
    	userRepository.save(artist);
    	
    	Address address = new Address();
    	address.setUser(artist);
    	address.setCity("Montreal");
    	address.setCountry("Canada");
    	address.setStreetAddress1("3302 St-Catherine");
    	address.setAddressID("3732St-Catherine");
    	Set<Address> addresss = new HashSet<>();
    	addresss.add(address);
    	
    	//create Artwork
    	Artwork artwork = new Artwork();
    	artwork.setTitle("Beauty");
    	artwork.setArtworkID("Beauty");
    	Set<User> artists = new HashSet<User>();
    	
    	Set<Artwork> artworks = new HashSet<Artwork>();
    	
    	artists.add(artist);
    	artworks.add(artwork);
    	System.out.println("ERROR DETECTOR PASSED");
    	addressRepository.save(address);
    	
    	artwork.setArtist(artists);
    	artist.setArtwork(artworks);
    	artworkRepository.save(artwork);
    	userRepository.save(artist);
    	
    	
    }
    
    @Test
    public void testPersistAndLoadUser() {
    	initDatabase();
    	//get artwork beauty from repo
    	Artwork artwork=artworkRepository.findArtworkByArtworkID("Beauty");
    	Set<User> artists= artwork.getArtist();
    	Iterator<User> iter = artists.iterator();
        //assertEquals(user.getUsername(),"TestUser");
    	User artist = iter.next();
    	//now check if this username is same as the one it should be 
    	String email = artist.getEmail();
    	User querryUser = userRepository.findUserByEmail(email);
    	assertEquals(querryUser.getUsername(),"Julius Cesar Arnouk");
    	
    }
    
   
    
}