package com.seproject.account.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorizations")
public class Authorization {
    @GeneratedValue @Id
    private Long id;

    private String path;
    private String method;
    private int priority;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "authorization",cascade = CascadeType.ALL)
    private List<RoleAuthorization> roleAuthorizations;

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
