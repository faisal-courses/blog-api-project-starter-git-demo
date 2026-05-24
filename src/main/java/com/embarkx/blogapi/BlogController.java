package com.embarkx.blogapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class BlogController {

    private static final Map<UUID, Post> posts = new LinkedHashMap<>();

    @PostMapping
    public Post createPost(@RequestParam String title, @RequestParam String content) {
        Post post = new Post(title, content);
        posts.put(post.getId(), post);
        return post;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return new ArrayList<>(posts.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable UUID id) {
        Post post = posts.get(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/validate")
    public String validateContent(@RequestParam String content) {
        if (content.length() > 5000) {
            return "Too long";
        }
        return "OK";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable UUID id) {
        if (posts.remove(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/total")
    public String getTotalWordCount() {
        int total = posts.values().stream()
            .mapToInt(p -> p.getContent().split("\\s+").length)
            .sum();
        return "Total words: " + total;
    }
}