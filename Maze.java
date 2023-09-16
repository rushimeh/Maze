//validate data if rows & cols is not below 3 and above 30
//if rows and cols dont match up with the maze itself, line not found
//if they enter a file that cannot be found
//if X is not on border and if S is on border
//if open space on border
//if mouse doesn't exist, if exit doesn't exist
//strings, integer and those type of data types
//no logical errors
//if legend inputted incorrectly, as in it has less than what the document says
//do load path button, where they get to choose to load path, when they want to
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Random;

public class Maze extends JFrame implements ActionListener{
    //Declaring public variables in order to get input from the file
    static Scanner sc = new Scanner(System.in);
    static int rows; //# of rows of the maze inputted or generated
    static int cols; //# of cols of the maze inputted or generated
    
    //for displaying shortest path button on random generated maze
    static String maze[][]; //set maze as data point
    static boolean checkPath = false; //checks if path exists.
    
    //variables for the legend of the maze, if they readfromfile, these values will be changed according to their input
    static String BORD = "BB";
    static String O = "O"; //Character to represent open spaces, initialized so that if they click guaranteed path, it should already have values
    static String MOUSE = "S"; //Character to represent starting position, initialized so that if they click guaranteed path, it should already have values
    static String EXIT = "X"; //Character to represent the exit position
    static String B = "B"; //Character to represent blocked/border spaces
    static JFrame frame; //Declaring a public frame, to add and remove from the GUI according to the user's choices
    static JLabel dataValidation = new JLabel(""); //A public label to add and remove from the top panel, if they enter data that is incorrect from the file
    static JButton findPath = new JButton("Find Shortest Path"); //A find path button that can be added or removed according to what stage the user is in their read from file option
    static JPanel mazePanel = new JPanel(); //A public panel for the maze, where it's components can be removed or added or change their layout according to the current maze
    
    JPanel buttonpanel = new JPanel(); // Create panel for buttons, random vs guaranteed.
	JLabel questionColumns = new JLabel("Columns:"); // create label for columns.
	JTextField columns = new JTextField(20); // create a text field for input for columns.
	JLabel questionRows = new JLabel("Rows:"); // create label for rows.
	JTextField rowsField = new JTextField(20); // create a text field for input for rows.
	JLabel question = new JLabel("Please which feature you would like to see:"); //Label to prompt user to select between options.
	JButton guaranteedPath = new JButton("Guaranteed Path Maze"); //guaranteed path option button
	JButton randomPath = new JButton("Random Maze"); //random path option button
    
    static Random randGen = new Random(); // Initialize random object, for randomizing the positions in the maze.
	
	
	public Maze(){//Constructor method - same name as the Class name, in order to run the generateMaze GUI
	    FlowLayout layout1 = new FlowLayout(); //initialize FlowLayout.
	    GridLayout layout2 = new GridLayout(4,1); //initialize GridLayout.

	    buttonpanel.setLayout(layout1); // set layout to button panel.
	    guaranteedPath.addActionListener(this); //add action listener to guaranteedPath button.
	    randomPath.addActionListener(this); // add action listener to randomPath button.
	    findPath.addActionListener(this);
	    buttonpanel.add(questionColumns); //adding to button panel.
	    buttonpanel.add(columns);//adding to button panel.
	    buttonpanel.add(questionRows);//adding to button panel.
	    buttonpanel.add(rowsField);//adding to button panel.
	    buttonpanel.add(question); //adding to button panel.
	    buttonpanel.add(guaranteedPath); //adding to button panel.
	    buttonpanel.add(randomPath); //adding to button panel.
	    frame.add(buttonpanel, BorderLayout.NORTH); //add button panel to frame, and set north.
	    

	}
	
	
	
