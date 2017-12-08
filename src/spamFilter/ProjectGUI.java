package spamFilter;
//These are the import statements required
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import pagelayout.*;
import static pagelayout.EasyCell.*;

//This class contains the list of components used in main window.

public class ProjectGUI
{ 
  protected JFileChooser browseMenu;
  protected JLabel upf;
  protected JLabel orLabel;
  protected JTextField fileName;
  protected JButton browse;
  protected JLabel eth;
  protected JTextArea itemDisplay;
  protected JButton editText;
  protected JButton check;
  protected JButton tam;
  protected JButton cancel;
  protected JButton read;
  protected JScrollPane scroller;
  protected JScrollBar bar;
  protected JLabel emptyLabel;
  protected Row row1;
  protected Row row2;
  protected Row row3;
  protected Row row4;
  protected Row orRow;
  protected Column root;
  protected JPanel mid;
  protected JScrollPane scroll;
  PageLayout layout;

  public static ProjectGUI createGUIObject()
  {
     return new ProjectGUI();
  }
  protected void createComponents() //This function creates components with their text fields and size..etc
  {
     upf = new javax.swing.JLabel();
     upf.setText("Upload File");
     
     fileName = new javax.swing.JTextField();
     fileName.setText("");
     setDimensions(fileName, 29, 6, 25, 32767, 25, 351, 25);
     fileName.setEditable(false);
     
     browse = new javax.swing.JButton();
     browse.setText("Browse");
     
     orLabel=new javax.swing.JLabel("OR");
     
     eth = new javax.swing.JLabel();
     eth.setText("Enter Text Here");
     
     itemDisplay = new javax.swing.JTextArea(16, 40);
     itemDisplay.setText("");
     itemDisplay.setBorder(BorderFactory.createLoweredBevelBorder());
     itemDisplay.setLineWrap(true);
     
     mid=new JPanel();
     scroll=new JScrollPane(itemDisplay);
     scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
     mid.add(scroll);
     
     editText = new javax.swing.JButton();
     editText.setText("Edit Text");
     editText.setEnabled(false);

     tam=new javax.swing.JButton();
     tam.setText("Try Another Message");

     check = new javax.swing.JButton();
     check.setText("Check");
     check.setAlignmentX(Component.RIGHT_ALIGNMENT);
	  check.setEnabled(false);
    
   
     cancel = new javax.swing.JButton();
     cancel.setText("Cancel");
     emptyLabel=new javax.swing.JLabel("                            ");
     
     read=new javax.swing.JButton("Read");
	 read.setVisible(true);
	 read.setEnabled(false);
     
     decorateComponents();   // decorateComponents is a function which adds  event Listeners to all buttons or text fields
  }
  protected void createCells()  //This f'n is to specify rows and columns in frame.will not work unless you have PageLayout
  {
     row1 = row(fileName,browse);
     row2 = row(editText,emptyLabel,tam,emptyLabel,check);
     row3 = row(emptyLabel,emptyLabel,read);
     row4 = row(emptyLabel);
     orRow = row(emptyLabel,emptyLabel,new JLabel("    "),orLabel);
     root = column(upf,row1,row3,orRow ,row4,eth,mid,row2);
     setConstraints();
  }
  public void readClicked(ActionEvent e)   //When read is clicked the f'n is called
  { 
      itemDisplay.setText(SpamFilter.read_file(fileName.getText()));
      itemDisplay.setEditable(false);
      editText.setEnabled(true);
      check.setEnabled(true);
  }
  public void tamClicked(ActionEvent e)   //When Try Another Message (tam) is clicked the f'n is called
  { 
	 fileName.setText("");
	 read.setVisible(true);
	 read.setEnabled(false);
	 itemDisplay.setText("");
	 editText.setEnabled(false);
	 itemDisplay.setEditable(true);
  }
  public void cancelClicked(ActionEvent e)
  {
  }
  public void fileNameEntered(ActionEvent e)
  { 
	  if(fileName.getText().equals(""))
		  read.setVisible(true);
  }
  public void browseClicked(ActionEvent e)   //Browse window appears and the file name selected is set as text to label fileName
  {
     browseMenu = new JFileChooser();
     int r = browseMenu.showOpenDialog(new JFrame());
     if (r == JFileChooser.APPROVE_OPTION)
     {
	     fileName.setText(browseMenu.getSelectedFile().getPath());
	     read.setVisible(true);
	     read.setEnabled(true);
     }
  }
  public void editTextClicked(ActionEvent e)
  {
	  itemDisplay.setEditable(true);
  }
  public void okClicked(ActionEvent e)
  {
  }
  public void checkClicked(ActionEvent e) throws IOException
  {
	  String text = itemDisplay.getText();
	  String s = SpamFilter.spamCat(text);
	  JFrame f2=new JFrame("Result");
      f2.setVisible(true);
      f2.setSize(350,120);
      f2.setResizable(false);
      JLabel classi=new JLabel("Classification :");
      JLabel result=new JLabel(s);
     result.setFont(new Font("papyrus", Font.BOLD, 23));
     result.setForeground(Color.RED);
     Row r1;
     Row r2;
     Row r3;
     JButton c=new JButton("Correct");
     JButton nc=new JButton("Not Correct");
     JButton iok=new JButton("OK");
     iok.setVisible(false);
     JLabel sc=new JLabel("Select Category :");
     JComboBox<String> cb=new JComboBox<String>();
     for(String k : SpamFilter.category)
     {
    	 cb.addItem(k);
     }
     sc.setVisible(false);
     cb.setVisible(false);
     Column c1;
     PageLayout pl;
     JPanel p=new JPanel();
     r1=row(c,emptyLabel,nc);
     r2=row(sc,cb);
     r3=row(emptyLabel,iok);
     c1=column(classi,result,r1,r2,r3);
     pl=c1.createLayout(f2.getContentPane());
     String t = text.replace("\r\n", " ").replace("\n", " ");
      c.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 f2.setVisible(false);
			 SpamFilter.append_text(s, t);
		  }
		});
       nc.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			  f2.setSize(350,180);
			  f2.setResizable(false);
			  sc.setVisible(true);
			  c.setEnabled(false);
			  nc.setEnabled(false);
			  cb.setVisible(true);
			  iok.setVisible(true);
		  }
		});
       iok.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 //add here on what to be done on clicking "OK"
			  f2.setVisible(false);
			  String f = (String) cb.getSelectedItem();
			  SpamFilter.append_text(f, t);
		  }
		});
	    itemDisplay.addKeyListener(new KeyListener() {
		@Override
		public void keyTyped(KeyEvent event) {
			check.setEnabled(true);
		}
		@Override
		public void keyReleased(KeyEvent event) {
		   check.setEnabled(true); 
		}
		@Override
		public void keyPressed(KeyEvent event) {
			check.setEnabled(true);
		}
	});
  }
  private void setConstraints()
  {
  }
