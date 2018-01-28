package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

@SuppressWarnings("serial")
public class TextEditor extends JFrame {
    private JTextArea area = new JTextArea(100,120);
    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    private String currentFile = "Untitled";
    private boolean changed = false;
    private KeyListener k1;
    private Action New;
    private Action saveAs;
    private Action open;
    private Action save;
    private Action quit;
	
    
    
    public TextEditor() {
    	area.setFont(new Font("Monospaced", Font.PLAIN, 12));
    	JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	add(scroll, BorderLayout.CENTER);
    	
    	JMenuBar menu = new JMenuBar();
    	setJMenuBar(menu);
    	JMenu file = new JMenu("File");
    	JMenu edit = new JMenu("Edit");
    	menu.add(file);
    	menu.add(edit);
    	
    	New = new AbstractAction("New") {
    		public void actionPerformed(ActionEvent e) {
    			new TextEditor();
    			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    		}
    	};
    	
    	saveAs = new AbstractAction("Save As") {
    		public void actionPerformed(ActionEvent e) {
    			saveFileAs();
    		}
    	};
    	
    	open = new AbstractAction("Open", new ImageIcon("open.gif")) {
    		public void actionPerformed(ActionEvent e) {
    			saveOld();
    			if(dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    				readInFile(dialog.getSelectedFile().getAbsolutePath());
    			}
    			saveAs.setEnabled(true);
    		}
    	};
    	
    	save = new AbstractAction("Save", new ImageIcon("save.gif")) {
    		public void actionPerformed(ActionEvent e) {
    			if(!currentFile.equals("Untitled")){
    				saveFile(currentFile);
    			}
    			else {
    				saveFileAs();
    			}
    		}
    	};
    	
    	quit = new AbstractAction("Quit") {
    		public void actionPerformed(ActionEvent e) {
    			saveOld();
    			System.exit(0);
    		}
    	};
    	
    	file.add(New);
    	file.add(open);
    	file.add(save);
    	file.add(saveAs);
    	file.add(quit);
    	file.addSeparator();
    	
    	for (int i = 0; i < 4; i++) {
    		file.getItem(i).setIcon(null);
    	}
    	
    	ActionMap m = area.getActionMap();
    	
    	Action cut = m.get(DefaultEditorKit.cutAction);
    	Action copy = m.get(DefaultEditorKit.copyAction);
    	Action paste = m.get(DefaultEditorKit.pasteAction);
    	
    	edit.add(cut);
		edit.add(copy);
    	edit.add(paste);
    	
    	JToolBar tool = new JToolBar();
    	add(tool, BorderLayout.NORTH);
    	tool.add(New);
    	tool.add(open);
    	tool.add(save);
    	tool.addSeparator();
    	
    	JButton Cut = (JButton) tool.add(cut), 
    			cop = (JButton) tool.add(copy), 
    			pas = (JButton) tool.add(paste);
    	
    	Cut.setText(null);
    //	Cut.setIcon(new ImageIcon("Editing-Cut-Filled-icon.png"));
    	
    	cop.setText(null);
   // 	cop.setIcon(new ImageIcon("copy-icon.png"));
    	
    	pas.setText(null);
   // 	pas.setIcon(new ImageIcon("Editing-Paste-icon.png"));
    	
    	save.setEnabled(false);
    	saveAs.setEnabled(true);
    	
    	k1 = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
        		changed = true;
        		save.setEnabled(true);
        		saveAs.setEnabled(true);
        	}
        };
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		System.exit(0);
        	}
        });
        	
    	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	pack();
    	area.addKeyListener(k1);
    	setTitle(currentFile + " - Farahino's Text Editor");
    	setVisible(true);
    	
    }
    
    public static void main(String[] args) {
    	new TextEditor();
    }
    
    private void saveFileAs() {
	    if(dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	saveFile(dialog.getSelectedFile().getName());
	    }
	}

	private void saveFile(String newFile) {
		try {
			FileWriter w = new FileWriter(newFile);
			area.write(w);
			w.close();
			currentFile = newFile;
			setTitle(currentFile + " - Farahino's Text Editor");
			changed = false;
			save.setEnabled(false);
			
		/*	if(currentFile == JFileChooser.) {
				
			} */
			
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error saving the file " + currentFile + ".", 
					"Error", JOptionPane.OK_OPTION);
		}
	}

	private void saveOld() {
		if(changed) {
			if(JOptionPane.showConfirmDialog(this, "Would you like to save " + currentFile + 
					" ?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				saveFile(currentFile);
			}
		}
		
	}
    
	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			area.read(r, null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile + " - Java Text Editor");
			changed = false;
			
		}
		catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Cannot find the file " + fileName);
		}
		
	}
	
	
}
