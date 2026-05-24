package com.embarkx.blogapi;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(UUID id) {
        super("Post not found with id: " + id);
    }
}
