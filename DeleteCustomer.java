import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class DeleteCustomer extends JInternalFrame implements ActionListener {

	private JPanel JpanelDel = new JPanel();
	private JLabel labelNo, labelName, labelDate, labelBalance;
	private JTextField TextNo, TextName, TextDate, TextBalance;
	private JButton btndeleteete, btnCancel;

	private int recordCount = 0;
	private int rows = 0;
	private	int total = 0;

	
	private String records[][] = new String [500][6];

	private FileInputStream fs;
	private DataInputStream ds;

	DeleteCustomer () {

		
		super ("Delete user", false, true, false, true);
		setSize (351, 236);

		JpanelDel.setLayout (null);

		labelNo = new JLabel ("Account-Number:");
		labelNo.setForeground (Color.red);
		labelNo.setBounds (16, 21, 80, 25);
	        labelName = new JLabel ("Name:");
		labelName.setForeground (Color.black);
	        labelName.setBounds (16, 55, 90, 25);
		labelDate = new JLabel ("Latest Transaction:");
		labelDate.setForeground (Color.black);
		labelDate.setBounds (16, 90, 100, 25);
		labelBalance = new JLabel ("the Balance:");
		labelBalance.setForeground (Color.black);
		labelBalance.setBounds (16, 126, 80, 25);

		TextNo = new JTextField ();
		TextNo.setHorizontalAlignment (JTextField.RIGHT);
		TextNo.setBounds (126, 21, 210, 26);
		TextName = new JTextField ();
		TextName.setEnabled (false);
		TextName.setBounds (126, 55, 210, 26);
		TextDate = new JTextField ();
		TextDate.setEnabled (false);
		TextDate.setBounds (126, 90, 210, 26);
		TextBalance = new JTextField ();
		TextBalance.setEnabled (false);
		TextBalance.setHorizontalAlignment (JTextField.RIGHT);
		TextBalance.setBounds (126, 126, 210, 26);

		
		btndeleteete = new JButton ("Delete");
		btndeleteete.setBounds (21, 165, 121, 26);
		btndeleteete.addActionListener (this);
		btnCancel = new JButton ("Cancel");
		btnCancel.setBounds (210, 165, 121, 26);
		btnCancel.addActionListener (this);

	
		JpanelDel.add (labelNo);
		JpanelDel.add (TextNo);
		JpanelDel.add (labelName);
		JpanelDel.add (TextName);
		JpanelDel.add (labelDate);
		JpanelDel.add (TextDate);
		JpanelDel.add (labelBalance);
		JpanelDel.add (TextBalance);
		JpanelDel.add (btndeleteete);
		JpanelDel.add (btnCancel);

	
		TextNo.addKeyListener (new KeyAdapter() {
			public void keyTyped (KeyEvent ke) {
				char c = ke.getKeyChar ();
				if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep ();
					ke.consume ();
      				}
    			}
  		}
		);
		
		TextNo.addFocusListener (new FocusListener () {
			public void focusGained (FocusEvent e) { }
			public void focusLost (FocusEvent fe) {
				if (TextNo.getText().equals ("")) { }
				else {
					rows = 0;
					populateArray ();	
					findRec ();		
				}
			}
		}
		);

		
		getContentPane().add (JpanelDel);

		populateArray ();	

		
		setVisible (true);

	}

	
	public void actionPerformed (ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btndeleteete) {
			if (TextNo.getText().equals("")) {
				JOptionPane.showMessageDialog (this, "Please! Provide Id of Customer.",
						"BankSystem - EmptyField", JOptionPane.PLAIN_MESSAGE);
				TextNo.requestFocus();
			}
			else {
				deletion ();	
			}
		}
		if (obj == btnCancel) {
			txtClear ();
			setVisible (false);
			dspose();
		}

	}

	
	void populateArray () {

		try {
			fs = new FileInputStream ("Bank.dat");
			ds = new DataInputStream (fs);
			
			while (true) {
				for (int i = 0; i < 6; i++) {
					records[rows][i] = ds.readUTF ();
				}
				rows++;
			}
		}
		catch (Exception ex) {
			total = rows;
			if (total == 0) {
				JOptionPane.showMessageDialog (null, "Records  File is null.\niput a record inoder to show.",
					"BankSystem - EmptyFile", JOptionPane.PLAIN_MESSAGE);
				btnEnable ();
			}
			else {
				try {
					ds.close();
					fs.close();
				}
				catch (Exception exp) { }
			}
		}

	}

	
	void findRec () {

		boolean found = false;
		for (int x = 0; x < total; x++) {
			if (records[x][0].equals (TextNo.getText())) {
				found = true;
				showRec (x);
				break;
			}
		}
		if (found == false) {
			String str = textNo.getText ();
			txtClear ();
			JOptionPane.showMessageDialog (this, "YOUR ACCOUNT number. " + str + " doesn't Exist.",
					"Bank system- wrong number", JOptionPane.PLAIN_MESSAGE);
		}

	}

	void showRec (int intRec) {

		textNo.setText (records[intRec][0]);
		TextName.setText (records[intRec][1]);
		TextDate.setText (records[intRec][2] + ", " + records[intRec][3] + ", " + records[intRec][4]);
		TextBalance.setText (records[intRec][5]);
		recordCount = intRec;

	}

	
	void deletion () {

		try {
		
		    	int reply = JOptionPane.showConfirmDialog (this,
					" Sure \n" + TextName.getText () + " Bank-System?",
					"Bank System - Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if (reply == JOptionPane.YES_OPTION) {
				delRec ();	
			}
			else if (reply == JOptionPane.NO_OPTION) { }
		} 

		catch (Exception e) {}

	}


	void delRec () {

		try {
			if (records != null) {
				for(int i = recordCount; i < total; i++) {
					for (int r = 0; r < 6; r++) {
						records[i][r] = records[i+1][r];				
						if (records[i][r] == null) break;
					}
				}
				total = total - 1;
				deleteFile ();
			}
		}
		catch (ArrayIndexOutOfBoundsException ex) { }

	}

	
	void deleteFile () {

		try {
			FileOutputStream fostream = new FileOutputStream ("Bank.dat");
			DataOutputStream dostream = new DataOutputStream (fostream);
			if (records != null) {
				for (int i = 0; i < total; i++) {
					for (int r = 0; r < 6; r++) {
						dostream.writeUTF (records[i][r]);
						if (records[i][r] == null) break;
					}
				}
				JOptionPane.showMessageDialog (this, " Deleted Successfuly.",
					"Record Deleted", JOptionPane.PLAIN_MESSAGE);
				txtClear ();
			}
			else { }
			dostream.close();
			fostream.close();
		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog (this, "error with  file",
						"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
		}

	}

	
	void txtClear () {

		textNo.setText ("");
		TextName.setText ("");
		TextDate.setText ("");
		TextBalance.setText ("");
		textNo.requestFocus ();

	}

	
	void btnEnable () {

		textNo.setEnabled (false);
		btndeleteete.setEnabled (false);

	}

}	