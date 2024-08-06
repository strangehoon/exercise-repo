package com.whatever_market.app.user.model;

import com.whatever_market.app.bible.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user__users")
public class User extends BaseModel{

    @Column(name = "name", length = 256)
    private String name;
}