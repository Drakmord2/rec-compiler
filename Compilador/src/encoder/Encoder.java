package encoder;

import util.AST.*;
import util.Arquivo;

public class Encoder {
	public AST astRoot;
	public Arquivo io;

	public Encoder(AST astRoot, String in, String out) throws Exception {
		this.astRoot	= astRoot;
		this.io 		= new Arquivo(in, out);
	}
	
	public void encode() {
		String data = "\nSECTION .data\n";
		String bss  = "SECTION .bss\n";
		String text = "SECTION .text\n    GLOBAL _main\n\n_main:\n";
		
		String out = data+"\n"+bss+"\n"+text+"\n";
		
		io.print(out);
		io.flush();
		io.close();
	}
}
