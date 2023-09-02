package io.sultanov.antifraudsystem.domain.ip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IpRepository extends JpaRepository<Ip, Long> {

    Optional<Ip> findByIp(String ip);
}
