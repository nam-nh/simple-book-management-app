import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.KeyStroke;


@SuppressWarnings("serial")
public class LoginGUI extends JFrame {
    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameLabel;
    private ActionMap actions;
    
	public LoginGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        loginBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        usernameLabel.setText("Username");

        passwordLabel.setText("Password");

        loginBtn.setText("Login");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Book Manager Application");
        
        InputMap input = password.getInputMap();
	    KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
	    input.put(enter, "text-submit");
	    actions = password.getActionMap();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameLabel)
                            .addComponent(passwordLabel))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(username)
                            .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(loginBtn)))
                .addGap(100, 100, 100))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(87, 87, 87))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(loginBtn)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }                        

    public void loginBtnActionPerformed(java.awt.event.ActionListener evt) {                                         
        loginBtn.addActionListener(evt);
    }
    
    public void addPressedEnterEvent(AbstractAction aa){
		actions.put("text-submit", aa);
	}
    
    public String getUsername() {
    	return username.getText();
    }
    
    public char[] getPassword() {
    	return password.getPassword();
    }

}
