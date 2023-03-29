package com.seproject.seboard.domain.model.post;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Attachment {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String fullPath;

}
