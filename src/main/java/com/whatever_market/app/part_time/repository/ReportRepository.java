package com.whatever_market.app.part_time.repository;

import com.whatever_market.app.part_time.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByRecruitPostIdAndUserId(Long recruitPostId, Long userId);
    long countByRecruitPostId(Long recruitPostId);
    void deleteByRecruitPostId(Long recruitPostId);
}
