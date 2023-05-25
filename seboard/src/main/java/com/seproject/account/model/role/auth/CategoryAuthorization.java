package com.seproject.account.model.role.auth;

import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.admin.controller.AccessOption;
import com.seproject.seboard.domain.model.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@DiscriminatorValue("CATEGORY")
@Entity
@Table(name = "category_authorizations")
public class CategoryAuthorization extends Authorization {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Enumerated(EnumType.STRING)
    private AccessOption accessOption;

    @Builder
    public CategoryAuthorization(Long id,String path,String method, List<RoleAuthorization> roleAuthorizations, Category category) {
        super(id,path,method,roleAuthorizations);
        this.category = category;
        accessOption = AccessOption.ALL;
    }

    @Override
    public String getType() {
        return "CATEGORY";
    }

    public void changeOption(AccessOption accessOption) {
        this.accessOption = accessOption;
    }
}
