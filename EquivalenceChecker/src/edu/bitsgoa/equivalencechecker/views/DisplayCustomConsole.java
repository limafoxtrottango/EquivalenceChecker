package edu.bitsgoa.equivalencechecker.views;

import org.eclipse.swt.widgets.Display;

public class DisplayCustomConsole {
	public static void display(final String message){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run(){
				ConsoleView.text.append(message);
				ConsoleView.text.append("\n");
			}
		});
	}
}
