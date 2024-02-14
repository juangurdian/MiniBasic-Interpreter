import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;


public class lab3Model {



    private HashMap<String, Integer> variables = new HashMap<>();
    private StringBuilder output = new StringBuilder();

    public String execute(String code) {
        output.setLength(0); // Clear the output before executing new code
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.startsWith("//") || line.isEmpty()) {
                continue;
            } else if (line.toLowerCase().startsWith("print")) {
                print(line);
            } else if (line.contains("=") && !line.toLowerCase().startsWith("if")) {
                let("LET " + line);  // Prepend LET to the line
            } else if (line.startsWith("INPUT")) {
                input(line);
            } else if (line.startsWith("goto")) {
                String[] parts = line.split(" ");
                if (parts.length < 2) {
                    output.append("ERROR: Invalid GOTO command\n");
                    continue;
                }
                try {
                    int gotoLine = Integer.parseInt(parts[1]);
                    i = gotoLine - 2; // Adjusting for 0-based index and the upcoming i++ in the loop
                } catch (NumberFormatException e) {
                    output.append("ERROR: Invalid line number in GOTO command: ").append(parts[1]).append("\n");
                }
            } else if (line.startsWith("if")) {
                String regex = "if\\s*\\(?\\s*(\\w+)\\s*=\\s*(\\d+)\\s*\\)?\\s*goto\\s*(\\d+)";
                Matcher matcher = Pattern.compile(regex).matcher(line);
                if (matcher.find()) {
                    String varName = matcher.group(1);
                    int value = Integer.parseInt(matcher.group(2));
                    int gotoLine = Integer.parseInt(matcher.group(3));

                    Integer varValue = variables.get(varName);
                    if (varValue != null && varValue == value) {
                        i = gotoLine - 2;
                    }
                } else {
                    output.append("ERROR: Invalid IF command\n");
                }
            
            } else if (line.equals("end")) {
                break; // Terminate the execution
            } else {
                output.append("Line ").append(i + 1).append(": ERROR: Unknown command: ").append(line).append("\n");
            }
        }
        return output.toString();
    }

    
    

    

    private void print(String line) {
        String[] parts = line.split(" ", 2);
        if (parts.length < 2) {
            output.append("ERROR: Invalid PRINT command\n");
            return;
        }
        String expr = parts[1];
        if (variables.containsKey(expr)) {
            output.append(variables.get(expr)).append("\n");
        } else {
            try {
                int value = Integer.parseInt(expr);
                output.append(value).append("\n");
            } catch (NumberFormatException e) {
                output.append("ERROR: Unknown variable or invalid number: ").append(expr).append("\n");
            }
        }
    }

    private void let(String line) {
        String[] parts = line.split("=", 2);
        if (parts.length < 2) {
            output.append("ERROR: Invalid LET command\n");
            return;
        }
        String varName = parts[0].replace("LET", "").trim();
        String valueStr = parts[1].trim();

        // Replace variable names with their values
        for (Entry<String, Integer> entry : variables.entrySet()) {
            valueStr = valueStr.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue().toString());
        }

        try {
            int result = evaluateExpression(valueStr);
            variables.put(varName, result);
        } catch (Exception e) {
            output.append("ERROR on line ").append(line).append(": ").append(e.getMessage()).append("\n");
        }
    }



    private void input(String line) {
    	 String varName = line.replace("INPUT", "").trim();
    	    String inputValue = JOptionPane.showInputDialog("Enter value for " + varName + ":");
    	    try {
    	        int value = Integer.parseInt(inputValue);
    	        variables.put(varName, value);
    	    } catch (NumberFormatException e) {
    	        output.append("ERROR: Invalid input for variable ").append(varName).append("\n");
    	    }
    }
    private int evaluateExpression(String expr) throws Exception {
        String postfix = infixToPostfix(expr);
        return evaluatePostfix(postfix);
    }
    private static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();

        StringTokenizer tokenizer = new StringTokenizer(infix, "+-*/()", true);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            char c = token.charAt(0);
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                    while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                        postfix.append(operators.pop()).append(' ');
                    }
                    operators.push(c);
                    break;
                case '(':
                    operators.push(c);
                    break;
                case ')':
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        postfix.append(operators.pop()).append(' ');
                    }
                    operators.pop();
                    break;
                default:
                    postfix.append(token).append(' ');
                    break;
            }
        }
        while (!operators.isEmpty()) {
            postfix.append(operators.pop()).append(' ');
        }
        return postfix.toString();
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private static int evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(postfix);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            char c = token.charAt(0);
            if (Character.isDigit(c)) {
                stack.push(Double.parseDouble(token));
            } else {
                double op2 = stack.pop();
                double op1 = stack.pop();
                switch (c) {
                    case '+':
                        stack.push(op1 + op2);
                        break;
                    case '-':
                        stack.push(op1 - op2);
                        break;
                    case '*':
                        stack.push(op1 * op2);
                        break;
                    case '/':
                        stack.push(op1 / op2);
                        break;
                }
            }
        }
        return (int) Math.round(stack.pop());  // Change here: use Math.round
    } 
    
    

 }