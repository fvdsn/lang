package happy.parser.bnf;

import java.util.List;

public interface Term {
	/**
	 * 
	 * @return true if the term is a terminal term
	 */
	public boolean isTerminal();
	
	/**
	 * 
	 * @return Le type du term pour l'analyseur syntaxique.
	 */
	public String getType();
	
	/**
	 * 
	 * @return la valeur du term, c'est à dire la chaine de caractère
	 * parsée par l'analyseur lexical.
	 * Si le term n'est pas terminal, il n'a pas de valeur
	 */
	public String getValue();
	
	/**
	 * Modifie la valeur du term
	 * @param v la nouvelle valeur du term
	 * @return this
	 */
	public Term setValue(String v);
	
	/**
	 * Renvoie la liste des enfants du terme, cette liste n'est pas vide que
	 * si le term n'es pas terminal
	 * @return
	 */
	public List<Term> getChildList();
	
	/**
	 * Imprime l'arbre qui est représenté par le term
	 * Indent définit le niveau d'indentation, lorsqu'on imprime
	 * l'arbre entier le point de départ est 0
	 * @param indent
	 */
	void	printTree(int indent);
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object obj);
	
}
