
/**
 *  @author Lisa Casino - CASL13619900
 *  @author Léopold Maillard - MAIL07059909
 */
import java.util.Iterator;
import java.util.Stack;

/**
 * Implémentation d'un TDA liste doublement chaînée avec différents types de
 * chainons et des fonctionnalités de performance.
 */
public class Liste< E > {
	/**
	 * Une classe interne abstraite pour représenter un Chainon. Tous les chainons
	 * ont en commun un pointeur vers un chainon précédent et un vers un chainon
	 * suivant.
	 */
	protected abstract class Chainon< EC > {
		public Chainon< EC > precedant;
		public Chainon< EC > suivant;

		public Chainon() {
			precedant = null;
			suivant = null;
		}

		public Chainon(Chainon< EC > precedant) {
			this.precedant = precedant;
		}

	}

	/**
	 * Un Mot est un Chainon particulier qui possède une valeur de type String
	 * appelée clef et l'indexe (en ordre d'apparition) de ce String dans la liste.
	 */
	protected class Mot< EC > extends Chainon< EC > {
		public String clef;
		public int indexe;

		public Mot() {
			super();
			this.clef = null;
			this.indexe = -1;
		}

		public Mot( Chainon< EC > precedant, String clef, int indexe ) {
			super( precedant );
			this.clef = clef;
			this.indexe = indexe;
		}
	}

	/**
	 * Une Accolade Ouvrante est un Chainon particulier qui possède une référence
	 * vers un chainon Accolade Fermante qui lui est associé dans la liste.
	 */
	protected class AccoladeOuvrante< EC > extends Chainon< EC > {
		public Chainon< EC > associe;

		public AccoladeOuvrante() {
			super();
			associe = null;
		}

		public AccoladeOuvrante( Chainon< EC > precedant ) {
			super( precedant );
		}
	}

	/**
	 * Une Accolade Fermante est un Chainon particulier qui possède une référence
	 * vers un chainon Accolade Ouvrante qui lui est associé dans la liste.
	 */
	protected class AccoladeFermante< EC > extends Chainon< EC > {
		public Chainon< EC > associe;

		public AccoladeFermante() {
			super();
			associe = null;
		}

		public AccoladeFermante( Chainon< EC > precedant, Chainon< EC > associe ) {
			super( precedant );
			this.associe = associe;
		}
	}

	/**
	 * Une reference au premier element de la liste est maintenue ici.
	 */
	protected Chainon< E > _tete;

	/**
	 * Puisque l'ajout a la fin d'une Liste est courante, un reference est conserve
	 * pour obtenir rapidement le dernier element.
	 */
	protected Chainon< E > _fin;

	protected int _taille;

	/**
	 * Pile qui va permettre d'asssurer les associations entres les accolades
	 * ouvrantes et fermantes lors de la construction de la liste.
	 */
	protected Stack< AccoladeOuvrante > pileAccolade = new Stack< AccoladeOuvrante >();

	/**
	 * Pile qui garde une référence sur les chainons représentants des accolades
	 * ouvrantes qui ont été récemment visités.
	 */
	protected Stack< AccoladeOuvrante > p = new Stack< AccoladeOuvrante >();

	/**
	 * Chainon qui garde une référence sur le dernier chainon qui a été cherché par
	 * le programme.
	 */
	protected Chainon d = null;

	/**
	 * Construit une Liste vide.
	 */
	public Liste() {
		_tete = null;
		_taille = 0;
	}

	/**
	 * Ajoute un Chainon de type Mot à la fin de la liste.
	 * 
	 * @param la clef du mot
	 * @param l'indexe du mot
	 */
	public void insererMot( String clef , int indexe ) {
		Mot nouveauMot = new Mot( _fin , clef , indexe );

		if ( _taille == 0 ) {
			_tete = nouveauMot;
		} else {
			_fin.suivant = nouveauMot;
		}

		_fin = nouveauMot;

		++_taille;
	}

