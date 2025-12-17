package com.gustavo.vaultapp.repo;

import com.gustavo.vaultapp.model.RequestEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestEntryRepository extends JpaRepository<RequestEntry, Long> {
}