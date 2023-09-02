package io.sultanov.antifraudsystem.domain.ip;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IpService {

    private final IpRepository ipRepository;
    private final IpMapper ipMapper;
    Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    public Ip addSuspiciousIp(IpDto ipDto) {
        if (ipRepository.findByIp(ipDto.getIp()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT);
        Ip ip = ipMapper.toEntity(ipDto);
        return ipRepository.save(ip);
    }

    public DeleteIpResponse deleteIp(String ip) {
        if (!pattern.matcher(ip).matches())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Ip ipCheck = ipRepository.findByIp(ip).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ipRepository.deleteById(ipCheck.getId());
        return new DeleteIpResponse("IP " + ip + " successfully removed!");
    }

    public List<Ip> getAllIps() {
        return ipRepository.findAll();
    }
}
