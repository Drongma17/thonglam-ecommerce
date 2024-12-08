package com.thonglam.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 2, name = "role_name")
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
