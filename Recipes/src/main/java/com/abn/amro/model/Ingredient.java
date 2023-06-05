package com.abn.amro.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String ingredient;

    @Getter
    @ManyToMany(
            mappedBy = "recipeIngredients",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Recipe> recipeIngredients;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
