package happy.checker;
import java.util.List;

import happy.parser.bnf.*;


public class checkWP {
	public static boolean checkAll(List<Rule> grammar){
			if(CheckLambda.checkLambda(grammar)){
			System.out.println("1) OK : La grammaire ne contient pas d'expensions vide");
		}else{
			System.out.println("1) FAIL : La grammaire contient des expensions vides");
			return false;
		}
		if(CheckUtility.check(grammar)) {
			System.out.println("2) OK : La grammaire ne contient pas de symbole inutile");
		}else{
			System.out.println("2) FAIL : La grammaire contient des symboles inutiles");
			return false;
		}
		
		if(CheckRevertible.check(grammar)){
			System.out.println("3) OK : La grammaire est réversible");
		}else{
			System.out.println("3) FAIL : La grammaire n'est pas réversible");
			return false;
		}
		if(CheckSuffix.check(grammar)){
			System.out.println("4) OK : La grammaire n'a pas d'erreurs de suffixes");
		}else{
			System.out.println("4) FAIL : La grammaire a des erreurs de suffixes");
			return false;
		}
		if(CheckPrecedence.checkPrecedence(grammar)){
			System.out.println("4) OK : La grammaire n'a pas de conflit de précédence ");
		}else{
			System.out.println("4) FAIL : La grammaire a des conflits de précédence");
			return false;
		}
		
		return true;
		
	}

}
