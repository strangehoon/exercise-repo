package com.whatever_market.app.part_time.service.implement;

import com.whatever_market.app.bible.exception.InvalidRequestException;
import com.whatever_market.app.part_time.dto.RecruitPostRequestDTO;
import com.whatever_market.app.part_time.dto.ReportRequestDto;
import com.whatever_market.app.part_time.model.RecruitPost;
import com.whatever_market.app.part_time.model.Report;
import com.whatever_market.app.part_time.repository.RecruitPostRepository;
import com.whatever_market.app.part_time.repository.ReportRepository;
import com.whatever_market.app.part_time.service.RecruitPostService;
import com.whatever_market.app.bible.exception.NotFoundException;
import com.whatever_market.app.user.model.User;
import com.whatever_market.app.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitPostServiceImpl implements RecruitPostService {
    private final RecruitPostRepository recruitPostRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RecruitPost> getAllRecruitPosts(Pageable pageable) {
        return recruitPostRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public RecruitPost getRecruitPostById(Long id) {
        return recruitPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Recruit post not found"));
    }

    @Override
    public RecruitPost createRecruitPost(RecruitPostRequestDTO recruitPostRequestDTO) {
        RecruitPost recruitPost = new RecruitPost();

        recruitPost.setTitle(recruitPostRequestDTO.getTitle());
        recruitPost.setContent(recruitPostRequestDTO.getContent());
        recruitPost.setWorkTime(recruitPostRequestDTO.getWorkTime());
        recruitPost.setHourlyWage(recruitPostRequestDTO.getHourlyWage());
        recruitPost.setLocation(recruitPostRequestDTO.getLocation());

        return recruitPostRepository.save(recruitPost);
    }

    @Override
    public RecruitPost updateRecruitPost(Long id, RecruitPostRequestDTO recruitPostRequestDTO) {
        RecruitPost recruitPost = recruitPostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RecruitPost not found"));

        recruitPost.setTitle(recruitPostRequestDTO.getTitle());
        recruitPost.setContent(recruitPostRequestDTO.getContent());
        recruitPost.setWorkTime(recruitPostRequestDTO.getWorkTime());
        recruitPost.setHourlyWage(recruitPostRequestDTO.getHourlyWage());
        recruitPost.setLocation(recruitPostRequestDTO.getLocation());

        return recruitPostRepository.save(recruitPost);
    }

    @Override
    public void deleteRecruitPostById(Long id) {
        RecruitPost recruitPost = recruitPostRepository.findById(id).orElseThrow
                (()-> new NotFoundException("Recruit post not found"));
        recruitPostRepository.delete(recruitPost);
    }

    // API : 신고하기
    // 유저는 동일한 알바 모집 글에 대한 신고를 1번만 할 수 있음,
    @Override
    public void reportProduct(ReportRequestDto requestDto) {
        if (reportRepository.existsByRecruitPostIdAndUserId(requestDto.getRecruit_post_id(), requestDto.getUser_id())) {
            throw new InvalidRequestException("You have already reported this product.");
        }
        User user = userRepository.findById(requestDto.getUser_id())
                .orElseThrow(() -> new NotFoundException("User not found"));
        RecruitPost recruitPost = recruitPostRepository.findById(requestDto.getRecruit_post_id())
                .orElseThrow(() -> new NotFoundException("RecruitPost not found"));

        Report report = new Report();
        report.setRecruitPost(recruitPost);
        report.setUser(user);
        report.setReason(requestDto.getReason());

        reportRepository.save(report);
        if (reportRepository.countByRecruitPostId(recruitPost.getId()) >= 5) {
            reportRepository.deleteByRecruitPostId(recruitPost.getId());
            recruitPostRepository.delete(recruitPost);
        }
    }
}
