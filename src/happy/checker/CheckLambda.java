package happy.checker;
import java.util.List;
import happy.parser.bnf.*;

public class CheckLambda {
	/**
	 * Vérifie que la liste ne contient pas d'expensions vides,
	 * en cherchant les termes 'lambda' dans tous les termes de toutes
	 * les règles. 
	 * @param grammar la grammaire
	 * @return  true si la grammaire ne contient pas de terme 'lambda',
	 * 			false sinon.
	 */
	public static boolean checkLambda(List<Rule> grammar){
		boolean valid = true;
		for(Term t:CheckPrecedence.getAllTerm(grammar)){
			if(t.toString().equals("lambda")){
				valid = false;
			}
		}
		for (Rule r:grammar){
			if (r.getOrList() == null){
				System.out.println("La règle -"+r.getName()+"- n'a pas de production");
				valid = false;
			}else if(r.getOrList().isEmpty()){

				System.out.println("La règle -"+r.getName()+"- n'a pas de production");
				valid = false;
			}else{
				for(CatList cl:r.getOrList()){
					if (cl.getTermList().isEmpty()){

						System.out.println("La règle -"+r.getName()+"- a une production vide");
						valid = false;
					}
				}
			}
		}
		return valid;
		
	}

}
