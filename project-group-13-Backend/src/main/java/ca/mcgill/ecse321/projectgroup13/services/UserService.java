package ca.mcgill.ecse321.projectgroup13.services;

import ca.mcgill.ecse321.projectgroup13.dao.*;

import ca.mcgill.ecse321.projectgroup13.dto.*;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.projectgroup13.model.*;

import java.sql.Date;
import java.sql.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import ca.mcgill.ecse321.projectgroup13.services.exception.*;
import ca.mcgill.ecse321.projectgroup13.services.exception.RegistrationException;

/**
 * Service to handle login and creation of user account.
 */
@Service
public class UserService {
	
	
    @Autowired
    private UserRepository userRepository;
    //TODO must implement password encoder, was causing errors
    //@Autowired
    //private PasswordEncoder passwordEncoder;

    /**
     * service method to create a new user
     * @param userDto
     * @return user
     * @throws RegistrationException
     */
    // @Transactional
    // public User createUser(UserDto userDto) throws RegistrationException {
    //     User user = new User();
    //     //checking if email syntax is valid
    //     if(checkIfValidEmail(userDto.getEmail()) == true){
    //         //must verify that no other user is associated with the same email
    //         if(userRepository.findUserByEmail(userDto.getEmail()) != null) throw new RegistrationException("Email already in use");
    //     }else{
    //         throw new RegistrationException("Invalid Email");
    //     }
    //     //make sure Username is unique
    //     if(userRepository.findUserByUsername(userDto.getUsername()) !=  null)  throw new RegistrationException("Username already in use");
    //     //invalid password -- password must contain at least one letter and one number
    //     if(!userDto.getPassword().matches(".*\\d.*") || !userDto.getPassword().matches(".*[A-z].*")) throw new  RegistrationException("invalid password entered, contain number and letter");
        
    //     //ALL CONDITIONS HAVE PASSED
    //     user.setUsername(userDto.getUsername());
    //     user.setEmail(userDto.getEmail());
    //     //TODO implement the encoder -- was causing errors
    //     //user.setPassword(passwordEncoder.encode(password));
    //     user.setPassword(userDto.getPassword());
    //     user.setArtwork(new HashSet<Artwork>());
    //     userRepository.save(user);
    //     return user;
    // }


    @Transactional
    public User createUser(String username, String email, String password) throws RegistrationException {
        User user = new User();
        if(userRepository.findUserByUsername(username) != null) throw new RegistrationException("Username already in use");
        //checking if email syntax is valid
        //TODO: validate email

//        if(checkIfValidEmail(email) || userRepository.findUserByEmail(email) != null) {
//            throw new RegistrationException("invalid email");
//        }

        //if(userRepository.findUserByEmail(email) != null) throw new RegistrationException("Email already in use");
        //ALL CONDITIONS HAVE PASSED
        user.setUsername(username);
        user.setEmail(email);
        //TODO implement the encoder -- was causing errors
        //user.setPassword(passwordEncoder.encode(password));
        user.setPassword(password);
        user.setArtwork(new HashSet<>());
        user.setOrder(new HashSet<>());
        user.setAddress(new HashSet<>());
        userRepository.save(user);
        return user;
    }
    
    //TODO make class to do password stuff

    /**
     * service method to delete a certain user from Database
     * @param username
     * @throws RegistrationException
     */
    //TODO make sure it checks that its admin
    @Transactional
    public void deleteUser(String username) throws RegistrationException {
    	//TODO: must ensure that objects associated with user get deleted (cart, order, address)
        User user = userRepository.findUserByUsername(username);
        if(user==null) throw new RegistrationException("User does not exist");
        Set<Address> userAddresses = user.getAddress();
        userRepository.delete(user);
    }


    @Transactional
    public User getUserByUsername(String username){
        if(username ==null) throw new IllegalArgumentException("invalid username");
        User user = userRepository.findUserByUsername(username);
        return user;
    }


    /**
     * service method to edit user email
     * @param username
     * @param newEmail
     * @throws RegistrationException
     */
    @Transactional
    public void editEmail(String username, String newEmail) throws RegistrationException {
        User user = userRepository.findUserByUsername(username);
            if(checkIfValidEmail(newEmail) || userRepository.findUserByEmail(newEmail) != null) {
                throw new RegistrationException("invalid email");
            }
                user.setEmail(newEmail);
                userRepository.save(user);
    }

    @Transactional
    public String login(LoginDto loginDto) throws LoginException {
    	//must validate login information
    	//check if username exists
    	User user = userRepository.findUserByUsername(loginDto.getUsername());
    	if (user==null ) throw new LoginException("invalid username");
    	//TODO must implement the password encoder
    	//if (user.getPassword()!=passwordEncoder.encode(loginDto.getPassword())) throw new LoginException("invalid password");
    	if (user.getPassword()!=loginDto.getPassword()) throw new LoginException("invalid password");
    	return createToken(user);
    }
    /**
     * service method to edit user bio
     * @param username
     * @param newBio
     */
    @Transactional
    public void editBio(String username, String newBio){
        User user = userRepository.findUserByUsername(username);
        user.setBio(newBio);
        userRepository.save(user);
    }


    /**
     * service method to edit profile picture of user
     * @param username
     * @param newUrl
     */
    @Transactional
    public void editProfilePictureUrl(String username, String newUrl){
        User user = userRepository.findUserByUsername(username);
        user.setProfilePictureURL(newUrl);
        userRepository.save(user);
    }
    
    @Transactional
    public UserDto getInfo(String username) {
    	User user = userRepository.findUserByUsername(username);
    	if(user == null) return null;
    	return convertToDto(user);
    }
    
