package api.stock.wine.entity;

import api.stock.wine.enums.WineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WineType type;

    @Column(nullable = false)
    private String grape;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private int quantity;

}
