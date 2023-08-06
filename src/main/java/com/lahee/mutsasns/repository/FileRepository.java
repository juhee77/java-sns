package com.lahee.mutsasns.repository;

import com.lahee.mutsasns.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
