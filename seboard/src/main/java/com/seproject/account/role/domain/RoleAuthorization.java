package com.seproject.account.role.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorization_metadata")
public class RoleAuthorization {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "authorization_id")
    private Authorization authorization;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
