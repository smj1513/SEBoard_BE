package com.seproject.account.authorization.domain;

import com.seproject.account.authorization.utils.AuthorizationProperty;
import com.seproject.admin.domain.SelectOption;
import com.seproject.board.menu.domain.Menu;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Entity
public class MenuEditAuthorization extends MenuAuthorization{

    @Enumerated(EnumType.STRING)
    private SelectOption selectOption;

    public MenuEditAuthorization(Menu menu) {
        super(menu);
    }

    @Override
    public boolean support(AuthorizationProperty property) {
        return property.equals(AuthorizationProperty.EDITABLE);
    }

    @Override
    public void setSelectOption(SelectOption selectOption) {
        this.selectOption = selectOption;
    }

    @Override
    public SelectOption getSelectOption() {
        return selectOption;
    }
}
