package org.ahavah.portal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 10)
    @NotNull
    @Column(name = "language", nullable = false, length = 10)
    private String language;

    @NotNull
    @Column(name = "boilerplate", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> boilerplate;

    @NotNull
    @Column(name = "test_cases", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> testCases;

    @NotNull
    @Column(name = "schedule", nullable = false)
    private OffsetDateTime schedule;

    @NotNull
    @Column(name = "division", nullable = false, length = Integer.MAX_VALUE)
    private String division;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;

    @Column(name = "output")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> output;

}