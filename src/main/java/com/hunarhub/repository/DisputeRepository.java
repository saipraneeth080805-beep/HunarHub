package com.hunarhub.repository;

import com.hunarhub.entity.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long> {
    List<Dispute> findByStatus(Dispute.DisputeStatus status);
    List<Dispute> findByReporterId(Long reporterId);
    List<Dispute> findByReportedUserId(Long reportedUserId);
    long countByStatus(Dispute.DisputeStatus status);
}
