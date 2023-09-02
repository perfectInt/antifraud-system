package io.sultanov.antifraudsystem.api.controllers;

import io.sultanov.antifraudsystem.domain.ip.DeleteIpResponse;
import io.sultanov.antifraudsystem.domain.ip.Ip;
import io.sultanov.antifraudsystem.domain.ip.IpDto;
import io.sultanov.antifraudsystem.domain.ip.IpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/antifraud/suspicious-ip")
public class IpController {

    private final IpService ipService;

    @PostMapping
    @PreAuthorize("hasAuthority('SUPPORT')")
    @ResponseStatus(HttpStatus.CREATED)
    public Ip addSuspiciousIp(@Valid @RequestBody IpDto ipDto) {
        return ipService.addSuspiciousIp(ipDto);
    }

    @DeleteMapping("/{ip}")
    @PreAuthorize("hasAuthority('SUPPORT')")
    @ResponseStatus(HttpStatus.OK)
    public DeleteIpResponse deleteIp(@PathVariable String ip) {
        return ipService.deleteIp(ip);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPPORT')")
    @ResponseStatus(HttpStatus.OK)
    public List<Ip> getAllIps() {
        return ipService.getAllIps();
    }
}
