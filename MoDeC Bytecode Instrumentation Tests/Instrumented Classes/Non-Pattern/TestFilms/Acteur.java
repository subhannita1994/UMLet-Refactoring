/*
 * Cr�e le 7 f�vrier 2005
 *
 * Auteur : Janice Ng
 * Source : Acteur.java
 */
import java.util.*;

public class Acteur {


	private int id;		// num�ro de l'acteur
	private String nom; // nom de l'acteur


	/**
	 * Constructeur qui re�oit l'identificateur
	 * le nom du film.
	 *
	 * @param id : num�ro identificateur de l'acteur
	 * @param nom : nom de l'acteur
	 *
	 */
	public Acteur (int id, String nom)
	{
		
		this.id = id;
		this.nom = nom;
	}

	
	/**
	 * M�thodes d'acc�s et de modification.
	 */
	public void setID (int id) { this.id = id; }
	public void setNom (String nom) { this.nom = nom; }

	public int getID () { return id; }
	public String getNom () { return nom; }

	/**
	 * Red�finition de la m�thode toString pour fin d'affichage.
	 */
	public String toString ()
	{
		return nom;
	}

}
