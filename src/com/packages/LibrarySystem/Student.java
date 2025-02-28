package com.packages.LibrarySystem;
import java.util.ArrayList;

public class Student {
    private int matricule;
    private String nom;
    private String prenom;
    private ArrayList<Book> borrowedBooks;

    public Student(int matricule, String nom, String prenom) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.borrowedBooks = new ArrayList<>();
    }
    public int getMatricule() {
        return matricule;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    @Override
    public String toString() {
        return "Student{" +
                "matricule=" + matricule +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }


}
