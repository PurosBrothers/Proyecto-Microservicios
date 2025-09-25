package com.microservicios.marketplace_ms.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long uid;

    private String titulo;

    private String cuerpo;

    private int likes;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comentario parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comentario> replies = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Comentario getParent() {
        return parent;
    }

    public void setParent(Comentario parent) {
        this.parent = parent;
    }

    public List<Comentario> getReplies() {
        return replies;
    }

    public void setReplies(List<Comentario> replies) {
        this.replies = replies;
    }

}