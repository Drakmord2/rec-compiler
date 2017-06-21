# REC Compiler

REC compiler built in Java.

## REC programming language overview
REC is based on the ADA language.

```ada
global pos : Integer := 8;

function fibonacci(pos : Integer) return Integer is
begin
	if pos < 3 then
		return 1;
	else
		return fibonacci(pos-1) + fibonacci(pos-2);
	end if;
end fibonacci;

procedure Main () is
begin
	show fibonacci(pos);
end Main;
```
You can find REC's formal grammars in EBNF on [/docs](https://github.com/Drakmord2/rec-compiler/tree/develop/docs)

## The Compiler
Right now lexical, syntatic and semantic analysis are performed. Code generation will be added on the next release.

## How to use

From the command line you can create an alias like:
```bash
$> alias compile="java -cp /PathToProject/Compilador/bin compiler.Compiler "
```
Now you can compile your REC programs from the command line using

```bash
$> compile /some/path/program.rec
```
