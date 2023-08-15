package com.seproject.account.authorization.domain;

import com.seproject.account.authorization.utils.AuthorizationProperty;
import com.seproject.admin.domain.SelectOption;
import com.seproject.board.menu.domain.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@Entity
public class MenuManageAuthorization extends MenuAuthorization {

    @Enumerated(EnumType.STRING)
    private SelectOption selectOption;

    public MenuManageAuthorization(Menu menu) {
        super(menu);
    }

    @Override
    public boolean support(AuthorizationProperty property) {
        return property.equals(AuthorizationProperty.MANAGEABLE);
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
