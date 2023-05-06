package com.seproject.account.model;

import com.seproject.seboard.domain.model.category.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_authorizations")
public class CategoryAuthorization {

    @Id @GeneratedValue
    private Long id;

    private String method;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Role.class)
    private Role role;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Menu.class)
    private Menu menu;

}
