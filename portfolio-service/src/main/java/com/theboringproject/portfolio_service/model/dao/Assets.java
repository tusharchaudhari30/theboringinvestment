package com.theboringproject.portfolio_service.model.dao;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "assets")
public class Assets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "date_of_listing")
    @Temporal(TemporalType.DATE)
    private Date dateOfListing;

    @Column(unique = true)
    private String isin;

    public Assets(String symbol, String assetName, Date dateOfListing, String isin) {
        this.symbol = symbol;
        this.assetName = assetName;
        this.dateOfListing = dateOfListing;
        this.isin = isin;
    }

    public Assets() {
    }
}
