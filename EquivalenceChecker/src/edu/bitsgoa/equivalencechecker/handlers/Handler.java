package edu.bitsgoa.equivalencechecker.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import edu.bitsgoa.equivalencechecker.startup.Startup;
import edu.bitsgoa.equivalencechecker.utilities.BetterRunProcess;
import edu.bitsgoa.equivalencechecker.views.ConsoleView;
import edu.bitsgoa.equivalencechecker.views.DisplayCustomConsole;

public class Handler extends AbstractHandler implements IHandler{
	
	public static boolean return_val=false;
	public static String arg1=null;		//path to the first .c file
	public static String arg2=null;		//path to the second .c file
	private static String file1=null;	//name of the first file
	private static String file2=null;	//name of the second file
	
	public static void changePermissions(String filename,String path){
		String[] cmd=new String[3];
		cmd[0]="chmod";
		cmd[1]="u+x";
		cmd[2]=filename;
		BetterRunProcess process=new BetterRunProcess();
		process.runProcessBuilderInDifferentDirectory(cmd,path,1,0,0,"");
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		BetterRunProcess process=new BetterRunProcess();
		file1=arg1.substring(arg1.lastIndexOf('/')+1,arg1.length());
		String[] cmd=new String[3];
		cmd[0]="gcc";
		cmd[1]="-fdump-tree-cfg";
		cmd[2]=file1;
		process.runProcessBuilderInDifferentDirectory(cmd,arg1.substring(0,arg1.lastIndexOf('/')),1,0,0,"");
		file2=arg2.substring(arg2.lastIndexOf('/')+1,arg2.length());
		cmd[2]=file2;
		process.runProcessBuilderInDifferentDirectory(cmd,arg2.substring(0,arg2.lastIndexOf('/')),1,0,0,"");
		
		deleteFiles(Startup.path_to_executable.substring(Startup.path_to_executable.indexOf('/'),Startup.path_to_executable.lastIndexOf('/')+1)+"PP_Outputs/");
		deleteFiles(Startup.path_to_executable.substring(Startup.path_to_executable.indexOf('/'),Startup.path_to_executable.lastIndexOf('/')+1)+"PP_Outputs/"+"/Debug/");
		
		DisplayCustomConsole.display("Performing equivalence check for the files: "+file1+" and "+file2+"..."+"\n");
		
		cmd[0]="./pp_equivalence_check.out";
		cmd[1]=arg1+".012t.cfg";
		cmd[2]=arg2+".012t.cfg";
		process.runProcessBuilderInDifferentDirectory(cmd,Startup.path_to_executable.substring(Startup.path_to_executable.indexOf('/'),Startup.path_to_executable.lastIndexOf('/')+1),1, 0,0,"");
		ProcessBuilder builder=new ProcessBuilder("./pp_equivalence_check.out",file1+".012t.cfg",file2+".012t.cfg");
		printResult(Startup.path_to_executable.substring(Startup.path_to_executable.indexOf('/'),Startup.path_to_executable.lastIndexOf('/')+1)+"PP_Outputs/");
				
		return null;
	}
	public boolean isEnabled(){
		return return_val;
	}		
	
	public static void printResult(String path){
		String fullPath=path+"Equivalence_Check_Result.txt";
		ConsoleView.text.append("********************Results Of Equivalence Checker********************"+"\n");
		try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {
			String line;
			while ((line = br.readLine()) != null) {
				DisplayCustomConsole.display(line);
			}

		} catch(FileNotFoundException e){
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	public static void deleteFiles(String path){
		File folder=new File(path);
		File flist[]=folder.listFiles();
		 for (File f : flist) {
             if ((f.getName().endsWith(".txt") && f.getName()!="Parsing_Output.txt")||(f.getName().endsWith(".gv") && f.getName()!="Parsing_Output.txt")) {
                 f.delete(); 
             }
		 }
	}
	
}