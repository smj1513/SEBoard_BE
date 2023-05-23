package com.seproject.account.model.role.auth;

import com.seproject.account.model.role.RoleAuthorization;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="dtype")
@Entity
@Table(name = "authorizations")
public abstract class Authorization {
    @GeneratedValue @Id
    private Long id;

    private String path;
    private String method;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "authorization",cascade = CascadeType.ALL)
    private List<RoleAuthorization> roleAuthorizations;

    public abstract String getType();

    public void setRoleAuthorizations(List<RoleAuthorization> roleAuthorizations) {
        this.roleAuthorizations = roleAuthorizations;
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
