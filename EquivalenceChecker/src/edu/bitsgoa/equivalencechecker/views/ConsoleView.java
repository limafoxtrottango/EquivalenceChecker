package edu.bitsgoa.equivalencechecker.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class ConsoleView extends ViewPart {

	public static final String ID = "edu.bitsgoa.EquivalenceChecker"; 
	public static Text text;

	public ConsoleView() {
	}

	@Override
	public void createPartControl(final Composite parent) {
				
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
	    Composite composite = new Composite(sc, SWT.NONE);
	    sc.setContent(composite);
	    composite.setLayout(new GridLayout(2, false));
	    
	    text = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY);
	    text.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
	    Font newFont=new Font(text.getDisplay(),new FontData("Monospace",10,SWT.NATIVE));
	    text.setFont(newFont);
	 
	    GridDataFactory.fillDefaults().grab(true, true).hint(400, 400).applyTo(text);
	    sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
	    sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		createActions();
		initializeToolBar();
		initializeMenu();
		
	}

	private void createActions() {
		// Create the actions
	}
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}
	public void setFocus() {
		// Set the focus
	}

}
