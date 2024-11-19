package uz.pdp.dreamexpressbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Region {
    @Id
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<District> districts;


}
