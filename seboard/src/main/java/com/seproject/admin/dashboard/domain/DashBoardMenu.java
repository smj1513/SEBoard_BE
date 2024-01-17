package com.seproject.admin.dashboard.domain;

import com.seproject.account.role.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Table(name = "dash_board_menus")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DashBoardMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;

    @Enumerated(EnumType.STRING)
    private DashBoardMenuGroup group;


    @OneToMany(mappedBy = "dashBoardMenu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DashBoardMenuAuthorization> dashBoardMenuAuthorizations;

    public boolean authorize(List<Role> roles) {

        for (DashBoardMenuAuthorization authorization : dashBoardMenuAuthorizations) {
            if(authorization.authorize(roles)) {
                return true;
            }
        }

        return false;
    }

    public void update(List<DashBoardMenuAuthorization> dashBoardMenuAuthorizations) {
        this.dashBoardMenuAuthorizations.clear();
        this.dashBoardMenuAuthorizations.addAll(dashBoardMenuAuthorizations);
    }


}
