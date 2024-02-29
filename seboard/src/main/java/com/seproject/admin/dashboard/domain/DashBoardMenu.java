package com.seproject.admin.dashboard.domain;

import com.seproject.account.role.domain.Role;
import com.seproject.admin.domain.SelectOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class DashBoardMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;

    @Enumerated(EnumType.STRING)
    private DashBoardMenuGroup menuGroup;

    @Enumerated(EnumType.STRING)
    private SelectOption selectOption;

    @OneToMany(mappedBy = "dashBoardMenu", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DashBoardMenuAuthorization> dashBoardMenuAuthorizations = new ArrayList<>();

    public boolean authorize(List<Role> roles) {

        for (DashBoardMenuAuthorization authorization : dashBoardMenuAuthorizations) {
            if(authorization.authorize(roles)) {
                return true;
            }
        }

        return false;
    }

    public void update(SelectOption selectOption, List<DashBoardMenuAuthorization> dashBoardMenuAuthorizations) {
        this.selectOption = selectOption;
        this.dashBoardMenuAuthorizations.clear();
        this.dashBoardMenuAuthorizations.addAll(dashBoardMenuAuthorizations);
    }


}