	public void actionPerformed(ActionEvent event) { 
		String command = event.getActionCommand(); //checks which button was pressed.
		int row = 0, col = 0; //Initialize rows and columns.
		mazePanel.removeAll();
		mazePanel.revalidate();
		mazePanel.repaint();
		frame.remove(mazePanel);
		try { // Using try-catch to avoid errors such as no input, or string input.
			col = Integer.parseInt(columns.getText()); //reads column text field.
			row = Integer.parseInt(rowsField.getText()); //reads rows text field.
			}catch(Exception e) { //catches error
			}
		if(command.equals("Find Shortest Path")) {
			
		}
		if(row > 2  && col > 2 && row <= 30 && col <= 30 ) { //checks that the rows and columns are 2-30.
			question.setText("Please which feature you would like to see:"); // set text to what it was originally in the case that it was changed.
			rows = row;
			cols = col;
			if(command.equals("Guaranteed Path Maze")) { //if Guaranteed Path option was selected
				int[] startPos= new int[2];
				System.out.println("rows" + " " + rows);
				startPos[0] = (int) (Math.random() * (rows-2)+ 1);
				startPos[1] = (int) (Math.random() * (cols-2) + 1);
				
				maze = generateMaze(startPos, rows, cols);
				maze[startPos[0]][startPos[1]] = MOUSE;
				buttonpanel.add(findPath);
				//get exit of the mouse so that the there will be a valid path
				setExit(maze);
				String mazeCopy[][] = maze;
				addMazePanel(mazeCopy); //without the path
				frame.add(mazePanel);
				frame.revalidate(); // refreshes frame.
				boolean[][] vis = new boolean[rows][cols];
				ArrayList<Integer> path = new ArrayList<Integer>();
				ArrayList<Integer> shortestPath = new ArrayList<Integer>();
				findShortestPath(maze, vis, startPos[0], startPos[1], path, shortestPath);
				for(int i = 0; i < shortestPath.size(); i+=2) {
					int rowP = shortestPath.get(i); //the first index in the pair of coordinates is going to be the row
	                int colP = shortestPath.get(i + 1); //the second index in the pair of coordinates is going to be the col
	                if(maze[rowP][colP].equals(EXIT)){ //If the current cell is the end cell, skip it and don't edit it as a valid path coordinate
	                    continue;
	                }
	                else{ //Otherwise mark the cell as part of the shortest path
	                    maze[rowP][colP] = "/";
	                }
	                checkPath = true;
				}
			}
			else if(command.equals("Random Maze")){ //if code for when Random Path option was selected
				checkPath = false;
				boolean[][] vis = new boolean[rows][cols];
				ArrayList<Integer> path = new ArrayList<Integer>();
				ArrayList<Integer> shortestPath = new ArrayList<Integer>();
				int positionX = 0, positionY = 0;
				buttonpanel.add(findPath);
				maze = formMaze(2, cols, rows); //run formMaze Method.
				frame.add(mazePanel);
				frame.revalidate(); // refreshes frame.
				for(int i = 0; i < maze.length; i++) {
					for(int j = 0; j < maze[i].length; j++) {
						if(maze[i][j] == "S") {
							positionX = i;
							positionY = j;
						}
					}
				}
				addMazePanel(maze);
				frame.revalidate(); // refreshes frame.
				findShortestPath(maze, vis, positionX, positionY, path, shortestPath);
				if(shortestPath.size() > 0) {
					for(int i = 0; i < shortestPath.size(); i+=2) {
						int rowP = shortestPath.get(i); //the first index in the pair of coordinates is going to be the row
		                int colP = shortestPath.get(i + 1); //the second index in the pair of coordinates is going to be the col
		                if(maze[rowP][colP].equals(EXIT)){ //If the current cell is the end cell, skip it and don't edit it as a valid path coordinate
		                    continue;
		                }
		                else{ //Otherwise mark the cell as part of the shortest path
		                    maze[rowP][colP] = "/";
		                }
					}
					checkPath = true;
				}
			}
			if(command.equals("Find Shortest Path")) {
				if(checkPath == false){
					question.setText("No Path");
				}
				else {
					addMazePanel(maze);
					frame.add(mazePanel);
					frame.revalidate(); // refreshes frame.
				}
				
			}
				
			
		} // end of selection, maze is shown.
		else{ // if the input does not meet the given parameters.
			question.setText("Please enter proper amount of columns and rows between 3-30"); // Informs user of the parameters.
		}
		
	} // end of ActionPerformed method.

	/**
	 * This main method creates the main menu in the GUI where they are able to choose from the two options 
	 * of "Read from File" or "Generate Random Maze", and depending on the action listeners set on them, 
	 * the program will run the code required for whichever button was pressed.
	 * 
	 * @param String[] args
	 * 
	 * JPanel mainPanel - a Panel that is used to add and remove the two choices of "Generate Maze" & "Read from file"
	 * JPanel askPanel - another Panel added to the mainPanel which consists of the two choices, and the text
	 * JButton generateMaze and readFromFile, the two options this program gives you in terms of what sort of option you want 
	 * 
	 * @return void
	 * 
	 */
    public static void main(String[] args) {
        
    	
    	frame = new JFrame("2D Maze Assignment"); //set the name for the frame of the GUI
        frame.setSize(500, 500); //set initial size for the frame, which can be adjusted according to the user
        frame.setLayout(new BorderLayout()); //set the frame to borderLayout so it encapsulates everything
        
        JPanel mainPanel = new JPanel(); //create a panel that adds and removes the two choices
        JPanel askPanel = new JPanel(); //create a panel which consists of the two choices
        askPanel.setLayout(new FlowLayout()); //sets the panel to flowLayout so that it is not fixed
        JButton generateMaze = new JButton("Generate Maze"); //button to generate Maze
        JButton readFromFile = new JButton("Read from file"); //button to read from file
        JLabel chooseText = new JLabel("Choose 1 of the options:"); //text to be user friendly to choose from one or the other
        
        //adding an action listener for the generateMaze button, so when it is clicked this will run
        generateMaze.addActionListener(new ActionListener() {
        	/**
        	 * Once the generateMaze button is clicked, this method will first remove the mainPanel with 
        	 * the two choices and then run and call upon the constructor of the Maze() class
        	 * which is used to start the GUI of the generateMaze option.
        	 *
        	 * @param e, event
        	 * 
        	 * @return void
        	 * 
        	 */
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mainPanel.remove(askPanel); //to remove the two choices, we remove the ask panel w/ buttons, to move onto the next step
				mainPanel.revalidate(); //to notify the frame that there has been an update to it's contents
				mainPanel.repaint(); //immediately shows the changes made to the screen
				
				
				//MAYBE REMOVE THE MAZE CONTENTS TO REDUCE THE MEMORY 

				new Maze(); //runs GUI of generateMaze through the constructor
//				frame.add(mazePanel);
			}

        });
        
