// Auteur class
class Auteur {
    private String nomComplet;
    private int age;

    // Constructor
    public Auteur(String nomComplet, int age) {
        this.nomComplet = nomComplet;
        this.age = age;
    }

    // Getters and Setters
    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return nomComplet + " (" + age + " ans)";
    }
}
