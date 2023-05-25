package com.seproject.account.model.role.auth;

import com.seproject.admin.controller.AccessOption;
import com.seproject.seboard.domain.model.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Getter
@NoArgsConstructor
@DiscriminatorValue("CATEGORY_MANAGER")
@Entity
@Table(name = "category_manager_authorizations")
public class CategoryManagerAuthorization extends Authorization {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private AccessOption accessOption;

    @Override
    public String getType() {return "CATEGORY_MANAGER";}

    public void changeOption(AccessOption accessOption) {
        this.accessOption = accessOption;
    }
}
