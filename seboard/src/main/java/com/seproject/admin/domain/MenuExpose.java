package com.seproject.admin.domain;

import com.seproject.account.model.role.Role;
import com.seproject.admin.controller.AccessOption;
import com.seproject.seboard.domain.model.category.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_expose")
public class MenuExpose {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private AccessOption accessOption;

    public void changeRole(Role role) {
        this.role = role;
    }

}
