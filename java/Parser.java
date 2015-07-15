package parser;

import java.io.*;

/**
 * This class is in charge of loading a CafeOBJ file with some restrictions,
 * namely:
 * - The header of the module is not preceded in the same line by any element
 * from the previous module.
 * - One line is used for each operator definition and operator name (the sorts
 * can be located in other line).
 * - The metadata information is located in the same line as the end of the
 * statement where it must be introduced.
 * And returns a modified module that can be read by Full Maude. This transformation
 * consists of:
 * - Enclosing each module in parentheses.
 * - Add the "`" character to the scape characters.
 * - Introduce the metadata information inside the equations or rules.
 * - Transform CafeOBJ comments into Maude comments.
 * @author adrian
 */
public class Parser {

	// This attribute indicates whether the first block has been found.
	private boolean started;

	public Parser(){
		started = false;
	}

	/**
	 * It receives the path of a CafeOBJ file, reads its
	 * contents and modifies the program to be run by Full
	 * Maude. This modification includes:
	 * - Introducing parentheses.
	 * - Modifying comments (from CafeOBJ comments to Maude comments).
	 * - Introducing the metadata information given as comments
	 * as attributes.
	 * This program is then saved in the path given as second argument.
	 * @param source The path of the CafeOBJ file to be modified.
	 * @param result The path of the new file to be created.
	 * @return True if the file was correctly opened and modified, False
	 * otherwise.
	 */
	public boolean transformCafeProgram(String source, String result){
		BufferedReader reader = null;
		String text = "";
		String line = "";
		boolean ok = true;

		try {
		    reader = new BufferedReader(new FileReader(source));
		    while ((line = reader.readLine()) != null) {
		    	text = text + transform(line) + "\n";
		    }
		    reader.close();
		    text = balance(text);
		    PrintWriter writer = new PrintWriter(result);
		    writer.print(text);
		    writer.close();
		}
		catch (IOException e) {
			ok = false;
		}
		return ok;
	}

	/**
	 * This function corrects the position of the parentheses introduced
	 * in the previous steps by removing the first one and placing it at
	 * the end of the file, where it is really required.
	 * @param text Text to be transformed.
	 * @return The transformed text, where the first (unbalanced) parenthesis
	 * has been removed, and the last (also unbalanced) has been closed.
	 * @author Adrian Riesco.
	 */
	private String balance(String text){
		String res = text;
		if (started){
			res = res + ")";
		}
		return res;
	}

	/**
	 * This method is in charge of applying all the transformation to
	 * each line of the original module. Note that the order of the
	 * transformations matters, since the metadata transformations
	 * require the CafeOBJ notation for comments.
	 * @param line Line to be modified.
	 * @return The line after applying all the transformations required
	 * by Full Maude.
	 * @author Adrian Riesco.
	 */
	private String transform(String line){
		String res = line;
		res = moduleHeaderTrans(res);
		res = scapeCharsTrans(res);
		res = newScapeCharsTrans(res);
		res = metadataTrans(res);
		res = commentTrans(res);
		res = membershipAxiomTrans(res);
		return res;
	}

	private String membershipAxiomTrans(String line){
		String res = line;
		int pos = res.indexOf(":is");
		while (pos != -1){
			res = transformFirstInto(res, ":is", "::");
			pos = res.indexOf(":is");
		}
		return res;
	}

	/**
	 * This funcion checks whether a comment with metadata information has been
	 * written at the end of the line. If this is the case, then the comment is
	 * removed and the attribute introduced inside the equation of rule, taking
	 * care of other possible attributes.
	 * @param line Line read from the input that will be modified.
	 * @return The line with the comment removed and the metadata attribute
	 * introduced into the equation/rule. If there was no metadata, the line
	 * is not modified.
	 * @author Adrian Riesco.
	 */
	private String metadataTrans(String line){
		String res = line;
		int pos = metadataInitPos(line);
		// If pos != -1 then there is a metadata attribute.
		if (pos != -1){
			// To prevent the application from reading the final dot, if
			// it exists.
			int finalPos = res.lastIndexOf("}");
			String metadataAttr = "{" + res.substring(pos, finalPos + 1);
			res = removeComment(res);
			res = introduceAttr(res, metadataAttr);
		}
		return res;
	}

