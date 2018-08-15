package client;

import org.json.*;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class clientGUI {

	private JFrame frame;
	private JTextField textField;
	private JButton btnSearch; 
	private JButton btnAdd;
	private JButton btnRemove;
	private JTextArea textArea;
	//private JTextField meaning;
	private DictionaryClient clientDict;
	private String IP;
	private int port;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		String ip=args[0];
		int p=Integer.parseInt(args[1]);
		
		/*catch(NumberFormatException e)
		{
			System.out.println("please input the correct port format.try again please");
		}*/
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clientGUI window = new clientGUI(ip,p);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public clientGUI(String IP,int port) throws Exception{
		this.IP=IP;
		this.port=port;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gridBagLayout);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.weightx = 0.5;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		btnSearch = new JButton("Search");
		GridBagConstraints gbc_btnSearchButton = new GridBagConstraints();
		gbc_btnSearchButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchButton.gridx = 1;
		gbc_btnSearchButton.gridy = 0;
		panel.add(btnSearch, gbc_btnSearchButton);
		
		btnRemove = new JButton("remove");
		GridBagConstraints gbc_btnRemoveButton = new GridBagConstraints();
		gbc_btnRemoveButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveButton.gridx = 1;
		gbc_btnRemoveButton.gridy = 1;
		panel.add(btnRemove,gbc_btnRemoveButton);
		
		btnAdd = new JButton("add");
		GridBagConstraints gbc_btnAddButton = new GridBagConstraints();
		gbc_btnAddButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddButton.gridx = 1;
		gbc_btnAddButton.gridy = 2;
		panel.add(btnAdd,gbc_btnAddButton);
		
		textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		textArea.setLineWrap(true);
		panel.add(textArea, gbc_textArea);
		
		clientDict = new DictionaryClient(IP,port);
		//clientDict.clientStart();
		Listener listener = new Listener();
		btnSearch.addActionListener(listener);
		btnAdd.addActionListener(listener);	
		btnRemove.addActionListener(listener);	
		/* btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Button clicked!");
				textArea.append(textField.getText()+"\n");
			}
		});*/
	
	}
	
	private class Listener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			String text=null;
		
			 if(e.getSource()== btnSearch)
			 {
				String search= textField.getText();
				if(search.equals("")==false) {
					try {
					   text =clientDict.search(search);
					}catch(Exception ex)
					{
						ex.printStackTrace();
					}
					if(text.equals("error"))
						JOptionPane.showMessageDialog(null, "Cannot connect to the server. Please try again later or check the network connection.", "Warning",JOptionPane.ERROR_MESSAGE);
					else if(text.equals("error1"))
						JOptionPane.showMessageDialog(null, "Please check the IP. Maybe it is wrong.", "Warning",JOptionPane.ERROR_MESSAGE);
					else if(text!=null)
						textArea.setText(text+"\n");
				}
				
			 }
			 
			 if(e.getSource()== btnAdd)
			 {
				 String add = textField.getText();
				 String addMeaning = textArea.getText();
				 
				 if(add.equals("")==false && addMeaning.equals("")==false)
				 {
					 try {
						 text =clientDict.add(add,addMeaning);
						}catch(Exception ex)
						{
							ex.printStackTrace();
						}
					    if(text.equals("error"))
							JOptionPane.showMessageDialog(null, "Cannot connect to the server. Please try again later or check the network connection.", "Warning",JOptionPane.ERROR_MESSAGE);
					    else if(text.equals("error1"))
							JOptionPane.showMessageDialog(null, "Please check the IP. Maybe it is wrong.", "Warning",JOptionPane.ERROR_MESSAGE);
						else if(text!=null)
							textArea.setText(text+"\n");
				 }
				 else if(add.equals("") && addMeaning.equals(""))
				 {
					 
				 }
				 else if(add.equals("") && addMeaning.equals("")==false)
				 {
					 JOptionPane.showMessageDialog(null, "please input the word you want to add", "Warning",JOptionPane.ERROR_MESSAGE);
				 }
				 else
				 {
					 JOptionPane.showMessageDialog(null, "please input the meaning", "Warning",JOptionPane.ERROR_MESSAGE);
				 }
				 
			 }
			 
			 if(e.getSource()== btnRemove)
			 {
				 String remove= textField.getText();
					if(remove.equals("")==false) {
						try {
						   text =clientDict.remove(remove);
						}catch(Exception ex)
						{
							ex.printStackTrace();
						}
						if(text.equals("error"))
							JOptionPane.showMessageDialog(null, "Cannot connect to the server. Please try again later or check the network connection.", "Warning",JOptionPane.ERROR_MESSAGE);
						else if(text.equals("error1"))
							JOptionPane.showMessageDialog(null, "Please check the IP. Maybe it is wrong.", "Warning",JOptionPane.ERROR_MESSAGE);
						else if(text!=null)
							textArea.setText(text+"\n");
					}
					
			 }	 
			
		}
		
	}

}
