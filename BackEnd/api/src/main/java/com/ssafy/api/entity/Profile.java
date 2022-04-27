package com.ssafy.api.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Null;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Nullable
    @Column(length = 10)
    private String emotion;

    @Nullable
    @Column(length = 20)
    private String comment;

    @Column(nullable = false, length = 10)
    private String role;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Nullable
    @Column(length = 20)
    private String mission_content;

    @Nullable
    @Column(length = 20)
    private String mission_target;

    @Nullable
    @Column(length = 200)
    private String imagePath;

    @Nullable
    @Column(length = 200)
    private String imageName;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;
//
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id")
//    private Profile profileParent;

    public void updateEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void updateRole(String role) {
        this.role = role;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void updateImageName(String imageName) {
        this.imageName = imageName;
    }

    public void updateMissionContent(String mission_content) {
        this.mission_content = mission_content;
    }

    public void updateMissionTarget(String mission_target){this.mission_target = mission_target;}
}
