package net.miladinov.holding;
import net.miladinov.util.Stack;

public class Exercise15 {

	public static void main(String[] args) {
		CommandInterpreter ci = new CommandInterpreter();
		ci.interpret("+U+n+c-+e+r+t-+a-+i-+n+t+y-+-+r+u-+l+e+s-");
	}
}

class CommandInterpreter {
	
	private Stack<Character> commandStack;
	
	public CommandInterpreter() {
		commandStack = new Stack<Character>();
	}
	
	public void interpret(String command) {
		int position = 0;
		
		for (position = 0; position < command.length(); position++) {
			char character = command.charAt(position);
		
			switch (character) {
				case '+':
					commandStack.push(command.charAt(++position));
					break;
				case '-':
					System.out.print(commandStack.pop());
					break;
				default:
					continue;
			}
		}
	}
}