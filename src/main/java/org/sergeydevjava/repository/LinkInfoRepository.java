package org.sergeydevjava.repository;

import org.sergeydevjava.model.LinkInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository extends JpaRepository<LinkInfo, UUID> {

    Optional<LinkInfo> findByShortLinkAndActiveIsTrueAndEndTimeIsAfter(String shortLink, LocalDateTime now);

    @Query("""
            FROM LinkInfo
            WHERE shortLink = :shortLink
            AND active = true
            AND (endTime is NULL OR endTime >= :endTime)
            """)
    Optional<LinkInfo> findActiveShortLink(String shortLink, LocalDateTime endTime);

    @Query("""
            UPDATE LinkInfo
            SET openingCount = openingCount + 1
            WHERE shortLink = :shortLink 
            """)
    @Modifying
    @Transactional
    void incrementOpeningCountByShortLink(String shortLink);

    @Query(value = """
            FROM LinkInfo
            WHERE (:linkPart IS NULL OR lower(link) LIKE '%' || lower(cast(:linkPart AS String)) || '%')
            AND (cast(:endTimeFrom as DATE) IS NULL OR endTime >= :endTimeFrom)
            AND (cast(:endTimeTo as DATE) IS NULL OR endTime <= :endTimeTo)
            AND (:description IS NULL OR lower(description) LIKE '%' || lower(cast(:description AS String)) || '%')
            AND (:active IS NULL OR active = :active)
            """)
    Page<LinkInfo> findByFilter(
            String linkPart,
            LocalDateTime endTimeFrom,
            LocalDateTime endTimeTo,
            String description,
            Boolean active,
            Pageable pageable
            );
}
