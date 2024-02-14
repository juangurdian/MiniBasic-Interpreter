import java.awt.*;
import javax.swing.*;

public class lab3App {
	public static void main(String args[]) {
		lab3View view = new lab3View();
		lab3Model model = new lab3Model();
		lab3Controller controller = new lab3Controller(view, model);
	}
}
