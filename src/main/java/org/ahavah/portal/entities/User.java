package org.ahavah.portal.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;



@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 126)
    private String name;

    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    private String password;

    @Column(name = "division", nullable = false, length = Integer.MAX_VALUE)
    private String division;

    @Column(name = "role", length = Integer.MAX_VALUE)
    private String role;

    @Builder.Default
    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Builder.Default
    @Column(name = "passchanged")
    private Boolean passchanged = false;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "phonenum", nullable = false)
    private String phone_num;

    @Column(name = "additional", length = Integer.MAX_VALUE)
    private String additional;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (passchanged == null) passchanged = false;
        if (role == null) role = "Student";
        if (password == null) password = "defaultPassword"; // Set a default password if none provided
    }
}