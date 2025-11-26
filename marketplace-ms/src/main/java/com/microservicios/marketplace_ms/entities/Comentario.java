package com.microservicios.marketplace_ms.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uid;

    private String titulo;

    private String cuerpo;

    private int likes;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Comentario parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comentario> replies = new ArrayList<>();

    // Relación con Item - solo para comentarios padre
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    // Relación con Calificacion - solo para comentarios padre
    @OneToOne(mappedBy = "comentario", cascade = CascadeType.ALL)
    @JsonManagedReference("comentario-calificacion")
    private Calificacion calificacion;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
    }

    // Método helper para verificar si es comentario padre
    public boolean isComentarioPadre() {
        return this.parent == null;
    }

    // Método helper para verificar si debe tener calificación obligatoria
    public boolean debeEstarCalificado() {
        return isComentarioPadre() && this.item != null;
    }

}