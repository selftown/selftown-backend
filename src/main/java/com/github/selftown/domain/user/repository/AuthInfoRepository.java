package com.github.selftown.domain.user.repository;

import com.github.selftown.domain.user.entity.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {

}
