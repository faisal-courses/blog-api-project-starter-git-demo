package com.embarkx.blogapi;

import java.time.LocalDateTime;
import java.util.UUID;

public class Post {

    private final UUID id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    public Post(String title, String content) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
