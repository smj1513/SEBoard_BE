package com.seproject.seboard.domain.model.exposeOptions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="privacies")
public class Privacy extends ExposeOption{
    private String password;
}
