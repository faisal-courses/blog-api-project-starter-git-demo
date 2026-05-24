package com.embarkx.blogapi;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(String title, String content) {
        validateTitle(title);
        validateContent(content);
        return postRepository.save(new Post(title, content));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post updatePost(UUID id, String title, String content) {
        validateTitle(title);
        validateContent(content);
        Post post = getPostById(id);
        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }

    public void deletePost(UUID id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    public List<Post> searchByTitle(String keyword) {
        return postRepository.findByTitleContainingIgnoreCase(keyword);
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        if (title.length() < 3 || title.length() > 100) {
            throw new IllegalArgumentException(
                    "Title must be between 3 and 100 characters, got " + title.length());
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content must not be blank");
        }
        if (content.length() < 50 || content.length() > 5000) {
            throw new IllegalArgumentException(
                    "Content must be between 50 and 5000 characters, got " + content.length());
        }
    }
}
