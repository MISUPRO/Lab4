package model;

import javax.persistence.*;

@Entity
@Table(name = "pollution")
public class PollutionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pollution;

    private String pollution_level;

    @Column(name = "region_id")
    private int region_id;

    // Геттеры и сеттеры
    public int getId_pollution() {
        return id_pollution;
    }

    public void setId_pollution(int id_pollution) {
        this.id_pollution = id_pollution;
    }

    public String getPollution_level() {
        return pollution_level;
    }

    public void setPollution_level(String pollution_level) {
        this.pollution_level = pollution_level;
    }

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }
}