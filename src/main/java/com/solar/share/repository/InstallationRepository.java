package com.solar.share.repository;

import com.solar.share.entity.Installation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallationRepository extends JpaRepository<Installation, Long> {
}