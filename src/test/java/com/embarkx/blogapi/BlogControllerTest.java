package com.embarkx.blogapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlogControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private BlogController controller;

    private MockMvc mockMvc;

    private static final UUID POST_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private Post makePost() {
        Post post = new Post("Spring Boot Guide", "Detailed content about Spring Boot");
        ReflectionTestUtils.setField(post, "id", POST_ID);
        ReflectionTestUtils.setField(post, "createdAt", LocalDateTime.of(2026, 1, 1, 12, 0));
        return post;
    }

    @Test
    void createPost_returns201_whenValid() throws Exception {
        Post created = makePost();
        when(postService.createPost("Spring Boot Guide", "Detailed content about Spring Boot"))
                .thenReturn(created);

        mockMvc.perform(post("/api/posts")
                        .param("title", "Spring Boot Guide")
                        .param("content", "Detailed content about Spring Boot"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(POST_ID.toString()))
                .andExpect(jsonPath("$.title").value("Spring Boot Guide"));
    }

    @Test
    void createPost_returns400_whenServiceThrowsIllegalArgumentException() throws Exception {
        when(postService.createPost(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Title must be between 3 and 100 characters, got 2"));

        mockMvc.perform(post("/api/posts")
                        .param("title", "Hi")
                        .param("content", "short"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title must be between 3 and 100 characters, got 2"));
    }

    @Test
    void getAllPosts_returns200_withListOfPosts() throws Exception {
        when(postService.getAllPosts()).thenReturn(List.of(makePost()));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Spring Boot Guide"));
    }

    @Test
    void getPost_returns200_whenFound() throws Exception {
        when(postService.getPostById(POST_ID)).thenReturn(makePost());

        mockMvc.perform(get("/api/posts/" + POST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(POST_ID.toString()))
                .andExpect(jsonPath("$.title").value("Spring Boot Guide"));
    }

    @Test
    void getPost_returns404_whenNotFound() throws Exception {
        when(postService.getPostById(POST_ID)).thenThrow(new PostNotFoundException(POST_ID));

        mockMvc.perform(get("/api/posts/" + POST_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Post not found with id: " + POST_ID));
    }

    @Test
    void deletePost_returns204_whenDeleted() throws Exception {
        doNothing().when(postService).deletePost(POST_ID);

        mockMvc.perform(delete("/api/posts/" + POST_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchPosts_returns200_withResults() throws Exception {
        when(postService.searchByTitle("spring")).thenReturn(List.of(makePost()));

        mockMvc.perform(get("/api/posts/search").param("keyword", "spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Spring Boot Guide"));
    }
}
