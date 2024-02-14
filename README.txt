# MINIBASIC Interpreter

This Java project is a simple interpreter for a MINIBASIC programming language, designed for educational purposes. It features a graphical user interface (GUI) built with Swing, allowing users to write, save, read, and execute MINIBASIC scripts. The interpreter supports basic programming constructs such as variable declarations, arithmetic operations, conditional statements, and loops through the use of `goto` statements.

## Features

- **Script Execution**: Run MINIBASIC scripts and view output within the application.
- **File Operations**: Save and read scripts from files, facilitating code reuse and sharing.
- **Input Handling**: Dynamically request input from users during script execution.
- **Conditional Logic and Loops**: Implement logic flow control using `if` statements and `goto` commands for loops and conditional branching.
- **Error Handling**: Display syntax or runtime errors to users, aiding in debugging scripts.

## Components

- `lab3App.java`: Initializes the MVC components and starts the application.
- `lab3Controller.java`: Manages user actions, linking the GUI with the model.
- `lab3Model.java`: Contains the logic for interpreting and executing MINIBASIC scripts.
- `lab3View.java`: Defines the GUI layout, including text areas for script input/output and control buttons for script operations.

## How to Run

1. Make sure Java is installed on your machine.
2. Compile the Java files using a Java compiler (e.g., `javac lab3*.java`).
3. Launch the application by running `java lab3App`.

## Usage

1. **Writing Scripts**: Type your MINIBASIC code in the upper text area.
2. **Running Scripts**: Click the "Run" button to execute the script. Output will be displayed in the lower text area.
3. **Saving Scripts**: To save your script to a file, click the "Save" button and choose a destination.
4. **Reading Scripts**: Load a previously saved script by clicking the "Read" button and selecting the file.
5. **Resetting the Environment**: Clear both input and output areas by clicking the "Reset" button.

## Supported Commands

- `PRINT <expression>`: Display the value of an expression or variable.
- `<variable> = <expression>`: Assign the result of an expression to a variable.
- `INPUT <variable>`: Request input from the user and store it in a variable.
- `IF <condition> GOTO <line>`: Conditional execution based on a simple comparison.
- `GOTO <line>`: Jump to a specific line number in the script.
- `//`: Comment line, ignored during execution.
- `END`: Terminates the script.

## Example Script

```basic
INPUT X
Y = X + 1
PRINT Y
IF Y = 10 GOTO 5
PRINT "Done"
END
