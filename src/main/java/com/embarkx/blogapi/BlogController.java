package com.embarkx.blogapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class BlogController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        //return ResponseEntity.ok(postService.createPost(post));
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}