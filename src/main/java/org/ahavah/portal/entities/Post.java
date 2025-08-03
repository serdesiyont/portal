package org.ahavah.portal.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Setter
    @Column(name = "pdf_title")
    private String pdfTitle;

    @Column(name = "pdf_link")
    private String pdfLink;

    @Column(name = "video_title")
    private String videoTitle;

    @Column(name = "video_link")
    private String videoLink;

    @Column(name = "schedule")
    private OffsetDateTime schedule;

    @Column(name = "division", nullable = false)
    private String division;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;



}