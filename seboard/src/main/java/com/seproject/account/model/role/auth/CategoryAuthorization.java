package com.seproject.account.model.role.auth;

import com.seproject.seboard.domain.model.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Getter
@NoArgsConstructor
@DiscriminatorValue("CATEGORY")
@Entity
@Table(name = "category_authorizations")
public class CategoryAuthorization extends Authorization {
    @Enumerated(EnumType.STRING)
    private AccessType accessType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    @Override
    public String getType() {
        return "CATEGORY";
    }
}
