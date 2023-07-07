package com.seproject.admin.domain;

import com.seproject.account.role.domain.Role;
import com.seproject.board.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_authorization")
public class MenuAuthorization {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private AccessOption accessOption;

    @Enumerated(EnumType.STRING)
    private SelectOption selectOption;

    public boolean writable(Collection<? extends GrantedAuthority> authorities) {
        if(accessOption != AccessOption.WRITE) return false;
        boolean flag = false;
        for (GrantedAuthority authority : authorities) {
            flag |= authority.getAuthority().equals(role.getAuthority());
        }
        return flag;
    }

    public boolean exposable(Collection<? extends GrantedAuthority> authorities) {
        if(accessOption != AccessOption.EXPOSE) return false;
        boolean flag = false;
        for (GrantedAuthority authority : authorities) {
            flag |= authority.getAuthority().equals(role.getAuthority());
        }
        return flag;
    }

    public boolean manageable(Collection<? extends GrantedAuthority> authorities) {
        if(accessOption != AccessOption.MANAGE) return false;
        boolean flag = false;
        for (GrantedAuthority authority : authorities) {
            flag |= authority.getAuthority().equals(role.getAuthority());
        }
        return flag;
    }

}
