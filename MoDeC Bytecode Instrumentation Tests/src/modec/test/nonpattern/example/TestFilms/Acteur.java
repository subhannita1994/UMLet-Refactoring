/*******************************************************************************
 * Copyright (c) 2001-2014 Yann-Ga�l Gu�h�neuc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Yann-Ga�l Gu�h�neuc and others, see in file; API and its implementation
 ******************************************************************************/
package modec.test.nonpattern.example.TestFilms;

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

	public int getID () { return this.id; }
	public String getNom () { return this.nom; }

	/**
	 * Red�finition de la m�thode toString pour fin d'affichage.
	 */
	public String toString ()
	{
		return this.nom;
	}

}
