CREATE TABLE `User` (
	`id`	VARCHAR(255)	NOT NULL,
	`user_id`	VARCHAR(255)	NULL,
	`name`	VARCHAR(255)	NULL,
	`password`	VARCHAR(255)	NULL,
	`birthday`	VARCHAR(255)	NULL,
	`fcm_token`	VARCHAR(255)	NULL,
	`refresh_token`	VARCHAR(255)	NULL
);

CREATE TABLE `Profile` (
	`id`	VARCHAR(255)	NOT NULL,
	`user_id`	VARCHAR(255)	NOT NULL,
	`family_id`	VARCHAR(255)	NOT NULL,
	`image`	VARCHAR(255)	NULL,
	`emotion`	VARCHAR(255)	NULL,
	`comment`	VARCHAR(255)	NULL,
	`role`	VARCHAR(255)	NULL,
	`nickname`	VARCHAR(255)	NULL,
	`mission_content`	VARCHAR(255)	NULL,
	`mission_target_id`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `Family` (
	`id`	VARCHAR(255)	NOT NULL,
	`code`	VARCHAR(255)	NULL,
	`picture`	VARCHAR(255)	NULL
);

CREATE TABLE `Chatting` (
	`id`	VARCHAR(255)	NOT NULL,
	`profile_id`	VARCHAR(255)	NOT NULL,
	`family_id`	VARCHAR(255)	NOT NULL,
	`content`	VARCHAR(255)	NULL,
	`created_at`	VARCHAR(255)	NULL
);

CREATE TABLE `Schedule` (
	`id`	VARCHAR(255)	NOT NULL,
	`family_id`	VARCHAR(255)	NOT NULL,
	`title`	VARCHAR(255)	NULL,
	`content`	VARCHAR(255)	NULL,
	`start_date`	VARCHAR(255)	NULL,
	`end_date`	VARCHAR(255)	NULL,
	`role`	VARCHAR(255)	NULL
);

CREATE TABLE `Album` (
	`id`	VARCHAR(255)	NOT NULL,
	`family_id`	VARCHAR(255)	NOT NULL,
	`date`	VARCHAR(255)	NULL
);

CREATE TABLE `Suggestion` (
	`id`	VARCHAR(255)	NOT NULL,
	`family_id`	VARCHAR(255)	NOT NULL,
	`text`	VARCHAR(255)	NULL
);

CREATE TABLE `Reaction` (
	`id`	VARCHAR(255)	NOT NULL,
	`suggestion_id`	VARCHAR(255)	NOT NULL,
	`profile_id`	VARCHAR(255)	NOT NULL,
	`is_liked`	VARCHAR(255)	NULL
);

CREATE TABLE `Picture` (
	`id`	VARCHAR(255)	NOT NULL,
	`album_id`	VARCHAR(255)	NOT NULL,
	`origin_name`	VARCHAR(255)	NULL,
	`path_name`	VARCHAR(255)	NULL,
	`is_main`	VARCHAR(255)	NULL
);

CREATE TABLE `Hash_tag` (
	`id`	VARCHAR(255)	NOT NULL,
	`album_id`	VARCHAR(255)	NOT NULL,
	`text`	VARCHAR(255)	NULL
);

CREATE TABLE `Album_reaction` (
	`id`	VARCHAR(255)	NOT NULL,
	`profile_id`	VARCHAR(255)	NOT NULL,
	`album_id`	VARCHAR(255)	NOT NULL,
	`emoticon`	VARCHAR(255)	NULL
);

ALTER TABLE `User` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`id`
);

ALTER TABLE `Profile` ADD CONSTRAINT `PK_PROFILE` PRIMARY KEY (
	`id`,
	`user_id`,
	`family_id`
);

ALTER TABLE `Family` ADD CONSTRAINT `PK_FAMILY` PRIMARY KEY (
	`id`
);

