package io.sultanov.antifraudsystem.domain.ip;

import org.springframework.stereotype.Component;

@Component
public class IpMapper {

    public Ip toEntity(IpDto ipDto) {
        Ip ip = new Ip();
        ip.setIp(ipDto.getIp());
        return ip;
    }
}
