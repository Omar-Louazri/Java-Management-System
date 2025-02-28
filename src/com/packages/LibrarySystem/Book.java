package com.packages.LibrarySystem;

public class Book {
    private int isbn;
    private String titre;
    private int nbDisponibles;

    public Book(int isbn, String titre, int nbDisponibles) {
        this.isbn = isbn;
        this.titre = titre;
        this.nbDisponibles = nbDisponibles;
    }
    public int getIsbn() {
        return isbn;
    }
    public String getTitre() {
        return titre;
    }
    public int getNbDisponibles() {
        return nbDisponibles;
    }
    @Override
    public String toString() {
        return "Book{" +
                "isbn=" + isbn +
                ", titre='" + titre + '\'' +
                ", nbDisponibles=" + nbDisponibles +
                '}';
    }

}