	/**
	 * Ajoute un chainon de type AccoladeOuvrante à la fin de la liste.
	 */
	public void insererAcoladeOuvrante() {
		AccoladeOuvrante nouvelle = new AccoladeOuvrante(_fin);

		if ( _taille == 0 ) {
			_tete = nouvelle;
		} else {
			_fin.suivant = nouvelle;
		}

		_fin = nouvelle;

		++_taille;

		pileAccolade.push( nouvelle );

	}

	/**
	 * Ajoute un chainon de type AccoladeFermante à la fin de la liste.
	 */
	public void insererAcoladeFermante() {
		AccoladeFermante nouvelle = new AccoladeFermante( _fin, pileAccolade.peek() );
		pileAccolade.peek().associe = nouvelle;
		pileAccolade.pop();

		if ( _taille == 0 ) {
			_tete = nouvelle;
		} else {
			_fin.suivant = nouvelle;
		}

		_fin = nouvelle;

		++_taille;

	}

	/**
	 * La taille de la liste
	 * 
	 * @return Le nombre d'element que contient la Liste
	 */
	public int taille() {
		return _taille;
	}

	/**
	 * Effectue la première recherche, en parcourant les éléments de la liste un par
	 * un. Met en place le mécanisme de référencement des accolades avec les piles .
	 * 
	 * @param l'élément cible à chercher dans la liste.
	 * @return le chemin des mots parcourus.
	 */
	public String recherche( String cible ) {
		String chemin = "";
		Chainon courant = _tete;

		while ( courant != null && d == null ) {
			if ( courant instanceof AccoladeOuvrante ) {
				p.push( (AccoladeOuvrante)courant );
			} else if ( courant instanceof AccoladeFermante ) {
				p.pop();
			} else if ( courant instanceof Mot ) {
				chemin = chemin + ( (Mot)courant ).indexe + " ";
				if ( ((Mot)courant ).clef.equals( cible ) ) {
					d = courant;
				}
			}
			courant = courant.suivant;
		}
		return chemin;
	}

	/**
	 * Traite les recherches subséquentes demandées par l'utilisateur. Le programme
	 * accèdera ainsi plus rapidement aux éléments demandés grâce au stockage des
	 * indicateurs de localité.
	 * 
	 * @param l'élément recherché dans la liste.
	 * @return le chemin des mots vérifiés.
	 */
	public String rechercheSub( String cible ) {
		Stack< AccoladeOuvrante > accoladeVisitee = new Stack< AccoladeOuvrante >();
		String chemin = "";
		boolean estTrouve = false;

		Chainon courant = d;

		// Vérifie dans un premier temps si le mot courant n'est pas celui
		// recherché

		chemin = chemin + ( (Mot)d ).indexe; 
		if ( ((Mot)courant ).clef.equals( cible ) ) {
			chemin = chemin + ( (Mot)courant ).clef;
		} else {
			AccoladeOuvrante derniere;
			accoladeVisitee.push( p.peek() );
			while ( !p.isEmpty() && !estTrouve ) {
				derniere = p.peek();
				courant = derniere.suivant;
				while ( !derniere.associe.equals( courant ) && !estTrouve ) {

					if ( courant instanceof AccoladeOuvrante ) {
						if ( courant.equals( accoladeVisitee.peek() ) ) {
							courant = accoladeVisitee.peek().associe;
						} else
							p.push( (AccoladeOuvrante) courant );
					} else if ( courant instanceof AccoladeFermante ) {
						p.pop();
					} else if ( courant instanceof Mot ) {
						chemin = chemin + " " + ( (Mot)courant ).indexe;
						if ( ( (Mot)courant ).clef.equals( cible ) ) {
							estTrouve = true;
							// assure le bon fonctionnement d'une nouvelle recherche
							p.push( p.peek() ); 
							this.d = courant;

						}
					}
					courant = courant.suivant;
				}
				accoladeVisitee.push( p.pop() );
			}
		}
		return chemin;
	}

}