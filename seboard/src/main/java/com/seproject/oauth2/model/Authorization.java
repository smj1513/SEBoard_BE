package com.seproject.oauth2.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorization_metadata" , joinColumns = {
            @JoinColumn(name = "authorization_id")
    }, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

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
