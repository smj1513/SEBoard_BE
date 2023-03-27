package com.seproject.seboard.domain.model.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
@Table(name = "categories")
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "super_category_id")
    private Category superCategory;
    private String name;

    public void changeName(String name) {
        //TODO : validation
        this.name = name;
    }
}
