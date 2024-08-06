package com.whatever_market.app.part_time.service;

import com.whatever_market.app.part_time.dto.RecruitPostRequestDTO;
import com.whatever_market.app.part_time.dto.ReportRequestDto;
import com.whatever_market.app.part_time.model.RecruitPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitPostService {
    Page<RecruitPost> getAllRecruitPosts(Pageable pageable);
    RecruitPost getRecruitPostById(Long id);
    RecruitPost createRecruitPost(RecruitPostRequestDTO recruitPostRequestDTO);
    RecruitPost updateRecruitPost(Long id, RecruitPostRequestDTO recruitPostRequestDTO);
    void deleteRecruitPostById(Long id);
    void reportProduct(ReportRequestDto requestDto);
}