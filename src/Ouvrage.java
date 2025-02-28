import java.util.Vector;

class Ouvrage {

    private int cote;
    private String titre;
    private Vector<Auteur> listAuteurs;
    private String maisonEdition;
    private int annee;

    public Ouvrage(){

    }
    // Constructor to create an Ouvrage
    public Ouvrage(int cote, String titre, Vector<Auteur> listAuteurs, String maisonEdition, int annee) {
        this.cote = cote;
        this.titre = titre;
        this.listAuteurs = listAuteurs;
        this.maisonEdition = maisonEdition;
        this.annee = annee;
    }

    // Getters and Setters for all private fields (accesseur methods)
    public int getCote() {
        return cote;
    }

    public void setCote(int cote) {
        this.cote = cote;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Vector<Auteur> getListAuteurs() {
        return listAuteurs;
    }

    public void setListAuteurs(Vector<Auteur> listAuteurs) {
        this.listAuteurs = listAuteurs;
    }

    public String getMaisonEdition() {
        return maisonEdition;
    }

    public void setMaisonEdition(String maisonEdition) {
        this.maisonEdition = maisonEdition;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    // Method to determine if an author is in the listAuteurs
    public boolean isAuteurInList(String nomComplet) {
        for (int i = 0; i < listAuteurs.size(); i++) {
            if (listAuteurs.get(i).getNomComplet().equals(nomComplet)) {
                return true;
            }
        }
        return false;
    }

    // Method to verify if a theme is in the title (checks if the theme is part of the title)
    public boolean isThemeInTitre(String theme) {
        return titre.contains(theme); // String "contains" method checks if a substring exists within the string
    }

    @Override
    public String toString() {
        return "Ouvrage: { \n" +
                "\t cote=" + cote + ", \n" +
                "\t titre='" + titre + "',\n" +
                "\t maisonEdition='" + maisonEdition + "', \n" +
                "\t annee=" + annee + ", \n" +
                "\t auteurs=" + listAuteurs +" \n" +
                "}\n";
    }
}
