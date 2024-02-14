import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class ScriptProcessor {

    private Map<String, Integer> variableMap = new HashMap<>();
    private StringBuilder resultBuilder = new StringBuilder();

    public String processScript(String script) {
        resultBuilder.setLength(0); // Reset the result builder
        String[] instructions = script.split("\n");
        for (int i = 0; i < instructions.length; i++) {
            String instruction = instructions[i].trim();

            if (instruction.isEmpty() || instruction.startsWith("//")) {
                continue;
            }

            if (instruction.toLowerCase().startsWith("print")) {
                handlePrint(instruction);
            } else if (instruction.contains("=") && !instruction.toLowerCase().startsWith("if")) {
                handleAssignment("ASSIGN " + instruction);
            } else if (instruction.startsWith("INPUT")) {
                handleInput(instruction);
            } else if (instruction.startsWith("goto")) {
                i = handleGoto(instruction, i) - 1;
            } else if (instruction.startsWith("if")) {
                i = handleIf(instruction, i) - 1;
            } else if ("end".equalsIgnoreCase(instruction)) {
                break;
            } else {
                resultBuilder.append("Line ").append(i + 1).append(": ERROR: Unknown instruction '").append(instruction).append("'\n");
            }
        }
        return resultBuilder.toString();
    }

    private void handlePrint(String instruction) {
        String[] parts = instruction.split(" ", 2);
        if (parts.length < 2) {
            resultBuilder.append("ERROR: 'PRINT' command requires an argument.\n");
            return;
        }
        String expression = parts[1];
        if (variableMap.containsKey(expression)) {
            resultBuilder.append(variableMap.get(expression)).append("\n");
        } else {
            try {
                int value = Integer.parseInt(expression);
                resultBuilder.append(value).append("\n");
            } catch (NumberFormatException e) {
                resultBuilder.append("ERROR: '").append(expression).append("' is not a valid number or variable.\n");
            }
        }
    }

    private void handleAssignment(String instruction) {
        String[] parts = instruction.split("=", 2);
        if (parts.length < 2) {
            resultBuilder.append("ERROR: 'ASSIGN' command syntax is incorrect.\n");
            return;
        }
        String variableName = parts[0].replace("ASSIGN", "").trim();
        String expression = parts[1].trim();

        expression = replaceVariables(expression);

        try {
            int result = evaluate(expression);
            variableMap.put(variableName, result);
        } catch (Exception e) {
            resultBuilder.append("ERROR: ").append(e.getMessage()).append(" in '").append(instruction).append("'.\n");
        }
    }

    private String replaceVariables(String expression) {
        for (Map.Entry<String, Integer> entry : variableMap.entrySet()) {
            expression = expression.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue().toString());
        }
        return expression;
    }

    private void handleInput(String instruction) {
        String variableName = instruction.replace("INPUT", "").trim();
        String input = JOptionPane.showInputDialog("Enter value for " + variableName + ":");
        try {
            int value = Integer.parseInt(input);
            variableMap.put(variableName, value);
        } catch (NumberFormatException e) {
            resultBuilder.append("ERROR: Invalid input for '").append(variableName).append("'.\n");
        }
    }

    private int handleGoto(String instruction, int currentLine) {
        String[] parts = instruction.split(" ");
        if (parts.length < 2) {
            resultBuilder.append("ERROR: 'GOTO' command syntax is incorrect.\n");
            return currentLine;
        }
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            resultBuilder.append("ERROR: 'GOTO' command contains invalid line number '").append(parts[1]).append("'.\n");
            return currentLine;
        }
    }

    private int handleIf(String instruction, int currentLine) {
        String pattern = "if\\s*\\(?\\s*(\\w+)\\s*=\\s*(\\d+)\\s*\\)?\\s*goto\\s*(\\d+)";
        Matcher matcher = Pattern.compile(pattern).matcher(instruction);
        if (matcher.find()) {
            String variableName = matcher.group(1);
            int expectedValue = Integer.parseInt(matcher.group(2));
            int gotoLine = Integer.parseInt(matcher.group(3));

            Integer actualValue = variableMap.get(variableName);
            if (actualValue != null && actualValue == expectedValue) {
                return gotoLine;
            }
        } else {
            resultBuilder.append("ERROR: 'IF' command syntax is incorrect.\n");
        }
        return currentLine;
    }

    private int evaluate(String expression) throws Exception {
        String postfix = convertToPostfix(expression);
        return calculatePostfix(postfix);
    }

    private String convertToPostfix(String infix) {
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

    private int calculatePostfix(String postfix) {
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
        return (int) Math.round(stack.pop());  
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
}
