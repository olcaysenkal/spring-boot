package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    List<AddressDto> getAddresses(String userId);

}
