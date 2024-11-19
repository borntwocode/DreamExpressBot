package uz.pdp.dreamexpressbot.entity;//package uz.pdp.dreamexpressbot.entity;
//
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.util.UUID;
//
//@Data
//@AllArgsConstructor
//public class District {
//
//    private UUID id;
//    @ManyToOne
//    @JoinColumn(name = "city_id", nullable = false)
//    private City city;
//    private String name;
//
//}


import jakarta.persistence.*;
import lombok.*;
import uz.pdp.dreamexpressbot.entity.Region;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class District {
    @Id
    private UUID id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

}
