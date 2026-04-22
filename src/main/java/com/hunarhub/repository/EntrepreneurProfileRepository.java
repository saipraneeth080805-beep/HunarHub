package com.hunarhub.repository;

import com.hunarhub.entity.EntrepreneurProfile;
import com.hunarhub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrepreneurProfileRepository extends JpaRepository<EntrepreneurProfile, Long> {
    Optional<EntrepreneurProfile> findByUser(User user);
    List<EntrepreneurProfile> findByApprovalStatus(EntrepreneurProfile.ApprovalStatus status);
    Page<EntrepreneurProfile> findByApprovalStatus(EntrepreneurProfile.ApprovalStatus status, Pageable pageable);
    boolean existsByUser(User user);
    
    @Query("SELECT ep FROM EntrepreneurProfile ep WHERE ep.approvalStatus = :status " +
           "AND (LOWER(ep.user.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(ep.skills) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(ep.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EntrepreneurProfile> findByApprovalStatusAndSearch(
        @Param("status") EntrepreneurProfile.ApprovalStatus status,
        @Param("search") String search,
        Pageable pageable
    );
    
    long countByApprovalStatus(EntrepreneurProfile.ApprovalStatus status);
}
