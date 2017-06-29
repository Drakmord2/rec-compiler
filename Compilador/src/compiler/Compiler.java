package compiler;

import checker.Checker;
import checker.SemanticException;
import encoder.Encoder;
import parser.Parser;
import parser.SyntacticException;
import scanner.LexicalException;
import util.FileException;
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
		try {
			String location = Compiler.validateInput(args);
			String[] file 	= location.split("/");
			String out 		= location.split(".rec")[0];
			
			Compiler.printHeader(file);
		
			Parser p 			= new Parser(location);
			Programa astRoot 	= null;
			astRoot 			= p.parse();
			
			System.out.println("\nAnálise Lexica    - PASS");
			System.out.println("Análise Sintatica - PASS");
			
			if ( astRoot != null ) {
				Checker c	= new Checker();
				astRoot 	= c.check(astRoot);
				
				System.out.println("Análise Semantica - PASS");

				out 		= out+".asm";
				Encoder e 	= new Encoder(astRoot, location, out);
				e.encode();
				
				Compiler.printBody(astRoot, out);
			}	
		} 
		catch (LexicalException e) {
			System.out.println(e.toString());
		} catch (SyntacticException e) {
			System.out.println(e.toString());
		} catch (SemanticException e) {
			System.out.println(e.toString());
		} catch (FileException e) {
			System.out.println(e.toString());
		}
	}
	
	public static String validateInput(String[] arg) throws FileException {
		if (arg.length == 0) {
 			String message = "Path do codigo-fonte é inválido";
			
			throw new FileException(message);
		}
		
		String location = arg[0];
		String[] ext 	= location.split("[.]");
		int i 			= ext.length;
		
		try {
			if (! ext[i-1].equals("rec")) {
				String message = "Código-fonte não é da linguagem REC.";
				throw new FileException(message);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			String message = "Código-fonte não é da linguagem REC.";
			throw new FileException(message);
		}
		
		return location;
	}
	
	public static void printHeader(String[] file) {
		System.out.println("\nREC Compiler\nVersão 0.9 - 2017\nRubens Carneiro - rec2@ecomp.poli.br");
		System.out.println("\nCompilando código-fonte [ " + file[file.length-1] + " ]\n");
		System.out.println("-----------------------------------------------------------------------------------------");
	}
	
	public static void printBody(Programa astRoot, String out) {
		System.out.println("Gerador de Código - PASS\n");
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println("\n\t-- AST Decorada --\n");
		System.out.println( astRoot.toString(0));
		System.out.println("-----------------------------------------------------------------------------------------\n");
		System.out.println("Código Assembly (NASM) criado em [ "+out+" ]\n");
		System.out.println("-----------------------------------------------------------------------------------------\n");
	}
}
