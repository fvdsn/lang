package happy.checker;
import java.util.List;

import happy.parser.bnf.*;


public class checkWP {
	public static boolean checkAll(List<Rule> grammar){
		if(CheckLambda.checkLambda(grammar)){
			System.out.println("Félicitations, La grammaire ne contient pas d'expensions vide");
		}else{
			System.out.println("Dommage, la grammaire contient des expensions vides");
			return false;
		}
		if(CheckPrecedence.checkPrecedence(grammar)){
			System.out.println("Bravo, la grammaire est WP");
		}else{
			System.out.println("Saperlipopette, la grammaire n'est pas WP!");
			return false;
		}
		
		return true;
		
	}

}
