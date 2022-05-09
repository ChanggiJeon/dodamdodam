package com.ssafy.core.entity;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album")
public class Album extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @Nullable
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @Column(name = "hashtag_id")
    private List<HashTag> hashTags;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @Column(name = "album_reaction_id")
    private List<AlbumReaction> albumReactions;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @Column(name = "picture_id")
    private List<Picture> pictures;

    public void updateLocalDate(LocalDate date){
        this.date = date;
    }
}