   // @Transactional
    //public void editPassword(String username, )

    
    //TODO update token sophistication
    /**
     * Create token upon login, and set it for corresponding user
     * 
     * @param user
     * @return String token 
     */
    private String createToken(User user){
    	String token = UUID.randomUUID().toString();
    	user.setApiToken(token);
    	userRepository.save(user);
    	return token;
    }

    /**
     * Generates a strong temporary password to be used in case of password reset.
     *
     * @return randomly generated password
     */
    //TODO: should this be a public method?
    public String generateRandomPassword() {
        String upperCaseLetters = RandomStringUtils.random(1, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(1, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(1);
        String totalChars = RandomStringUtils.randomAlphanumeric(6);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }



//    //To reset user password
//    @Transactional
//    public String resetPassword(String username) throws RegistrationException {
//        User user = userRepository.findUserByUsername(username);
//        if(user == null) throw new RegistrationException("No user found");
//        //String tmpPassword = randomPassword();  //MUST COMPLETE IMPLEMENTATION
//        String tmpPassword = "Mister1";
//        user.setPassword(tmpPassword);
//        return tmpPassword;
//    }




    //*************** HELPER METHODS ***************//
    
    //iterable to list
    public <T> List<T> toList(Iterable<T> iterable) {
        List<T> lst = new ArrayList<T>();
        for (T t : iterable) {
            lst.add(t);
        }
        
        return lst;
    }

    /*
     * Checks for a valid email using regex from
     * https://stackoverflow.com/questions/8204680/java-regex-email
     *
     */
    private boolean checkIfValidEmail(String email) {
    	String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    	 Pattern pattern = Pattern.compile(regex);
    	 return pattern.matcher(email).matches();

    }
    private PaymentDto convertToDto(Payment e) {
		if (e == null) {
			return null;
//			throw new IllegalArgumentException("There is no such payment!");
		}
		PaymentDto dto = new PaymentDto(e.getPaymentID(), e.getCardNumber(),e.getExpirationDate(),e.getNameOnCard(),e.getCvv());
		return dto;
	}


	private OrderDto convertToDto(Order order) {
		if (order == null) {
			return null;
		//throw new IllegalArgumentException("There is no such order!");
		}

		Set<ArtworkDto> artworksDto = new HashSet<ArtworkDto>();
		for (Artwork artwork : order.getArtwork()) {
			artworksDto.add(convertToDto(artwork));
		}

		ShipmentDto shipmentsDto = convertToDto(order.getShipment());

		OrderDto dto = new OrderDto(order.getOrderID(), order.getTotalAmount(), order.getOrderStatus(), artworksDto, convertToDto(order.getPayment()), shipmentsDto);
		return dto;
	}


	private ArtworkDto convertToDto(Artwork artwork) {
		if (artwork == null) {
			return null;
//			throw new IllegalArgumentException("There is no such artwork!");
		}

		Set<UserDto> artists = new HashSet<UserDto>();
		for (User artist : artwork.getArtist()) {
			artists.add(convertToDto(artist));
		}

		ArtworkDto dto = new ArtworkDto(artwork.getArtworkID(), artwork.isIsOnPremise(),  convertToDto(artwork.getOrder()), artwork.getWorth(), artwork.isArtworkSold(), artwork.getDescription(), artwork.getTitle(), artwork.getCreationDate(), artwork.getDimensions(), artwork.getMedium(), artwork.getCollection(), artwork.getImageUrl());
		return dto;
	}



	private UserDto convertToDto(User user) {
		if (user == null) {
			return null;
//			throw new IllegalArgumentException("There is no such user!");
		}

		Set<ArtworkDto> artworksDto = new HashSet<ArtworkDto>();
		for (Artwork artwork : user.getArtwork()) {
			artworksDto.add(convertToDto(artwork));
		}

		Set<AddressDto> addressDtos = new HashSet<AddressDto>();
		for (Address address : user.getAddress()) {
			addressDtos.add(convertToDto(address));
		}

		Set<OrderDto> ordersDto = new HashSet<OrderDto>();
		for (Order order : user.getOrder()) {
			ordersDto.add(convertToDto(order));
		}

		UserDto dto = new UserDto(convertToDto(user.getCart()), artworksDto, user.getBio(), addressDtos, ordersDto ,user.getUsername(), user.getEmail(), user.getProfilePictureURL());
		return dto;
	}


	private CartDto convertToDto(Cart cart) {
		if (cart == null) {
			return null;
		}

		Set<ArtworkDto> artworksDto = new HashSet<ArtworkDto>();
		for (Artwork artwork : cart.getArtwork()) {
			artworksDto.add(convertToDto(artwork));
		}

		CartDto dto = new CartDto(cart.getCartID(), cart.getTotalCost(), artworksDto);
		return dto;
	}


	private ShipmentDto convertToDto(Shipment shipment) {
		if (shipment == null) {
			return null;
		}
		ShipmentDto dto = new ShipmentDto(shipment.getShipmentID(), shipment.getShipmentInfo(), shipment.getEstimatedDateOfArrival(), shipment.getEstimatedTimeOfArrival(), convertToDto(shipment.getAddress()));
		return dto;
	}

	private AddressDto convertToDto(Address address) {
		if (address == null) {
			//throw new IllegalArgumentException("There is no such address!");
			return null;
		}
		AddressDto dto = new AddressDto(address.getAddressID(), address.getUser().getUsername(), address.getStreetAddress1(), address.getStreetAddress2(), address.getCity(), address.getProvince(), address.getCountry(), address.getPostalCode());
		return dto;
	}
}