import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class Main {
	private UserGUI userGUI;
	private DBConnection dbConnection;
	private BookDialog bookDialog;
	private LoginGUI loginDialog;
	private Vector<Vector<Object>> data;
	private String ISBN, title, author, publisher, genres;
	private String col1, col2, col3, col4, col5, col6, col7;
	private int quantity;
	private double price;
	private boolean change = false;
	public Main() {
		/* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
		loginDialog = new LoginGUI();
		loginDialog.setVisible(true);
		
		// event listener when press enter in password field
		loginDialog.addPressedEnterEvent(new AbstractAction(){
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				login();			
			}
		});
		
		//login button listener
		loginDialog.loginBtnActionPerformed(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
			
		});
		
	}
	
	public void login(){
		String username = loginDialog.getUsername();
		char[] password = loginDialog.getPassword();
		
		//connect to the database
		dbConnection = new DBConnection();
		if (dbConnection.CheckUser(username, password)){
			userGUI = new UserGUI();
			userGUI.setVisible(true);
			loginDialog.dispose();
			loadingData();
			buttonListener();
		};
	}
	
	public void buttonListener(){
		//listening for click close window
		userGUI.windowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if(change){
					int confirm = JOptionPane.showConfirmDialog(null,
	                        "Data changed. Save before exit Y/N?");
					if (confirm == JOptionPane.YES_OPTION) {
						dbConnection.commitChange();
						System.exit(0);
	                } else if(confirm == JOptionPane.NO_OPTION) {
	                	dbConnection.rollBackChange();
	                	System.exit(0);
	                }
				} else {
					int confirm = JOptionPane.showOptionDialog(null,
	                        "Quit Book Managerment Application?",
	                        "Confirmation", JOptionPane.YES_NO_OPTION,
	                        JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (confirm == JOptionPane.YES_OPTION) {
						System.exit(0);
	                }
				}
			}
		});

		//listening for selected row
		userGUI.tableListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
            	 if (!event.getValueIsAdjusting() && userGUI.getBookTable().getSelectedRow() != -1){
	            	//enable remove and edit button
	            	userGUI.setRemoveBtnEnabled();
	            	userGUI.setEditBtnEnabled();
	            	
	            	//get data in selected row
	            	col1 = userGUI.getBookTable().getValueAt(userGUI.getBookTable().getSelectedRow(), 0).toString();
	            	col2 = userGUI.getBookTable().getValueAt(userGUI.getBookTable().getSelectedRow(), 1).toString();
	            	col3 = userGUI.getBookTable().getValueAt(userGUI.getBookTable().getSelectedRow(), 2).toString();
	            	col4 = userGUI.getBookTable().getValueAt(userGUI.getBookTable().getSelectedRow(), 3).toString();
	            	col5 = userGUI.getBookTable().getValueAt(userGUI.getBookTable().getSelectedRow(), 4).toString();
	            	col6 = userGUI.getBookTable().getValueAt(userGUI.getBookTable().getSelectedRow(), 5).toString();
	            	col7 = userGUI.getBookTable().getValueAt(userGUI.getBookTable().getSelectedRow(), 6).toString();
            	 }
            }
		});
		// add button listener
		userGUI.addBtnActionPerformed(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//adding book process
				addingBook();
			}
			
		});
		
		//remove button listener
    	userGUI.removeBtnActionPerformed(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Do you want to remove this book?");
				if(result == JOptionPane.YES_OPTION){
					removeBook(col1);
					userGUI.setSaveBtnEnabled();
					userGUI.setDiscardBtnEnabled();
					userGUI.setRemoveBtnDisabled();
	            	userGUI.setEditBtnDisabled();
	            	change = true;
				}
			}
    		
    	});
    	
    	//edit button listener
    	userGUI.editBtnActionPerformed(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				editBook();
				backBtnListener();
				userGUI.setSaveBtnEnabled();
				userGUI.setDiscardBtnEnabled();
				change = true;
			}
    		
    	});
    	
		// save change button listener
		userGUI.saveBtnActionPerformed(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Data changed. Save Y/N?");
				if(result == JOptionPane.YES_OPTION){
					// commit to change database
					dbConnection.commitChange();
					
					//disable "Save Changes" and "Discard" button
					userGUI.setSaveBtnDisabled();
					userGUI.setDiscardBtnDisabled();
					change = false;
				}
			}
			
		});
		
		//discard button listener
		userGUI.discardBtnActionPerformed(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Data changed. Discard Y/N?");
				if(result == JOptionPane.YES_OPTION){
					dbConnection.rollBackChange();
					loadingData();
					userGUI.setSaveBtnDisabled();
					userGUI.setDiscardBtnDisabled();
					change = false;
				}
			}
			
		});
		
		//search button listener
		userGUI.searchBtnActionPerformed(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				searchingBook();
			}
			
		});
		
		//back button listener
		userGUI.backBtnActionPerformed(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				loadingData();
				userGUI.setBackBtnDisable();
			}
			
		});
	}
	
	public void loadingData(){
		//loading data thread
		Thread loadDataThread = new Thread(new Runnable(){
			@Override
			public void run() {
				data = dbConnection.loadingBooks();
				userGUI.setTable(data);
			}});
		loadDataThread.start();
	}
	
	public void addingBook(){
		//set header of dialog and button
		bookDialog = new BookDialog("ADDING BOOK", "Add");
		bookDialog.setVisible(true);
		
		//add button listener
		bookDialog.addBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//check if book is already in database
				ISBN =  bookDialog.getISBN();
				
				//data validation
				if (ISBN.length() == 10 && ISBN.matches("[0-9]+")){	
					try {
						price = Double.parseDouble(bookDialog.getPrice());
						try {
							quantity = Integer.parseInt(bookDialog.getQuantity());
							title = bookDialog.getBookTitle();
							author = bookDialog.getAuthor();
							publisher = bookDialog.getPublisher();
							genres = bookDialog.getGenres();
							if (title.length() > 1 || author.length() > 1 || publisher.length() > 1 || genres.length() > 1){
								
								//execute spAddBook in database
								dbConnection.addBook(ISBN, title, author, publisher, genres, price, quantity);

								JOptionPane.showMessageDialog(null, "Add the new book successful.");
								bookDialog.dispose();
								userGUI.setSaveBtnEnabled();
								userGUI.setDiscardBtnEnabled();
								change = true;
							} else {
								JOptionPane.showMessageDialog(null, "Please fill all required fields!");
							}
							
						} catch (NumberFormatException en) {
							JOptionPane.showMessageDialog(null, "Quantity must be a number.");
						}
					} catch (NumberFormatException en) {
						JOptionPane.showMessageDialog(null, "Price must be a number.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "ISBN must be 10 digits.");
				}
				
				//reload data
				loadingData();
			}
			
		});
		
		backBtnListener();
		
	}
	
	public void searchingBook(){
		bookDialog = new BookDialog("SEARCHING BOOK", "Search");
		bookDialog.setVisible(true);
		bookDialog.addBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ISBN =  bookDialog.getISBN();
				title = bookDialog.getBookTitle();
				author = bookDialog.getAuthor();
				publisher = bookDialog.getPublisher();
				genres = bookDialog.getGenres();
				
				//set value for empty fields
				if(ISBN.length() == 0){
					ISBN = "%";
				}
				if(title.length() == 0){
					title = "%";
				}
				if(author.length() == 0){
					author = "%";
				}
				if(publisher.length() == 0){
					publisher = "%";
				}
				if(genres.length() == 0){
					genres = "%";
				}
				
				getSearchResult();
				bookDialog.dispose();
				userGUI.setBackBtnEnabled();
			}
		});
		
		backBtnListener();
	}
	
	public void editBook(){
		//create edit book dialog
		bookDialog = new BookDialog("EDIT BOOK", "Edit");
		bookDialog.setVisible(true);
		
		//get value of selected row and set them to edit dialog
		bookDialog.setISBN(col1);
		bookDialog.disableISBNEditable();
		bookDialog.setBookTitle(col2);
		bookDialog.setAuthor(col3);
		bookDialog.setPublisher(col4);
		bookDialog.setGenres(col5);
		bookDialog.setPrice(col6);
		bookDialog.setQuantity(col7);
		
		bookDialog.addBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//get values after edit
				ISBN =  bookDialog.getISBN();
				
				//data validation
				if (ISBN.length() == 10 && ISBN.matches("[0-9]+")){	
					try {
						price = Double.parseDouble(bookDialog.getPrice());
						try {
							quantity = Integer.parseInt(bookDialog.getQuantity());
							title = bookDialog.getBookTitle();
							author = bookDialog.getAuthor();
							publisher = bookDialog.getPublisher();
							genres = bookDialog.getGenres();
							if (title.length() > 1 || author.length() > 1 || publisher.length() > 1 || genres.length() > 1){
								String message = "Save Change?\n";
								System.out.println(message.length());
								if(!ISBN.equals(col1)){
									message = message + "ISBN: " + col1 + " -> " + ISBN + "\n";
								}
								
								if(!title.equals(col2)){
									message = message + "Title: " + col2 + " -> " + title + "\n";
								}
								
								if(!author.equals(col3)){
									message = message + "Author: " + col3 + " -> " + author + "\n";
								}
								
								if(!publisher.equals(col4)){
									message = message + "Publisher: " + col4 + " -> " + publisher + "\n";
								}
								
								if(!genres.equals(col5)){
									message = message + "Genres: " + col5 + " -> " + genres + "\n";
								}
								
								if(!bookDialog.getPrice().equals(col6)){
									message = message + "Price: " + col6 + " -> " + bookDialog.getPrice() + "\n";
								}
								
								if(!bookDialog.getQuantity().equals(col7)){
									message = message + "Quantity: " + col7 + " -> " + bookDialog.getQuantity() + "\n";
								}
								
								if (message.length() != 13){
									int result = JOptionPane.showConfirmDialog(null, message);
									if (result == JOptionPane.YES_OPTION){
										//execute spAddBook in database
										dbConnection.dbEditBook(col1, ISBN, title, author, publisher, genres, price, quantity);
										JOptionPane.showMessageDialog(null, "Edit the book successful.");
										bookDialog.dispose();
										userGUI.setSaveBtnEnabled();
										userGUI.setDiscardBtnEnabled();
										change = true;
									}
								} else {
									JOptionPane.showMessageDialog(null, "You doesn't change anything");
								}
							} else {
								JOptionPane.showMessageDialog(null, "Please fill all required fields!");
							}
							
						} catch (NumberFormatException en) {
							JOptionPane.showMessageDialog(null, "Quantity must be a number.");
						}
					} catch (NumberFormatException en) {
						JOptionPane.showMessageDialog(null, "Price must be a number.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "ISBN must be 10 digits.");
				}
				
				//reload data
				loadingData();
			}
		});
		backBtnListener();
	}
	
	public void removeBook(String ISBN){
		dbConnection.dbRemoveBook(ISBN);
		((DefaultTableModel)userGUI.getBookTable().getModel()).removeRow(userGUI.getBookTable().getSelectedRow());
	}
	
	public void backBtnListener(){
		bookDialog.addBackBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				bookDialog.dispose();
				
			}
			
		});
	}
	
	public void getSearchResult(){
		//loading Search Result thread
		Thread loadDataThread = new Thread(new Runnable(){
			@Override
			public void run() {
				data = dbConnection.searchingBook(ISBN, title, author, publisher, genres);
				userGUI.setTable(data);
			}});
		loadDataThread.start();
	}
	
	public static void main(String[] argv){
		new Main();
	}
}
