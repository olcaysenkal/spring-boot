package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "sadasfdsfs";
    String encryptedPassword = "sfdsfdsgds";
    UserEntity userEntity = null;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Olcay");
        userEntity.setLastName("Senkal");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("wqdeqwesfdsd");
        userEntity.setAddresses(getAddressesEntity());
    }

    @Test
    void getUser() {


        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Olcay", userDto.getFirstName());
    }

    @Test
    void getUser_UserNameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UserServiceException.class, () -> {
            userService.getUser("test@test.com");
        });
    }

    @Test
    void createUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("dsadsadsafsd");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("Olcay");
        userDto.setLastName("Senkal");
        userDto.setPassword("123456");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressesDto());

        UserDto storedUserDetails = userService.createUser(userDto);

        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(userEntity.getAddresses().size(), storedUserDetails.getAddresses().size());
        verify(utils, times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder,times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUser_UserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("Olcay");
        userDto.setLastName("Senkal");
        userDto.setPassword("123456");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressesDto());

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(userDto);
        });
    }

    private List<AddressDto> getAddressesDto() {
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Istanbul");
        addressDto.setCountry("Turkey");
        addressDto.setStreet("Huseyin Ayanoglu");
        addressDto.setPostalCode("34742");
        addressDto.setType("shipping");

        AddressDto billingAddressDto = new AddressDto();
        addressDto.setCity("London");
        addressDto.setCountry("UK");
        addressDto.setPostalCode("N15 4DW");
        addressDto.setType("billing");

        List<AddressDto> addressList = new ArrayList<AddressDto>();
        addressList.add(addressDto);
        addressList.add(billingAddressDto);
        return addressList;
    }

    private List<AddressEntity> getAddressesEntity(){

        List<AddressDto> addressDtoList = getAddressesDto();

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();

        List<AddressEntity>  addressEntityList = modelMapper.map(addressDtoList,listType);

        return addressEntityList;
    }

}