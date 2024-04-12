package com.skyapi.weatherapiservice.helper.utils;

import org.modelmapper.ModelMapper;

import com.skyapi.weatherapiservice.helper.string.CommonHeader;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CommonUtility {

    private CommonUtility() {
    }

    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader(CommonHeader.X_FORWARD_FOR);

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        log.info("Client's IP Address: " + ip);

        return ip;
    }

    public static <D, E, M extends ModelMapper> D entity2DTO(Class<D> dto, E entity, M modelMapper) {
        return modelMapper.map(entity, dto);
    }

}
