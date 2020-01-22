//cpoint.cpp
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

//cpoint.h
//Declaration de la classe CPoint
//M�thodes d�finies dans cpoint.cpp

// Pr�vient l'exclusion multiple
#ifndef CPOINT_H
#define CPOINT_H

class CPoint {
public:
	CPoint();                          //Constructeur par d�faut.
	CPoint( double x, double y );      //Constructeur par valeur.

	CPoint operator+( const CPoint &point ); //Surcharge du + ( d�phasage )
	double operator*( const CPoint &point ); //Surcharge du * (produit scalaire)
	void operator!( void );            //Surchage du !
	int operator<( CPoint point );     //Surcharge du <
	int operator>( CPoint point );     //Surcharge du >
	int operator==( CPoint point );    //Surcharge du ==
	int operator<=( CPoint point );    //Surcharge du <=
	int operator>=( CPoint point );    //Surcharge du >=
   CPoint operator!( void );          //Surcharge du !

	double lire_x();                   //lis la valeur de x
	double lire_y();                   //Lis la valeur de y
	void ecrire( double x, double y ); //�cris les coordonn�es
	void afficher();                   //Renvois les coor. arrondies.
	double calculer_origine();         //Calcule distance entre origine
												  //et le point
	void dephasage( CPoint point );    //Fais le dephasage.

private:
	double m_x;                        //Coordonn�e x
	double m_y;                        //Coordonn�e y
};

#endif

// Constructeur par d�faut.
CPoint::CPoint()
{
	m_x = m_y = 0;
}

// Constructeur par valeur.
CPoint::CPoint( double x, double y )
{
	ecrire( x, y );
}

// Lire la coord. x
double CPoint::lire_x()
{
	return( m_x );
}

// Lire la coord. y
double CPoint::lire_y()
{
	return( m_y );
}

// �crire la coord. dans les membres x y
void CPoint::ecrire( double x, double y )
{
	m_x = x;
	m_y = y;
}

// Affiche les coordonn�e du point arrondies au centieme
void CPoint::afficher()
{
	printf( "(%.2f,%.2f)", m_x, m_y );
}

// Calcule la distance de l'origine au point
double CPoint::calculer_origine()
{
	 return( sqrt( ( pow( m_x, 2 ) ) + ( pow( m_y, 2 ) ) ) );
}

// deplace le point selon un d�phasage d'un autre point
void CPoint::dephasage( CPoint point )
{
	m_x += point.m_x;
	m_y += point.m_y;
}

// Surcharge de l'operateur + ( d�phasage )
CPoint CPoint::operator+( const CPoint &point )
{
	dephasage( point );
	return( *this );
}

//Surcharge de l'op�rateur * ( Produit scalaire )
double CPoint::operator*( const CPoint &point )
{
	return( ( m_x * m_y ) + ( point.m_x * point.m_y ) );
}

//Surcharge du !
CPoint CPoint::operator!( void )
{
	m_x = -m_x;
	m_y = -m_y;
}

//Surcharge du <
int CPoint::operator<( CPoint point )
{
	return( calculer_origine() < point.calculer_origine() );
}

//Surcharge du >
int CPoint::operator>( CPoint point )
{
	return( calculer_origine() > point.calculer_origine() );
}

//Surcharge du ==
int CPoint::operator==( CPoint point )
{
	return( calculer_origine() == point.calculer_origine() );
}

//Surcharge du <=
int CPoint::operator<=( CPoint point )
{
	return( calculer_origine() <= point.calculer_origine() );
}

//Surcharge du >=
int CPoint::operator>=( CPoint point )
{
	return( calculer_origine() >= point.calculer_origine() );
}