/* 
   Override this  method in a subclass 
   to add your own event listeners.
   Here actionListeners have been added to each 
   button to print a message when the button 
   is clicked.
*/
  protected void decorateComponents()
  {  
     cancel.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 cancelClicked(e);
		  }
		});
      check.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 try {
				checkClicked(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }
		});
     fileName.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
		  }
		});
     browse.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 browseClicked(e);
		  }
		});
    editText.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 editTextClicked(e);
		  }
		});
      tam.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 tamClicked(e);
		  }
		});
       read.addActionListener(
		new ActionListener(){
		  public void actionPerformed(ActionEvent e){
			 readClicked(e);
		  }
		});
        itemDisplay.addKeyListener(new KeyListener() {
    @Override
    public void keyTyped(KeyEvent event) {
        check.setEnabled(true);
    }
    @Override
    public void keyReleased(KeyEvent event) {
       check.setEnabled(true);
    }
    @Override
    public void keyPressed(KeyEvent event) {
        check.setEnabled(true);
    }});
  }
/* 
   This method should be called for creating
   the layout for a given container.
*/
  public void createGUI(Container container)
  {
     createComponents();
     createCells();
     layout=root.createLayout(container);
  }
/* 
   This method provides an alternative way to
   create the GUI on a JPanel.
   It returns a JPanel containing the GUI.
*/
  public JPanel getJPanel()
  {
     JPanel p=new JPanel();
     createGUI(p);
     return p;
  }
  public static void testGUI()
  {
     JFrame f=new JFrame("Spam Filter");
     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     createGUIObject().createGUI(f.getContentPane());//f.add(createGUIObject().getJPanel()) could also br used
     f.pack();
     Dimension d=f.getPreferredSize();
     f.setSize(new Dimension(d.width,d.height));
     f.setResizable(false);
     f.setVisible(true);
  }
/* 
   This calls the method for testing the 
   generated code. It should not require
   the generated code to be modified in any way.
*/
  public static void main(String[] args)
  {
     SwingUtilities.invokeLater(
          new Runnable(){public void run(){testGUI();}});
  }
}