package com.embarkx.blogapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class BlogController {

    private final PostService postService;

    public BlogController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestParam String title, @RequestParam String content) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(title, content));
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable UUID id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam String keyword) {
        return postService.searchByTitle(keyword);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handleNotFound(PostNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidation(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
