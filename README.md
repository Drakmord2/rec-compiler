# REC Compiler

A compiler for the REC language implemented in Java.

## REC programming language overview
REC is based on the ADA language.

```ada
global num : Integer := 8;

function fibonacci(pos : Integer) return Integer is
begin
    if pos <= 0 then
        return 0;
    end if;
    
    if pos < 3 then
        return 1;
    end if;
    
    return fibonacci(pos-1) + fibonacci(pos-2);
end fibonacci;

procedure Main () is
begin
    show fibonacci(num);
end Main;
```
You can find REC's context-free grammars written in an EBNF variant on [/docs](https://github.com/Drakmord2/rec-compiler/tree/develop/docs)

## The Compiler
Right now lexical, syntatic, and semantic analysis are performed. Code generation will be added on the next release.

* **Implementation**
    * The Scanner simulates a Deterministic Finite Automaton (DFA) to create Tokens
    * The Parser builds an Abstract Syntax Tree (AST) using the Recursive-descent parsing algorithm
    * The Checker decorates the AST with information from indentification and type checking using the Visitor pattern
 
## How to use

While an executable is not available, build the project

```bash
$> cd /Path/To/Project/root/

$> javac -d Compilador/bin -cp Compilador/src Compilador/src/compiler/Compiler.java
```

Then create an alias for the compiler with

```bash
$> alias compile="java -cp Compilador/bin compiler.Compiler"
```

Now you can compile your REC programs from the command line using

```bash
$> compile /some/path/program.rec
```
