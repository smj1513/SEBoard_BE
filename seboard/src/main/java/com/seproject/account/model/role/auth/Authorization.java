package com.seproject.account.model.role.auth;

import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.admin.domain.AccessOption;
import com.seproject.admin.domain.SelectOption;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorizations")
public class Authorization {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;

    @Enumerated(EnumType.STRING)
    private SelectOption selectOption;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "authorization",cascade = CascadeType.ALL)
    private List<RoleAuthorization> roleAuthorizations;

    public void setRoleAuthorizations(List<RoleAuthorization> roleAuthorizations) {
        this.roleAuthorizations = roleAuthorizations;
    }

    public void setSelectOption(SelectOption selectOption) {
        this.selectOption = selectOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authorization that = (Authorization) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
