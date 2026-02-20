package com.vaibhav.urlshortener.repository;

import com.vaibhav.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
}
