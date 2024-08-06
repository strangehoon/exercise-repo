package com.whatever_market.app.part_time.model;

import com.whatever_market.app.bible.model.BaseModel;
import com.whatever_market.app.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "part_time__reports")
public class Report extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recruit_post_id", nullable = false)
    private RecruitPost recruitPost;

    @Column(name = "reason", length = 1024)
    private String reason;
}
