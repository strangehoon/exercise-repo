package com.whatever_market.app.part_time.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private Long user_id;
    private Long recruit_post_id;
    private String reason;
}
