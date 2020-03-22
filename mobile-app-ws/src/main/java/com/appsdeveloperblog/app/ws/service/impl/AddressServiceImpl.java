package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressesRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<AddressDto>();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity==null) {
            return returnValue;
        }

        List<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

        if (addresses!=null && !addresses.isEmpty()){
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<AddressDto>>() {}.getType();
            returnValue = modelMapper.map(addresses, listType);
        }

        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressEntity address = addressRepository.findByAddressId(addressId);

        if (address == null){
            throw new RuntimeException("Address not found");
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(address, AddressDto.class);
    }
}
