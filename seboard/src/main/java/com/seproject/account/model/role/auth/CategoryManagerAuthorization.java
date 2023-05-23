package com.seproject.account.model.role.auth;

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

    @Override
    public String getType() {return "CATEGORY_MANAGER";}
}