	/**
	 * This function receives as argument a line and an attribute that was
	 * previously commented. Then, it introduces the attribute (taking care
	 * of the final dot and of other attributes) at the end of the statement.
	 * @param line A line read from the input. This line must be the last line
	 * defining an equation or a rule (possibly conditional).
	 * @param attr Attribute that will be introduced inside the statement
	 * that finishes in this line.
	 * @return The line modified by introducing the attribute inside the statement.
	 * @author Adrian Riesco.
	 */
	private String introduceAttr(String line, String attr){
		String res = line;
		int nonExecPos = res.indexOf("nonexec");
		if (nonExecPos == -1){
			int pos = res.lastIndexOf(".");
			res = res.substring(0, pos) + attr + " .";
		}
		else{
			// Remove the "{"
			attr = attr.substring(1);
			res = res.substring(0, nonExecPos) + "nonexec " + attr + " .";
		}
		return res;
	}

	/**
	 * This function receives a line and removes everything coming after
	 * a CafeOBJ comment.
	 * @param line Line read from the input.
	 * @return The line once all the characters after the first comment
	 * has been removed. Note that the line is not modified if the line does
	 * not contain any comment.
	 * @author Adrian Riesco.
	 */
	private String removeComment(String line){
		String res = line;
		int pos = res.indexOf("--");
		if (pos != -1){
			res = res.substring(0, pos);
		}
		pos = res.indexOf("**");
		if (pos != -1){
			res = res.substring(0, pos);
		}
		return res;
	}

	/**
	 * This function looks for the metadata attribute placed after a comment.
	 * @param line Line where the metadata attribute is looked for.
	 * @return The position where the metadata information starts, counting from 0.
	 * If there is no metadata, it returns -1.
	 * @author Adrian Riesco.
	 */
	private int metadataInitPos(String line){
		int pos = -1;
		int commentPos = line.indexOf("-->");
		if (commentPos == -1){
			commentPos = line.indexOf("**>");
		}
		if (commentPos != -1){
			pos = line.indexOf("metadata", commentPos);
		}
		return pos;
	}

	/**
	 * This function transforms CafeOBJ comments into Maude comments.
	 * @param line The line to be transformed.
	 * @return The first appearance of a CafeOBJ comment in line
	 * is transformed into the corresponding Maude version of the comment.
	 * @author Adrian Riesco.
	 */
	public String commentTrans(String line){
		String res = line;
		res = transformFirstInto(res, "-- ", "---> ");
		res = transformFirstInto(res, "-->", "---> ");
		res = transformFirstInto(res, "** ", "***> ");
		res = transformFirstInto(res, "**>", "***> ");
		if (res.endsWith(" --")){
			int pos = line.lastIndexOf(res);
			res = res.substring(0, pos);
		}
		if (res.equals("--")){
			res = "--->";
		}
		return res;
	}

	/**
	 * This function transforms, in the given line, the first appearance
	 * of init by fin.
	 * @param line String to be modified.
	 * @param init String to be replaced.
	 * @param fin String that replaces init.
	 * @return Returns the String line, where the first appearance of init
	 * has been replaced by fin. If init does not appear in line, then it is
	 * not modified.
	 * @author Adrian Riesco.
	 */
	private String transformFirstInto(String line, String init, String fin){
		String res = line;
		int pos = line.indexOf(init);
		if (pos != -1){
			res = res.substring(0, pos) + fin + res.substring(pos + init.length());
		}
		return res;
	}

	/**
	 * This function places parentheses when a new module or view
	 * is found, closing the previous one.
	 * @see The first time this function is used it introduces
	 * an extra ")", that will be removed once all the lines
	 * have been processed. Note also that the last ")" will be
	 * missing, so we also need to add it at the end.
	 * @param line Line we are processing.
	 * @return The line preceded by a closing and an opening parenthesis
	 * if this lines contains the beginning of a new module.
	 * Otherwise, it returns the same line.
	 * @author Adrian Riesco.
	 */
	private String moduleHeaderTrans(String line){
		String res = line;
		if (startsWithIgnoreBlanks(line, "module") ||
				startsWithIgnoreBlanks(line, "module!") ||
				startsWithIgnoreBlanks(line, "module*") ||
				startsWithIgnoreBlanks(line, "mod") ||
				startsWithIgnoreBlanks(line, "mod!") ||
				startsWithIgnoreBlanks(line, "mod*") ||
				startsWithIgnoreBlanks(line, "make") ||
				startsWithIgnoreBlanks(line, "view") ||
				startsWithIgnoreBlanks(line, "open")){
			if (started){
				res = ")\n\n(" + line;
			}
			else{
				res = "(" + line;
				started = true;
			}
		}
		return res;
	}