        //creating a method that will run once the readFromFile button is clicked
        readFromFile.addActionListener(new ActionListener() {
			@Override
			/**
			 * Once the readFromFile button has been clicked, it will remove the initial two choices
			 * given, and then run the GUI for the readFromFile option.
			 * 
			 * 
			 * @param e, action
			 * 
			 * @return void
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mainPanel.remove(askPanel); //to remove the two choices, we remove the ask panel w/ buttons, to move onto the next step
				mainPanel.revalidate(); //to notify the frame that there has been an update to it's contents
				mainPanel.repaint(); //immediately shows the changes made to the screen
				
				
		        runReadFromFileGUI(); //runs the GUI for the readFromFile option
			}
        });
        
        
        
        askPanel.add(chooseText); //adds the text which prompts them to choose one of the two options
        askPanel.add(generateMaze); //adds the generateMaze button to the askPanel
        askPanel.add(readFromFile); //adds the readFromFile button to the askPanel
        mainPanel.add(askPanel); //adds the askPanel to the mainPanel, which can be removed later on

        frame.add(mainPanel); //adds this whole panel to the frame
        

        //Set visibility of the frame
        frame.setVisible(true);
        

    }

    /**
     * Creates a panel for the input of the file and the buttons that are required such as load maze
     * and find path, then adds the panel to the frame and sets action listeners for both "load maze"
     * and "find path"
     * 
     * 
     * @param none
     * 
     * topPanel - panel to contain, input, "loadButton", "findPath", and "dataValidation" component
     * loadMazeButton - button to load the maze from file
     * findPath - button to find the shortest path, which will appear once the loadMazeButton has been clicked
     * dataValidation - label which will appear if there is a problem with the data
     * 
     * 
     * @return void
     * 
     * 
     */
    public static void runReadFromFileGUI() {
    	JPanel topPanel = new JPanel(); //panel for input and buttons of the load maze and findPath and dataValidation
        topPanel.setLayout(new FlowLayout()); //set flowLayout for the topPanel
        
        JLabel fileLabel = new JLabel("Enter the file name without the \".txt\" :"); //set label to prompt user to enter the file name
        fileLabel.setFont(new Font("Arial",Font.PLAIN,15));//set font and size for the prompt
        
        JTextField fileNameField = new JTextField(20); //set text field for the panel, so user can enter
        
        
        JButton loadMazeButton = new JButton("Load Maze");//create button for the load maze
        
        topPanel.add(fileLabel); //add label to the topPanel
        topPanel.add(fileNameField); //add Textfield to the panel
        topPanel.add(loadMazeButton);// add the loadMazeButton to the panel
        
        
        //set appealing visual characteristics for the dataValidation component of the topPanel
        dataValidation.setFont(new Font("Arial",Font.PLAIN,15));
        dataValidation.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    	dataValidation.setHorizontalAlignment(JLabel.CENTER);
    	dataValidation.setVerticalAlignment(JLabel.CENTER);
    	
    	
        topPanel.add(dataValidation); //add the dataValidation JLabel to the top panel, which can be setVisible or not depending on usage
        topPanel.add(findPath); //add the findPath button to the top panel, which can setVisible depending on usage
        
        findPath.setVisible(false); //initially set visibility of findPath button to false so that the user cannot click findPath without loading maze first
        dataValidation.setVisible(false); //initially set visibility of dataValidation label to false, so if there isn't any invalid data, the component won't be visible
        
        frame.add(topPanel, BorderLayout.NORTH); //add the topPanel to the frame and set it to NORTH so that it stays on top even when the maze is added

    	//adding action listener to the loadMaze button
        loadMazeButton.addActionListener(new ActionListener() {
        	
        	/**
        	 * Once button clicked, it removes the mazePanel from the frame and then takes the input given
        	 * and then calls the method of the loadMazeButton in order to load the maze the user gave
        	 * 
        	 * 
        	 * @param e, event
        	 * 
        	 * dataValidation - visibility is set to false, since we don't know if there is invalid data yet
        	 * findPath - visibility is set to true, since we should now allow them to find the shortest path
        	 * 
        	 * @return void
        	 * 
        	 */
        	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				mazePanel.removeAll(); //removes all components from the maze, that might've been there since before
				mazePanel.revalidate(); //tells the frame that there has been a change made
				mazePanel.repaint(); //tells the frame to immediately change the frame's contents
				frame.remove(mazePanel); //removes the mazePanel from the frame
				dataValidation.setVisible(false); //sets visibility of dataValidation to false since, the new maze might not have invalid data
				findPath.setVisible(true); //sets visibility of findPath to true, since once load has been clicked, the maze that has loaded is now ready to be able to find the shortest path
				
				//gets the input from the text field, and puts it into variable input
				String input = fileNameField.getText();
				
				loadMazeButton(input); //gives the file name into the loadMazeButton method, which changes the public mazePanel
		        
				//adds the mazePanel to the mazePanel, so that the maze will be visible now
		        frame.add(mazePanel);

			}
        });
        
        
        //adding an action listener to the findPath button
        findPath.addActionListener(new ActionListener() {
			@Override
			
	       	/**
        	 * Once button clicked, it removes the mazePanel from the frame and then takes the input given
        	 * and then calls the method of the findPathButton in order to load the maze the user gave with 
        	 * the shortest path highlighted
        	 * 
        	 * 
        	 * @param e, event
        	 * 
        	 * dataValidation - visibility is set to false, since we don't know if there is invalid data yet
        	 * findPath - visibility is set to false, since we should now remove this button after it is clicked
        	 * 
        	 * @return void
        	 * 
        	 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mazePanel.removeAll(); //removes all components from the maze, that might've been there since before
				mazePanel.revalidate(); //tells the frame that there has been a change made
				mazePanel.repaint(); //tells the frame to immediately change the frame's contents
				frame.remove(mazePanel); //removes the mazePanel from the frame
				dataValidation.setVisible(false); //sets visibility of dataValidation to false since, the new maze might not have invalid data
				findPath.setVisible(false); //hide findPath after it has been clickd


				//gets the input from the text field, and puts it into variable input
				String input = fileNameField.getText();//

				
				findPathButton(input);//gives the file name into the findPathButton method, which changes the public mazePanel
				
				//adds the mazePanel to the mazePanel, so that the maze will be visible now
		        frame.add(mazePanel);

			}
        });
    }
    
    /**
     * Takes in the file string the user entered, makes a file object and checks if there are no errors 
     * with finding the file, it will send in the file + ".txt" into a method called printMazeAndPath 
     * with an arugment of false, to just plainly load the maze, without any path being shown
     * 
     * 
     * 
     * 
     * @param input - the file name the user enters
     * 
     * 
     * file - a file object with the name the user entered
     * dataValdiation - if problem with getting file,  set visibility to true and set text to something regarding the error with the file
     * findPath - if problem with getting file, set visibility to false since there is an error, that needs to replace this button
     * 
     * @return void
     * 
     * 
     */
    public static void loadMazeButton(String input) {
    	//try to open the file, and if an error occurs catch it and tell the user
        try {
        	//create a file object by appending ".txt" to input
        	File file = new File(input+".txt");
        	Scanner mySc = new Scanner(file); //create a scanner object with the file to check if the file exists
        	mySc.close(); //close the scanner object if the file can be found

        	//sends in the file to print the maze onto the screen/GUI
			printMazeAndPath(file, false); //false means do not print the path, but just the maze itself
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			
			dataValidation.setVisible(true); //set dataValidation to true so that we can tell the user what the error is
			//set the text of the dataVaidation so the user knows what's wrong
			dataValidation.setText("File is not found. Properly enter the file name, without the \".txt\". Make sure that you also have the file in your workspace.");
			
			findPath.setVisible(false); //hide findPath button, since there is an error
			
		}
    }
    
    
    /**
     * Takes in the file string the user entered, makes a file object and checks if there are no errors 
     * with finding the file, it will send in the file + ".txt" into a method called printMazeAndPath 
     * with an argument of "true" this time, to load the maze and the shortest path being printed
     * 
     * 
     * @param input - the file name the user enters
     * 
     * 
     * file - a file object with the name the user entered
     * dataValdiation - if problem with getting file,  set visibility to true and set text to something regarding the error with the file
     * findPath - if problem with getting file, set visibility to false since there is an error, that needs to replace this button
     * 
     * @return void
     * 
     * 
     */
    public static void findPathButton(String input) {
        try {
        	//create a file object by appending ".txt" to input
        	File file = new File(input+".txt"); 
        	Scanner mySc = new Scanner(file); //create a scanner object with the file to check if the file exists
        	mySc.close(); //close the scanner object if the file can be found
        	
        	//sends in the file to print the maze & shortest path onto the screen/GUI
			printMazeAndPath(file, true); //true means print the shortest path on top of the maze
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			
			findPath.setVisible(false); //hide findPath button, since there is an error
			
			dataValidation.setVisible(true); //set dataValidation to true so that we can tell the user what the error is
			//set the text of the dataVaidation so the user knows what's wrong
			dataValidation.setText("File is not found. Properly enter the file name, without the \".txt\". Make sure that you also have the file in your workspace.");
		}
    }
    
    
    
    /**
     * This takes in a file and a boolean and gets the legend input, and if no error, it creates all the
     * arrays needed like visited, maze and a temporary maze, then it will take matrix input and if
     * everything is fine with the matrix input, it will then move printing the maze and the shortest path.
     * 
     * 
     * @param file - the file the user enters
     * 		  disPath - a boolean to determine if the method should display path or not
     * 
     * 
     * error - a boolean variable that checks if taking the input of the legend gives an error
     * findPath - hidden when there is an error
     * maze[][] - a 2D array to hold all the contents of the maze
     * visited[][] - a 2D array to keep track of the moves the computer has made, when it is finding shortest path
     * path - an arrayList that adds the coordinates of a path each time the computer makes a move
     * shortestPath - an arrayLIst that adds the coordinates shortest Path to itself from all different paths
     * start[] - holds the coordinates of the starting position of the mouse
     * 
     * @return void
     * 
     * 
     */
    public static void printMazeAndPath(File file, boolean dispPath) throws FileNotFoundException{
    	
    	
        
        //retrieving the legend input from the user, and seeing if it returns that there is an error
    	//and inside this method, the text is set according to what the error was
        boolean error = getLegendInput(file); 
        //if any sort of error with the legend
        if(error) {
        	//set findPath to not visible
        	findPath.setVisible(false);
        }
        else {
        	
	        String maze[][] = new String[rows][cols]; //Creating an actual maze array, to hold the maze letters
	        boolean visited[][] = new boolean[rows][cols]; //Creating a 2D array to track which cells have been visited
	        
	        //if retrieving the matrix doesn't return an error continue with loading the maze
	        if(!getMatrixInput(file, maze)) { //Retrieving the maze matrix from the file
	        	//add the mazePanel to the frame
		        addMazePanel(maze); //first just print the maze, without any edits or path edited
		        

		        //Declaring and initializing variables for finding the shortest path
		        ArrayList<Integer> path = new ArrayList<>(); //Creating an array to store the path taken to find the end
		        ArrayList<Integer> shortestPath = new ArrayList<>(); //Creating an array to store the shortest path taken
		        int[] start = new int[2]; //Creating an array to store the starting position
		        
		        //Finding the starting position
		        findMouse(maze, start); 
		        //Find the shortest path, and edit the shortestPath array list with the coordinates of the path
		        findShortestPath(maze, visited, start[0], start[1], path, shortestPath);
		
		        
		        //if the user wants to display shortest path then go into this segment of the method, to display a path
				if(dispPath) {
						// TODO Auto-generated method stub
						

				        // if there was a path, the shortestPath wouldn't be empty, so if it is not empty load the maze with the path
				        if(!shortestPath.isEmpty()){ //If there is a valid path
				        	
				        	//since coordinates are added at each adjacent index, we have to increment += 2
				            for (int i = 0; i < shortestPath.size(); i += 2) { //Loop through the shortest path
				            	
				            	
				                int row = shortestPath.get(i); //the first index in the pair of coordinates is going to be the row
				                int col = shortestPath.get(i + 1); //the second index in the pair of coordinates is going to be the col
				                if(maze[row][col].equals(EXIT)){ //If the current cell is the end cell, skip it and don't edit it as a valid path coordinate
				                    continue;
				                }
				                else{ //Otherwise mark the cell as part of the shortest path
				                    maze[row][col] = "/";
				                }
				                
				            }
				            addMazePanel(maze); //add the edited maze array to the frame
				        }
				        else{ //If there is no valid path
				        	dataValidation.setText("NO VALID PATH"); //tell user there is no path
				        	dataValidation.setVisible(true); //make it visible
				        }
				}
	        }else {
	        	//if error came when getting input from matrix, hide findPath button
	        	findPath.setVisible(false);
	        }
        }
        //make frame visible
        frame.setVisible(true);
    	
    }
    
    /**
     * This takes in the string Maze and checks if there are open spaces near the border.
     * If there are open spaces near the border, the code puts it in a pool of possible coordinates 
     * for the exit, and randomly selects one. It sets the exit to this randomized coordinate.
     * 
     * @param maze[][] - a 2D String array to hold all the contents and data of the maze. The exit is stored in this array.
     * 
     * @return void
     * 
     * 
     */
    public static void setExit(String[][] maze) {
    	int dir = (int) (Math.random() * 4); //picks a number between 0-3
    	ArrayList<int[]> exits = new ArrayList<int[]>(); //Initialize exits arrayList that holds arrays.
    	if(maze.length == 3 || maze[0].length == 3) { //checks if array has any rows/columns that are 3.
    		if(dir == 0) {
        		int i = 0; // sets position to top left
        		for(int j = 0; j < maze[0].length; j++) {
        			if(maze[i+1][j].equals(MOUSE) || maze[i+1][j].equals(O)) { //checks which border is above the open space/mouse.
            			maze[i][j] = EXIT; //places exit at the top.
            			break;

        			}
        		}
    		}
    		else if(dir == 1) { 
        		int i = maze.length - 1; // sets position to bottom left.
        		for(int j = 0; j < maze[0].length; j++) { 
        			if(maze[i-1][j].equals(MOUSE)|| maze[i-1][j].equals(O)) { //checks which border is below the open space/mouse.
            			maze[i][j] = EXIT; //places exit at the bottom.
            			break;

        			}
        		}
    		}
    		else if(dir == 2) { 
        		int j = 0; //sets position to top left
        		for(int i = 0; i < maze.length; i++) {
        			if(maze[i][j+1].equals(MOUSE)|| maze[i][j+1].equals(O)) { //checks which border is to the left of the open space/mouse.
            			maze[i][j] = EXIT; //places exit at the left.
            			break;

        			}
        		}
    		}else {
        		int j = maze[0].length-1; //sets position to the top right
        		for(int i = 0; i < maze.length; i++) {
        			if(maze[i][j-1].equals(MOUSE) || maze[i][j-1].equals(O)) { //checks which border is to the right of the open space/mouse
            			maze[i][j] = EXIT; //places exit at the right
            			break;
        			}
        		}
    		}
    		
    	}
    	else {
	    	if(dir == 0) {//upper border
	    		int i = 0; //sets position top left.
	    		for(int j = 0; j < maze[0].length; j++) {
	    			if(maze[i+1][j].equals(O)) {  //checks if there is open space below
	    				int[] coord = {i, j}; //create array of coordinate dimensions.
	    				exits.add(coord); // add coordinate to array list
	    			}
	    		}//end of for loop
	    	}
	    	else if(dir == 1) {//downward border
	    		int i = maze.length-1; //sets position to bottom left
	    		for(int j = 0; j < maze[0].length; j++) {
	    			if(maze[i-1][j].equals(O)) { //checks if there is open space above

	    				int[] coord = {i, j}; //create array of coordinate dimensions
	    				exits.add(coord); //add coordinate to array list
	    			}
	    			
	    		}//end of for loop
	    	}
	    	else if(dir == 2) { //left border
	    		int j = 0; //sets position to top left
	    		for(int i = 0; i < maze.length; i++) { 
	    			if(maze[i][j+1].equals(O)) { //checks if there is open space to the right

	    				int[] coord = {i, j}; //create array of coordinate dimensions
	    				exits.add(coord); //add coordinate to array list.
	    			}
	    			
	    		}//end of for loop
	    	}
	    	else { //right border
	    		int j = maze[0].length-1; //set position to top right.
	    		for(int i = 0; i < maze.length; i++) {
	    			if(maze[i][j-1].equals(O)) {//checks if there is open space to the left
	    				int[] coord = {i, j}; //create array of coordinate dimensions
	    				exits.add(coord); //add coordinate to array list.
	    			} //
	    			
	    		}//end of for loop
	    	}
	    	
	    	int rand = (int) (Math.random() * exits.size()); //picks one of the random exits/possible paths.
	    	int [] coord = exits.get(rand); //get coordinate of randomly selected coordinate.
	    	maze[coord[0]][coord[1]] = EXIT; //assign exit to coordinate.
    	}	
     }
    
    /**
     * Removes any of the components from the maze and then sets the layout in terms of the rows and cols
     * of the maze of the user entered and creates individual labels and adds it to the panel during each 
     * cell. 
     * 
     * 
     * 
     * @param maze[][] - the maze array that holds all letters
     * 
     * 
     * 
     * JLabel label - a label of each cell to add to the mazePanel
     * mazePanel - the panel where the maze exists, and in this method the panel's contents' are 
     * 			   removed first, and then the new labels are added, to ensure that any previous maze
     * 			   isn't added to the current maze
     * 
     * 
     * @return void
     * 
     */
    
    public static void addMazePanel(String[][] maze) {
    	//remove the contents of maze panel if there were any before
    	mazePanel.removeAll();
    	
    	//set the maze panel's layout to the new cols/rows
        mazePanel.setLayout(new GridLayout(rows, cols));
        
        
        //loop through the maze
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
            	//create a label for each cell/letter
            	JLabel label = new JLabel(maze[i][j]);
            	label.setFont(new Font("Arial",Font.BOLD,15)); //set the font of the cell/letter
            	
            	//if mouse at this coordinate, set color to green
            	if(maze[i][j].equals(MOUSE)) {
            		label.setBackground(Color.GREEN); 
            		label.setOpaque(true);
            	}
            	//if exit exists at this coordinate set color to red
            	else if(maze[i][j].equals(EXIT)) {
            		label.setBackground(Color.RED); 
            		label.setOpaque(true);
            	}
            	//if a path exists at this coordinate set text to open space, and then change color to yellow
            	else if(maze[i][j].equals("/")) {
            		label.setText(O);
            		label.setBackground(Color.YELLOW);
            		label.setOpaque(true);
            	}
            	//if border exists, change color of the cell to gray
            	else if(maze[i][j].equals(B)){
            		label.setBackground(Color.GRAY); 
            		label.setOpaque(true);
            	}
            	else{
            		//if open space just set opaque to true
            		label.setOpaque(true);
            	}
            	//setting the borders and vertical/horizontal alignments
            	label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            	label.setHorizontalAlignment(JLabel.CENTER);
            	label.setVerticalAlignment(JLabel.CENTER);
            	
            	//add the label to the maze panel
            	mazePanel.add(label);
               
            }
        }



    }
    /**
     * Gets the maze and starting position of the mouse and goes through each path possible at every
     * split by recursing and if statements and at each move, the coordinates are added to a 
     * temporary maze array list, and when the computer finds a path it compares it with the shortestPath
     * array list, which if the current path is shorter than the shortestPath, it sends the current path's
     * coordinates to the shortestPath array list
     * 
     * 
     * 
     * @param maze - 2D array which holds the complete maze
     * @param visited - an array to track which cells have been visited so the method doesn't run into errors
     * @param row - what row the method is currently on
     * @param col - what col the method is currently on
     * @param path - an array list that holds each move the user takes, and removes the move when recursing back
     * @param shortestPath - an array list that holds the coordinates of the shortest path
     * 
     * 
     * 
     * The method, checks what moves are possible, then adds the coordinates of the possible move
     * and then calls this method again recursively, but this time with the new coordinates, which moves
     * and when the method call ends, it removes that move's coordinates. Which means if the recursive
     * call, ended up with a path, it checks if this path's coordinates is smaller than the shortestPath's
     * path size, and if it is, it copies it. But we also have to check if this is the first path found
     * 
     * @return false all the time, but we don't care about the return, we care about whether the 
     * shortestPath array list is empty or not, if it is empty, that means, there is no valid path
     * But returns true, only if path found, which is arbitrary.
     * 
     */
 // Method to find the shortest path from the starting position to the end position in the maze
    public static boolean findShortestPath(String[][] maze, boolean[][] visited, int row, int col,
                                             ArrayList<Integer> path, ArrayList<Integer> shortestPath) {
    	
        if (maze[row][col].equals(EXIT)) { // If the current cell is the end cell
            if (shortestPath.isEmpty() || path.size() < shortestPath.size()) { // If this is the first path we've found or it's shorter than the previous shortest path, save it
                shortestPath.clear(); //clear the previous shortestPath, since we found a shorter path
                for(int i = 0; i < path.size(); i++){ // Add the current path to the list of shortest path
                    shortestPath.add(path.get(i)); //add the coordinates
                }
            }
            return true; // Return true if exit found, doesn't matter though
        }

        // Mark the current position as visited, so that the computer doesn't think this is a valid move
        visited[row][col] = true;
        
        //if current path that is being recursed is bigger than the shortest path, then stop recursing now
        if(!shortestPath.isEmpty() && path.size() > shortestPath.size()) {
            visited[row][col] = false; //set the visited position to false, since we may come by this position later
            return false; // No path to the end cell found
        }
        else {
	        // Check the neighboring positions for a valid move
	        if(!(maze[row-1][col].equals(B)) && !visited[row-1][col]){ // Check if upward movement possible
	            path.add(row-1); //add the possible path to the temporary path array list, row coordinate
	            path.add(col); //col coordinate
	            //send these coordinates to the same method
	            findShortestPath(maze, visited, row-1, col, path, shortestPath); // Recursive call to find shortest path from the new position
	            path.remove(path.size() - 1); // Backtrack by removing last position added, row
	            path.remove(path.size() - 1); //remove column
	        }
	        if(!(maze[row+1][col].equals(B)) && !visited[row+1][col]){ // Check for downward movement
	            path.add(row+1); //add row coordinate of allowed move
	            path.add(col); //add col coordinate
	            findShortestPath(maze, visited, row+1, col, path, shortestPath); //recurse call with this new coordanite
	            path.remove(path.size() - 1); //when backtracking, remove the path coordinates
	            path.remove(path.size() - 1);
	        }
	        if(!(maze[row][col+1].equals(B)) && !visited[row][col+1]){ // Check for rightward movement
	            path.add(row); //add row coordinate of path
	            path.add(col+1); //add col coordinate of the move
	            findShortestPath(maze, visited, row, col+1, path, shortestPath); //recurse call with this move
	            path.remove(path.size() - 1); //remove coordinate when backtracking
	            path.remove(path.size() - 1);
	        }
	        if(!(maze[row][col-1].equals(B)) && !visited[row][col-1]){ // Check for leftward movement
	            path.add(row);
	            path.add(col-1);
	            findShortestPath(maze, visited, row, col-1, path, shortestPath);
	            path.remove(path.size() - 1);
	            path.remove(path.size() - 1);
	        }
        }
        // Mark the current position as unvisited when backtracking
        visited[row][col] = false;
        return false; // No path to the end cell found, so return false, but doesn't matter
    }

    // Method to find the starting position of the mouse
    public static void findMouse(String[][] maze, int[] start) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].equals(MOUSE)) {
                    start[0] = i; // Assign row index of starting position
                    start[1] = j; // Assign column index of starting position
                }
            }
        }
    }

    /**

    This method searches through the maze array to locate the exit position.

    @param - maze is a 2D array representing the maze.

    @param - end is an integer array that stores the coordinates of the exit position.
    
    */

	public static void findExit(String[][] maze, int[] end) {
	    for (int i = 0; i < maze.length; i++) {
	        for (int j = 0; j < maze[0].length; j++) {
	            if (maze[i][j].equals(EXIT)) { 
	                end[0] = i;
	                end[1] = j;
	            }
	        }
	    }
	}//end of findExit method.
	
	
	
	/**

	This method takes a File object as input and extracts the legend information from it.

	The extracted information is stored in the static variables of this class.

	@param file is a File object representing the legend file.

	@throws FileNotFoundException if the file is not found in the specified path.
	*/
	public static boolean getLegendInput(File file) throws FileNotFoundException{

		try {
			Scanner mySc = new Scanner(file);
			
			rows = mySc.nextInt(); // Extract the number of rows from the legend file.
			cols = mySc.nextInt(); // Extract the number of columns from the legend file.
			B = mySc.next(); // Extract the string representation of block from the legend file.
			O = mySc.next(); // Extract the string representation of open from the legend file.
			MOUSE = mySc.next(); // Extract the string representation of start from the legend file.
			EXIT = mySc.next(); // Extract the string representation of end from the legend file.
			mySc.close(); // Close the scanner object.
			if(rows >= 3 && rows <= 30 && cols >= 3 && cols <= 30) {
				return false;
			}
			else {
				dataValidation.setVisible(true);
	        	dataValidation.setText("Please enter the allowed number of rows/cols. The Minimum Amount of Rows/Cols is: 3. And the Maximum Amount is: 30");
				return true; //return false if there isn't an error with the input of the legend
			}
			
		}
		catch(Exception e) {
			dataValidation.setVisible(true);
        	dataValidation.setText("Input of the Legend is incorrect. Please follow the proper data types and inputs of the legend: 1. rows 2. cols 3. border 4. open space 5. mouse 6. exit");
			return true; //return true if there is an error regarding the input of the legend, of data type or not enough data
		}

		
	}//end of getLegendInput method

	/**
	
	This method takes a File object, a temporary maze array and a maze array as input and extracts the maze information from it.

	The extracted information is stored in the maze array and the temporary maze array.

	@param file is a File object representing the maze file.

	@param tempMaze is a temporary 2D array used to store the original maze before marking the path.

	@param maze is a 2D array representing the maze.

	@throws FileNotFoundException if the file is not found in the specified path.
	
	 */
	public static boolean getMatrixInput(File file, String[][] maze) throws FileNotFoundException{
			
			boolean mouseError = true;
			boolean exitError= true;
			boolean openSpaceError = false; 
			
			System.out.println("Rows" + rows);
			System.out.println("Cols" + cols);
			Scanner mySc = new Scanner(file);
			try {
				
				for(int i = 0; i < 6; i++) {
						mySc.nextLine(); // Skip the first 6 lines of the file as they contain the legend information.
				}
				for(int i = 0; i < rows; i++){
					String temp = mySc.nextLine(); // Extract the current row of maze from the file.
	
					 for(int j = 0; j < cols; j++){
					     maze[i][j] = Character.toString(temp.charAt(j)); // Store the current element in maze array.
					     
					     if(maze[i][j].equals(EXIT)) {
					    	 if(i == rows-1 || i == 0 || j == cols-1 || j == 0) {
						    	 exitError = false;
					    	 }
					     }
					     else if(maze[i][j].equals(MOUSE)){
					    	 if(!(i == rows-1 || i == 0 || j == cols-1 || j == 0)) {
						    	 mouseError = false;
					    	 }
					    	 	
					     }
					     else if(maze[i][j].equals(O)){
					    	 if(i == rows-1 || i == 0 || j == cols-1 || j == 0) {
						    	 openSpaceError = true;
					    	 }
					     }
					     
					     
					 }
					  
				}
				mySc.close();// Close the scanner object.
			}catch(Exception e) {

				dataValidation.setVisible(true);
				dataValidation.setText("Enter the maze accurately with all the given aspects of the maze in the legend. Enter the cols/rows according to the maze.");
				return true;
			}
			
			
			if(mouseError || exitError || openSpaceError) {
				
				dataValidation.setVisible(true);
				dataValidation.setText("Enter the maze accurately. All aspects of the legend need to exist. No open spaces on border, exit only on border and no mouse on border.");
				return true;
				
				
			}
			return false;
			
	}

	/**
	* Forms the maze according to the users needs. Either creates
	* a random maze or a maze with a guaranteed path. Also transfers the 
	* String data to the GUI.
	* @param 
	* int input - lets program know what maze user wants.
	* int col - number of columns user wanted.
	* int rows - number of rows user wanted.
	* JLabel[][] mazeLabels - 2D array of JLabels that are then applied to the grid or frame.
	* @return 
	* returns String[][] maze, an array that holds on data on where the exit, mouse, borders,
	* and possibly paths are.
	*/
	
	public static String[][] generateMaze(int[] start, int numRows, int numCols) {
	    String[][] maze = new String[numRows][numCols];
	    System.out.println("# of rows: " + numRows);
	    System.out.println("# of cols: " + numCols);
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numCols; j++) {
	        	if(i == 0) {
	        		maze[i][j] = BORD; 
	        	}
	        	else if(i == numRows - 1) {
	        		maze[i][j] = BORD;
	        	}
	        	else if(j == 0) {
	        		maze[i][j] = BORD;
	        	}
	        	else if(j == numCols - 1) {
	        		maze[i][j] = BORD;
	        	}
	        	else {
	        		maze[i][j] = B;
	        	}
	        	
	        }
	    }

	    // Mark the starting position as a valid path
	    maze[start[0]][start[1]] = O;

	    // Initialize the list of possible path extensions with the starting position
	    ArrayList<int[]> possibleExtensions = new ArrayList<>();
	    possibleExtensions.add(start);

	    // Call the recursive helper method to generate the maze
	    generateMazeHelper(maze, possibleExtensions);

	    // Mark the exit position as a valid path
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numCols; j++) {
	        	if(maze[i][j].equals(BORD)) {
	        		maze[i][j] = B;
	        	}
	        }
	    }

	    return maze;
	}

	public static void generateMazeHelper(String[][] maze, ArrayList<int[]> possibleExtensions) {
	    if (possibleExtensions.isEmpty()) {
	        return;
	    }

	    // Pick a random element from the list of possible path extensions
	    int index = (int) (Math.random() * possibleExtensions.size());
	    int[] current = possibleExtensions.remove(index);
	    System.out.println(current[0] + " " + current[1] + "ss");
	    
	    // Check if adding the current point will cause the maze to intersect itself
	    if (willIntersect(maze, current)) {
	    	System.out.println("will intersect");
	        generateMazeHelper(maze, possibleExtensions); // Backtrack and try a different path extension
	        return;
	    }
	    
	    // Add the current point to the maze and mark it as visited
	    maze[current[0]][current[1]] = O;

	    // Add all unvisited neighbors of the current point to the list of possible path extensions
	    int row = current[0];
	    int col = current[1];
	    if (row > 0 && maze[row - 1][col].equals(B)) { //doesn't link with the border or an open space
	    	int[] coord = {row - 1, col};
	    	possibleExtensions.add(coord);
	    }
	    if (row < maze.length - 1 && maze[row + 1][col].equals(B)) {
	    	int[] coord = {row + 1, col};
	    	possibleExtensions.add(coord);
	    }
	    if (col > 0 && maze[row][col - 1].equals(B)) {
	    	int[] coord = {row, col - 1};
	        possibleExtensions.add(coord);
	    }
	    if (col < maze[0].length - 1 && maze[row][col + 1].equals(B)) {
	    	int[] coord = {row, col + 1};
	    	possibleExtensions.add(coord);
	    }

	    generateMazeHelper(maze, possibleExtensions); // Continue recursively generating the maze
	}

	public static boolean willIntersect(String[][] maze, int[] point) {
	    int row = point[0];
	    int col = point[1];
	    int numWalls = 0;
	    System.out.println(row + "rows");
	    System.out.println(col + "cols");
	    if (row > 0 && maze[row - 1][col].equals(O)) {
	    	
	        numWalls++;
	    }
	    if (row < maze.length - 1 && maze[row + 1][col].equals(O)) {
	    	
	        numWalls++;
	    }
	    if (col > 0 && maze[row][col - 1].equals(O)) {

	        numWalls++;
	    }
	    if (col < maze[0].length - 1 && maze[row][col + 1].equals(O)) {

	        numWalls++;
	    }
	    return numWalls >= 2; // A point will intersect the maze if it has
	}
	
	public static boolean checkIntersection(String[][] maze, int row, int col) {
		boolean intersection = false;
		if (row > 0 && maze[row - 1][col].equals(O)) {
	        intersection = true;
	    }
	    if (row < maze.length - 1 && maze[row + 1][col].equals(O)) {
	        intersection = true;
	    }
	    if (col > 0 && maze[row][col - 1].equals(O)) {
	        intersection = true;
	    }
	    if (col < maze[0].length - 1 && maze[row][col + 1].equals(O)) {
	    	intersection = true;
	    }
	    return intersection;
	}
	
	
	public static ArrayList<int[]> getUnvisitedNeighbors(String[][] maze, int row, int col) {
	    ArrayList<int[]> neighbors = new ArrayList<>();
	    if (row > 0 && maze[row - 1][col].equals(B)) {
	    	int[] neighbor = {row - 1, col};
	    	neighbors.add(neighbor);
	    }
	    if (row < maze.length - 1 && maze[row + 1][col].equals(B)) {
	    	int[] neighbor = {row+1, col};
	    	neighbors.add(neighbor);
	    }
	    if (col > 0 && maze[row][col - 1].equals(B)) {
	    	int[] neighbor = {row, col-1};
	    	neighbors.add(neighbor);
	    }
	    if (col < maze[0].length - 1 && maze[row][col+1].equals(B)){
	    	int[] neighbor = {row, col+1};
	    	neighbors.add(neighbor);
	    }
	    return neighbors;
	}
	
	public static String[][] formMaze(int input, int col, int row){
//		mazePanel.setLayout(new GridLayout(rows, col)); //set mazePanel layout to match rows and columns.
//		mazePanel.removeAll(); //clear panel incase there are buttons from previous executions.
		rows = row;
		cols = col;
		String maze[][] = new String[rows][cols]; //initialize 2dArray that holds maze.
		
		
		for(int i = 0; i < maze.length; i++){// nested for loop to randomize maze.
			for(int j = 0; j < maze[i].length; j++){
					
					if(i == 0 || j == 0 || i == rows-1 || j == col-1 ){//check if maze border
						 
						 maze[i][j] = B; //assign "B" for border
						 
					}
					else{//if not in border
						
						if((int)(Math.random()*2) == 0){ //randomize between two number.
							
							maze[i][j] = B; //assign "B" for wall/border.
							
						}
						else{
							
							maze[i][j] = O;//assign open space
							
						}
					}
			    	   

			}
		}
		int positionYfinal = (int)((Math.random()*(col-2))+1); // randomly generated y dimension of exit.
		int positionXfinal = randGen.nextInt(2)* (rows-1);// randomly generated x dimension of exit.
		maze[positionXfinal][positionYfinal] = EXIT; // set coordinate of exit.

		int positionX = (int) ((Math.random()*(rows-2))+1); //randomly generated x dimension of mouse.
		int positionY = (int) ((Math.random()*(col-2))+1); //randomly generated y dimension of mouse.
		maze[positionX][positionY] = MOUSE; //set coordinate of mouse.
		return maze;
	}//end of formMaze method
	


}