ALTER TABLE `Chatting` ADD CONSTRAINT `PK_CHATTING` PRIMARY KEY (
	`id`,
	`profile_id`,
	`family_id`
);

ALTER TABLE `Schedule` ADD CONSTRAINT `PK_SCHEDULE` PRIMARY KEY (
	`id`,
	`family_id`
);

ALTER TABLE `Album` ADD CONSTRAINT `PK_ALBUM` PRIMARY KEY (
	`id`,
	`family_id`
);

ALTER TABLE `Suggestion` ADD CONSTRAINT `PK_SUGGESTION` PRIMARY KEY (
	`id`,
	`family_id`
);

ALTER TABLE `Reaction` ADD CONSTRAINT `PK_REACTION` PRIMARY KEY (
	`id`,
	`suggestion_id`,
	`profile_id`
);

ALTER TABLE `Picture` ADD CONSTRAINT `PK_PICTURE` PRIMARY KEY (
	`id`,
	`album_id`
);

ALTER TABLE `Hash_tag` ADD CONSTRAINT `PK_HASH_TAG` PRIMARY KEY (
	`id`,
	`album_id`
);

ALTER TABLE `Album_reaction` ADD CONSTRAINT `PK_ALBUM_REACTION` PRIMARY KEY (
	`id`,
	`profile_id`,
	`album_id`
);

ALTER TABLE `Profile` ADD CONSTRAINT `FK_User_TO_Profile_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `User` (
	`id`
);

ALTER TABLE `Profile` ADD CONSTRAINT `FK_Family_TO_Profile_1` FOREIGN KEY (
	`family_id`
)
REFERENCES `Family` (
	`id`
);

ALTER TABLE `Chatting` ADD CONSTRAINT `FK_Profile_TO_Chatting_1` FOREIGN KEY (
	`profile_id`
)
REFERENCES `Profile` (
	`id`
);

ALTER TABLE `Chatting` ADD CONSTRAINT `FK_Family_TO_Chatting_1` FOREIGN KEY (
	`family_id`
)
REFERENCES `Family` (
	`id`
);

ALTER TABLE `Schedule` ADD CONSTRAINT `FK_Family_TO_Schedule_1` FOREIGN KEY (
	`family_id`
)
REFERENCES `Family` (
	`id`
);

ALTER TABLE `Album` ADD CONSTRAINT `FK_Family_TO_Album_1` FOREIGN KEY (
	`family_id`
)
REFERENCES `Family` (
	`id`
);

ALTER TABLE `Suggestion` ADD CONSTRAINT `FK_Family_TO_Suggestion_1` FOREIGN KEY (
	`family_id`
)
REFERENCES `Family` (
	`id`
);

ALTER TABLE `Reaction` ADD CONSTRAINT `FK_Suggestion_TO_Reaction_1` FOREIGN KEY (
	`suggestion_id`
)
REFERENCES `Suggestion` (
	`id`
);

ALTER TABLE `Reaction` ADD CONSTRAINT `FK_Profile_TO_Reaction_1` FOREIGN KEY (
	`profile_id`
)
REFERENCES `Profile` (
	`id`
);

ALTER TABLE `Picture` ADD CONSTRAINT `FK_Album_TO_Picture_1` FOREIGN KEY (
	`album_id`
)
REFERENCES `Album` (
	`id`
);

ALTER TABLE `Hash_tag` ADD CONSTRAINT `FK_Album_TO_Hash_tag_1` FOREIGN KEY (
	`album_id`
)
REFERENCES `Album` (
	`id`
);

ALTER TABLE `Album_reaction` ADD CONSTRAINT `FK_Profile_TO_Album_reaction_1` FOREIGN KEY (
	`profile_id`
)
REFERENCES `Profile` (
	`id`
);

ALTER TABLE `Album_reaction` ADD CONSTRAINT `FK_Album_TO_Album_reaction_1` FOREIGN KEY (
	`album_id`
)
REFERENCES `Album` (
	`id`
);

