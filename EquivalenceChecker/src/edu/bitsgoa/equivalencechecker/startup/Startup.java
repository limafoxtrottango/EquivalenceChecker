package edu.bitsgoa.equivalencechecker.startup;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import edu.bitsgoa.equivalencechecker.handlers.Handler;
import edu.bitsgoa.equivalencechecker.utilities.BetterRunProcess;

public class Startup  implements IStartup{
	
static ArrayList<String> paths=new ArrayList<String>(2);
public static String path_to_executable;
	
	public  ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			if (sourcepart != Startup.this) {
			    showSelection(sourcepart, selection);
			}
		}
	};
	
	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) throws NullPointerException {
		if (selection instanceof IStructuredSelection) {		
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object[] ob=ss.toArray();
			String file=null;
			for(int i=0;i<ob.length;i++){
				if((file=ob[i].toString()).endsWith(".c")){
					String path=getPath().substring(0,getPath().lastIndexOf('/'));
					paths.add(0,path+"/"+file);
					if(paths.size()>=2){
						Handler.return_val=true;
						Handler.arg1=paths.get(0);
						Handler.arg2=paths.get(1);
					}
				}
			}
			
		}
		
	}
	public String getPath() {
		String path=null;
		try{
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart(); 
		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		path=ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()+file.getFullPath();
		}catch(NullPointerException e){
			getPath();
		}
		return path;
	}
	
	public void earlyStartup() {
		Bundle bundle=Platform.getBundle("edu.bitsgoa.equivalencechecker");
		URL url_executable = FileLocator.find(bundle, new Path("/Executables/pp_equivalence_check.out"), null);
		path_to_executable = null;
		try {
			url_executable=FileLocator.toFileURL(url_executable);
			path_to_executable=url_executable.toString();
			new File(path_to_executable.substring(path_to_executable.indexOf('/'),path_to_executable.lastIndexOf('/')+1)+"PP_Outputs/").mkdir();
			new File(path_to_executable.substring(path_to_executable.indexOf('/'),path_to_executable.lastIndexOf('/')+1)+"PP_Outputs/"+"Debug/").mkdir();
			new PrintWriter(path_to_executable.substring(path_to_executable.indexOf('/'),path_to_executable.lastIndexOf('/')+1)+"PP_Outputs/"+"Debug/"+"Parsing_Output.txt", "UTF-8");
			changePermissions("pp_equivalence_check.out",path_to_executable.substring(path_to_executable.indexOf('/'),path_to_executable.lastIndexOf('/')+1));
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		IWorkbenchWindow window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		window.getSelectionService().addSelectionListener(listener);
	}
	public static void changePermissions(String filename,String path){
		String[] cmd=new String[3];
		cmd[0]="chmod";
		cmd[1]="u+x";
		cmd[2]=filename;
		BetterRunProcess process=new BetterRunProcess();
		process.runProcessBuilderInDifferentDirectory(cmd,path,1,0,0,"");
	}

}