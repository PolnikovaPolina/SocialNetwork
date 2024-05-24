package com.socialnetwork.repository;

import com.socialnetwork.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> 
{
    List<Publication> findByAccountId(Long accountId);
}