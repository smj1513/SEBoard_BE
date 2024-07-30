package com.seproject.account.authorization.domain;

import com.seproject.account.authorization.utils.AuthorizationProperty;
import com.seproject.account.role.domain.Role;
import com.seproject.account.role.domain.RoleAuthorization;
import com.seproject.admin.domain.SelectOption;
import com.seproject.board.menu.domain.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class MenuExposeAuthorization extends MenuAuthorization {


    @Enumerated(EnumType.STRING)
    private SelectOption selectOption;


    public MenuExposeAuthorization(Menu menu) {
        super(menu);
        this.selectOption = SelectOption.ALL;
    }
    @Override
    public void setSelectOption(SelectOption selectOption) {
        this.selectOption = selectOption;
    }

    @Override
    public SelectOption getSelectOption() {
        return selectOption;
    }

    @Override
    public AuthorizationProperty getAuthorizationProperty() {
        return AuthorizationProperty.EXPOSE;
    }
}