	/**
	 * This function adds the special character "`" preceding
	 * scape characters if they are used to define a new operator.
	 * @param line Line to be modified.
	 * @return A new line with the character "`" preceding commas,
	 * parentheses and curly braces if the line starts with a
	 * reserved word for operators. Otherwise, it returns the same line.
	 * @author Adrian Riesco.
	 */
	private String scapeCharsTrans(String line){
		String res = line;
		if (startsWithIgnoreBlanks(res, "op") ||
				startsWithIgnoreBlanks(res, "ops") ||
				startsWithIgnoreBlanks(res, "bop") ||
				startsWithIgnoreBlanks(res, "bops")){
			res = addScapeChars(res, ",");
			// res = addScapeChars(res, "(");
			// res = addScapeChars(res, ")");
			res = dealWithParensInOp(res);
			res = addScapeChars(res, "{");
			res = addScapeChars(res, "}");
			res = addScapeChars(res, "[");
			res = addScapeChars(res, "]");
		}
		return res;
	}

	/**
	 * This transformation makes sure that ";", which are scape characters for CafeOBJ but not
	 * for Maude, have whitespaces surrounding them.
	 * @param line Line to be modified.
	 * @return New line with whitespaces surrounding the ";"
	 */
	private String newScapeCharsTrans(String line){
		if (line.indexOf("op") == -1){
			line = line.replaceAll(";", " ; ");
		}
		return line;
	}

	private String dealWithParensInOp(String line){
		String res = line;
		int last = res.lastIndexOf(" ->");
		// Double parentheses are enforced, so we keep them.
		int pos = res.indexOf("((");
		while (pos != -1 && pos < last){
			res = res.substring(0, pos) + "@#$" + res.substring(pos + 4, res.length());
			pos = res.indexOf("((");
			last = res.lastIndexOf(" ->");
		}
		pos = res.indexOf("))");
		while (pos != -1 && pos < last){
			res = res.substring(0, pos) + "$#@" + res.substring(pos + 4, res.length());
			pos = res.indexOf("))");
			last = res.lastIndexOf(" ->");
		}

		// We remove the no required ones.
		pos = res.indexOf("(");
		while (pos != -1 && pos < last){
			res = res.substring(0, pos) + res.substring(pos + 1, res.length());
			pos = res.indexOf("(");
			last = res.lastIndexOf(" ->");
		}
		pos = res.indexOf(")");
		while (pos != -1 && pos < last){
			res = res.substring(0, pos) + res.substring(pos + 1, res.length());
			pos = res.indexOf(")");
			last = res.lastIndexOf(" ->");
		}
		// And place again the double ones.
		res = res.replace("@#$", "`(");
		res = res.replace("$#@", "`)");
		return res;
	}

	/**
	 * This function adds the character "`" to the scape character indicated
	 * by the argument ch.
	 * @param line Line where the characters ch must be preceded by "`".
	 * @param ch Scape character following the "`" character.
	 * @return The line with the "`" characters added before the scape
	 * characters.
	 */
	private String addScapeChars(String line, String ch){
		String res = line;
		int previous = 0;
		int pos = res.indexOf(ch);
		int last = res.lastIndexOf(" ->");
		// A loop is required in case the operator uses several commas.
		while (pos != -1 && pos < last){
			res = res.substring(previous, pos) + "`" + res.substring(pos);
			// We look for the next comma
			pos = res.indexOf(ch, pos + 1 + ch.length());
		}
		return res;
	}

	/**
	 * This function checks whether the String given as first argument
	 * starts with the word given as second argument.
	 * @param line Line to be analyzed.
	 * @param word Word we are looking for.
	 * @return true if either the line starts with the word or it
	 * starts by whitespaces followed by the word.
	 * @author Adrian Riesco.
	 */
	private boolean startsWithIgnoreBlanks(String line, String word){
		int pos = line.indexOf(word);
		boolean ok = pos != -1;
		if (ok){
			int i = 0;
			char ch;
			while (ok && i < pos){
				ch = line.charAt(i);
				ok = Character.isWhitespace(ch);
				i++;
			}
		}
		return ok;
	}

	public static void main(String[] args) {
		Parser parser = new Parser();
		//String res = parser.addScapeChars("op ((_)) : A -> B [ctor] .", "))");
		//System.out.println(res);
		String source = args[0];
		String target = args[1];
		parser.transformCafeProgram(source, target);
	}

}
