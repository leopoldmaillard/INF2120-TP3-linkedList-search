/**
 * @author Leopold Maillard - MAIL07059909
 * @author Lisa Casino - CASL13619900
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		/**
		 * On commence par demander le nom d'un fichier source avant d'insérer
		 * ses valeurs dans une nouvelle liste doublement chaînée.
		 */
		List< String > liste = new ArrayList< String >();
		Stack pile = new Stack();
		Chainon chainon = new Chainon();

		// Scanner va permettre de rentrer le nom du fichier texte au clavier
		Scanner sc = new Scanner(System.in);
		System.out.println( " Entrez le nom du fichier : " );
		String nomFichier = sc.nextLine();

		Scanner file = new Scanner( new File ( nomFichier ) );

		while ( file.hasNext() ) {
			liste.add( file.next() );
		}

		file.close();

		System.out.println( liste );

		Iterator it = liste.iterator();

		// Creation d'une liste doublement chainee vide

		Liste maListeDoublementChainee = new Liste();
		int i = 0;

		// ont remplie la liste doublement chainee
		while ( it.hasNext() ) {
			String valeur = it.next().toString();
			if ( valeur.equals("{") ) {
				maListeDoublementChainee.insererAcoladeOuvrante();
			} else if ( valeur.equals("}") ) {
				maListeDoublementChainee.insererAcoladeFermante();
			} else {
				maListeDoublementChainee.insererMot( valeur , i );
				i++;
			}

		}
	
		String res = "";

		// On commence la recherche :
		// si la requete commence par t alors on demarre l'algorithme
		// si la requete commence pas f le logiciel se termine (sauf si d est null)

		// premiere recherche : la variable d est null et p est initialement vide
		
		while ( maListeDoublementChainee.d == null ) {

			System.out.println( "Entrez une commande : " );
			res = sc.nextLine();

			if ( res.charAt(0) == 'f' ) {
				System.out.println( "Vous etes oblige de trouver la cible! " );
			} else if ( res.charAt(0) == 't' ) {
				String cible = res.substring( 2 );

				System.out.println( maListeDoublementChainee.recherche( cible ) );
			}
		}

		// deuxieme recherche : d n'est plus null (on garde une trace de la recherche)
		// et les recherches ne s'arrêtent pas tant que la commande ne commence
		// pas par 'f'.
		
		while ( res.charAt(0) != 'f' ) {
			System.out.println( "Nous gardons une trace de votre recherche..." );
			System.out.println( "Entrez une autre commande : " );
			res = sc.nextLine();

			if ( res.charAt(0) == 'f' ) {
				System.out.println( "Fin. Programme terminé." );
			} else if ( res.charAt(0) == 't' ) {
				String cible2 = res.substring( 2 );
				System.out.println( maListeDoublementChainee.rechercheSub( cible2 ) );
			}
		}
		sc.close();
	}
}
