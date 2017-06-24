package compiler;

import checker.Checker;
import checker.SemanticException;
import parser.Parser;
import parser.SyntacticException;
import scanner.LexicalException;
import util.AST.Programa;

/**
 * Compiler driver
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public class Compiler {

	/**
	 * Compiler start point
	 * @param args - none
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String location = Compiler.validateInput(args);
		String[] file 	= location.split("/");
		
		System.out.println("\nREC Compiler\nVersão 0.8 - 2017\nRubens Carneiro - rec2@ecomp.poli.br");
		System.out.println("\nCompilando código-fonte [ " + file[file.length-1] + " ]\n");
		System.out.println("-----------------------------------------------------------------------------------------");
		
		try {
			Parser p 			= new Parser(location);
			Programa astRoot 	= null;
			
			astRoot = p.parse();
			
			System.out.println("\nAnálise Lexica    - PASS");
			System.out.println("Análise Sintatica - PASS");
			
			if ( astRoot != null ) {
			    
				Checker c	= new Checker();
				astRoot 	= c.check(astRoot);
				
				System.out.println("Análise Semantica - PASS\n");
				System.out.println("-----------------------------------------------------------------------------------------");
				System.out.println("\n\t-- AST Decorada --\n");
				System.out.println( astRoot.toString(0));
				System.out.println("-----------------------------------------------------------------------------------------");
			}
			
		} catch (LexicalException e) {
			e.printStackTrace();
		} catch (SyntacticException e) {
			e.printStackTrace();
		} catch (SemanticException e) {
			e.printStackTrace();
		}
	}
	
	public static String validateInput(String[] arg) throws Exception {
		if (arg.length == 0) {
 			String erro = "\n\n------------------- INPUT ERROR - BEGIN -------------------------\n"
					+ "\nPath do codigo-fonte é inválido\n"
					+ "\n------------------- INPUT ERROR - END ---------------------------\n";
			
			throw new Exception(erro);
		}
		
		return arg[0];
	}
	
}
