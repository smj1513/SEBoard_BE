package com.seproject.seboard.domain.model.category;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "external_site_categories")
@NoArgsConstructor
@SuperBuilder
public class ExternalSiteCategory extends Category {
    private String externSiteUrl;

    public void changeExternalSiteUrl(String externalUrl) {
        this.externSiteUrl = externalUrl;
    }
}
