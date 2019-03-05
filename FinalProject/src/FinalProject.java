/*
 * Brandon Watts
 * Final Project
 * 07/27/18
 * ITC 2060
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FinalProject extends Application{
	//Puzzle Game
	private Image image= new Image("icesid.jpg");
	public static int TILE_ROW_COUNT = 5;
	public static int TILE_COLUMN_COUNT = 5;
	public static double TILE_SIZE=96;
	public static int SHUFFLE_COUNT = TILE_ROW_COUNT*TILE_COLUMN_COUNT;
	
	//Puzzle Game & window size
	private static double SCENE_WIDTH = 800;
	private static double SCENE_HEIGHT = 600;
	
	//More Puzzle Game
	public static double offsetX =(SCENE_WIDTH - TILE_ROW_COUNT * TILE_SIZE) /2;
	public static double offsetY = (SCENE_HEIGHT - TILE_COLUMN_COUNT * TILE_SIZE) /2;
	
	List<puzzleCell> cells = new ArrayList<>();
	
	//Tic tac toe
	private char whoseTurn = 'X';
	private ticTacCell[][] tcell =  new ticTacCell[3][3];
	private Label lblStatus = new Label("X's turn to play");
	
	//Loan Calculator
	private TextField tfAnnualInterestRate = new TextField();
	private TextField tfNumberOfYears = new TextField();
	private TextField tfLoanAmount = new TextField();
	private TextField tfMonthlyPayment = new TextField();
	private TextField tfTotalPayment = new TextField();
	private Button btCalculate = new Button("Calculate");
	
	//Text editor
	CheckBox checkEditable = new CheckBox("Editable");
	CheckBox checkWrap = new CheckBox("Wrap");
	
	//Main program and text editor
	BorderPane root = new BorderPane();
	TextArea textArea = new TextArea();
	ScrollPane scrollPane= new ScrollPane(textArea);
	private File file;
	
	
	@Override
	public void start(Stage stage){
		
		textArea.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
		textArea.setWrapText(true);
		//create menu bar
		MenuBar menuBar = new MenuBar();
		
		
		Menu fileMenu = new Menu("File");
		
		//create MenuItems
		SeparatorMenuItem sep = new SeparatorMenuItem();
		SeparatorMenuItem sep2 = new SeparatorMenuItem();
		SeparatorMenuItem sep3 = new SeparatorMenuItem();
		SeparatorMenuItem sep4 = new SeparatorMenuItem();
		
		//New Item
		MenuItem newFileItem = new MenuItem ("New File");
		newFileItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent event){
				//clear text area
				root.setCenter(scrollPane);
				textArea.clear();
				textArea.setEditable(true);
				textArea.setWrapText(true);
				root.setBottom(null);

			}
			
		});
		//Open Item
		MenuItem openFileItem = new MenuItem ("Open File");
		openFileItem.setOnAction(new EventHandler<ActionEvent>() {
			
			private Scanner input;

			@Override
			public void handle (ActionEvent event){
				//clear text area
				textArea.clear();
				textArea.setEditable(true);
				textArea.setWrapText(true);
				root.setCenter(scrollPane);
			    root.setBottom(null);

				
				//open new File
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new 
						FileChooser.ExtensionFilter("TXT files(*.txt)","*.txt");
				fileChooser.getExtensionFilters().add(extFilter);
				file = fileChooser.showOpenDialog(stage);
				
				try {
					//create buffered stream
					if (file != null){
						input = new Scanner(file);
						//read a line and append line to text area
						while (input.hasNext()) {
							textArea.appendText(input.nextLine() + '\n');
						}
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println("File not found: "+ file.getName());
				}
				
			}
		
		});
		
		//Save Item
		MenuItem saveFileItem = new MenuItem ("Save");
		saveFileItem.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle (ActionEvent event){
				try{
					PrintWriter outStream = new PrintWriter(file);
					
					outStream.write(textArea.getText());
					outStream.close();
				} catch (FileNotFoundException el) {
					el.printStackTrace();
				}
			}
		});
		
		//Save As
		MenuItem saveAsItem = new MenuItem ("Save As");
			saveAsItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					FileChooser saveAsChooser = new FileChooser();
					FileChooser.ExtensionFilter saveAsFilter = new 	
							FileChooser.ExtensionFilter("TXT files(*.txt)","*.txt");
					 			saveAsChooser.getExtensionFilters().add(saveAsFilter);
			            File file = saveAsChooser.showSaveDialog(stage);
			           
			            try{
			                PrintWriter out = new PrintWriter(file);
			               
			                out.write(textArea.getText());
			                out.close();
			            } catch (FileNotFoundException ex){
			                ex.printStackTrace();
			            }
				}
			 });
		//exit Item
		MenuItem exitItem = new MenuItem ("Exit");
		exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+x"));
		//when user clicks exit
		exitItem.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle (ActionEvent event){
				System.exit(0);
			}
		});
		
		
		//General Application
		Menu genApp = new Menu("General Application");
		//items
		MenuItem loanCalc = new MenuItem("Loan Calculator");
		loanCalc.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent event){
				GridPane loanPane = new GridPane();
				loanPane.setHgap(5);
				loanPane.setVgap(5);
				loanPane.add(new Label("Annual Interest Rate:"), 0, 0);
				loanPane.add(tfAnnualInterestRate, 1, 0);
				loanPane.add(new Label("Number of Years:"), 0, 1);
				loanPane.add(tfNumberOfYears, 1, 1);
				loanPane.add(new Label("Loan Amount:"), 0, 2);
				loanPane.add(tfLoanAmount, 1, 2);
				loanPane.add(new Label("Monthly Payment:"), 0, 3);
				loanPane.add(tfMonthlyPayment, 1, 3);
				loanPane.add(new Label("Total Payment:"), 0, 4);
				loanPane.add(tfTotalPayment, 1, 4);
				loanPane.add(btCalculate, 1, 5);

				// Set properties for UI
				loanPane.setAlignment(Pos.CENTER);
				tfAnnualInterestRate.setAlignment(Pos.BOTTOM_RIGHT);
				tfNumberOfYears.setAlignment(Pos.BOTTOM_RIGHT);
				tfLoanAmount.setAlignment(Pos.BOTTOM_RIGHT);
				tfMonthlyPayment.setAlignment(Pos.BOTTOM_RIGHT);
				tfTotalPayment.setAlignment(Pos.BOTTOM_RIGHT);
				tfMonthlyPayment.setEditable(false);
				tfTotalPayment.setEditable(false);
				GridPane.setHalignment(btCalculate, HPos.RIGHT);

				// Process events
				btCalculate.setOnAction(e -> calculateLoanPayment());
				
				root.setCenter(null);
				root.setCenter(loanPane);
				root.setBottom(null);

			}
				
		});
		
		
		MenuItem textEditor = new MenuItem("Text Editor");
			textEditor.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent event){
		

		// Create a hbox
		HBox boxForChecks = new HBox(5);
		boxForChecks.setAlignment(Pos.CENTER);
		boxForChecks.getChildren().addAll(checkEditable, checkWrap);
		textArea.setEditable(false);
		textArea.setWrapText(false);
		
		textArea.clear();
		root.setCenter(scrollPane);
		root.setBottom(boxForChecks);
		
		checkEditable.setOnAction(e -> {
			textArea.setEditable(checkEditable.isSelected());
		});

		checkWrap.setOnAction(e -> {
			textArea.setWrapText(checkWrap.isSelected());
		});
				}
			});
		
		//Game App
		Menu gameApp = new Menu ("Game Application");
		
		//items
		MenuItem puzzleGame = new MenuItem ("Puzzle Game");
		puzzleGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent event){
				for (int x = 0; x < TILE_ROW_COUNT; x++) {
					for(int y=0; y< TILE_COLUMN_COUNT; y++){
						
						//create tile
						ImageView tile = new ImageView(image);
						Rectangle2D rect = new Rectangle2D(TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
						tile.setViewport(rect);
						
						//consider empty tcell, let it remain empty
						if (x== (TILE_ROW_COUNT -1) && y ==(TILE_COLUMN_COUNT - 1)) {
							tile=null;
						}
						
						cells.add(new puzzleCell(x,y,tile));
						
					}
				}
				//Shuffle Cells
				shuffle();
				
				//Create PlayField
				Pane puzzlePane= new Pane();
				
				//put tiles on playfield, assign event handler
				for (int i=0; i<cells.size(); i++) {
					
					puzzleCell tcell = cells.get(i);
					
					Node imageView = tcell.getImageView();
					
					//consider empty tcell
					if (imageView == null)
						continue;
					
					// click handler: swap tiles, check if puzzle is solved
					imageView.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
						moveCell((Node) mouseEvent.getSource());
					});
					
					//position images on scene
					imageView.relocate(tcell.getLayoutX(), tcell.getLayoutY());
					
					puzzlePane.getChildren().add(tcell.getImageView());
					
					root.setCenter(null);
					root.setCenter(puzzlePane);
				    root.setBottom(null);

				}
			}
		});
		
		MenuItem ticTacToe = new MenuItem ("Tic Tac Toe");
		ticTacToe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent event){
				 GridPane ticTacPane = new GridPane(); 
				    for (int i = 0; i < 3; i++)
				      for (int j = 0; j < 3; j++)
				        ticTacPane.add(tcell[i][j] = new ticTacCell(), j, i);
				    	
				    root.setCenter(null);
				    root.setCenter(ticTacPane);
				    root.setBottom(lblStatus);
			}
		});
		
		//help Menu
		Menu helpMenu = new Menu("Help");
		//about item
		MenuItem updateItem = new MenuItem ("Check For Updates");
		updateItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent event){
				Timeline upToDateTimer = new Timeline(new KeyFrame(Duration.seconds(10)));			
				Timeline updateTimer1 = new Timeline(new KeyFrame(Duration.seconds(1)));
				Timeline updateTimer2 = new Timeline(new KeyFrame(Duration.seconds(1)));
				Timeline updateTimer3 = new Timeline(new KeyFrame(Duration.seconds(1)));
				Timeline updateTimer4 = new Timeline(new KeyFrame(Duration.seconds(1)));
				Timeline updateTimer5 = new Timeline(new KeyFrame(Duration.seconds(1)) );
				Timeline updateTimer6 = new Timeline(new KeyFrame(Duration.seconds(1)) );
				Timeline updateTimer7 = new Timeline(new KeyFrame(Duration.seconds(1)) );
				Timeline updateTimer8 = new Timeline(new KeyFrame(Duration.seconds(1)) );
				Timeline updateTimer9 = new Timeline(new KeyFrame(Duration.seconds(1)) );
				
				Label updateLabel = new Label();
				Button updateOkBt = new Button("Cancel");
				updateLabel.setText("Checking For Updates Please Wait...");
				
				updateTimer1.play();
				upToDateTimer.play();
		
				BorderPane updateLayout = new BorderPane();
				updateLayout.setCenter(updateLabel);
				updateLayout.setBottom(updateOkBt);
				BorderPane.setAlignment(updateOkBt, Pos.CENTER);
		
				Scene secondScene = new Scene (updateLayout, 300,100);
				Stage secondStage = new Stage ();
				secondStage.setTitle("Updating...");
				secondStage.setScene(secondScene);
		
				secondStage.setX(stage.getX() + 250);
				secondStage.setY(stage.getY() + 200);
				secondStage.show();
		
				updateOkBt.setOnAction(e->{
					secondStage.close();
				});
				updateTimer1.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating.  ");
						updateTimer2.play();
					}
				});
				
				updateTimer2.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating.. ");
						updateTimer3.play();
					}
				});
				
				updateTimer3.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating...");
						updateTimer4.play();
					}
				});
				
				updateTimer4.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating.  ");
							updateTimer5.play();
					}
				});
				
				updateTimer5.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating.. ");
							updateTimer6.play();
					}
				});
				
				updateTimer6.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating...");
							updateTimer7.play();
					}
				});
				
				updateTimer7.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating.  ");
							updateTimer8.play();
					}
				});
				
				updateTimer8.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating.. ");
							updateTimer9.play();
					}
				});
				updateTimer9.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						updateLabel.setText("Updating...");
					}
				});
				
				upToDateTimer.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle (ActionEvent event){
						updateLabel.setText("You are up to date!");
						updateOkBt.setText("Continue");
					}
				});
			}
		});
		
		
		
		MenuItem reportBugItem = new MenuItem ("Report A Bug");
		reportBugItem.setOnAction(new EventHandler<ActionEvent>() {
		public void handle (ActionEvent event){
			Button bugOkBt = new Button("OK");
			textArea.setText("If you have noticed a bug please take a screenshot, "
					+ "create a breif description of the problem and email both to: Bkw005@bravemail.uncp.edu");
		    
			textArea.setEditable(false);
			textArea.setWrapText(true);
			
			BorderPane bugLayout = new BorderPane();
			bugLayout.setCenter(textArea);
			bugLayout.setBottom(bugOkBt);
			BorderPane.setAlignment(bugOkBt, Pos.CENTER);
			
			Scene secondScene = new Scene (bugLayout, 500,150);
			Stage secondStage = new Stage ();
			secondStage.setTitle("Report A Bug");
			secondStage.setScene(secondScene);
			
			secondStage.setX(stage.getX() + 150);
			secondStage.setY(stage.getY() + 100);
			secondStage.show();
			
			bugOkBt.setOnAction(e->{
				secondStage.close();
			});
			
		}
	});
		MenuItem aboutItem = new MenuItem ("About");
		aboutItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent event){
				Button aboutBt = new Button("OK");
				textArea.setText("Hello! This program was created so you can open .txt files in a text editor "
					+ "and save them too!" + "\n \n" +"It also has useful applications like a loan calculator and even some fun games!");
				
				textArea.setEditable(false);
				textArea.setWrapText(true);
				
				BorderPane aboutLayout = new BorderPane();
				aboutLayout.setCenter(textArea);
				aboutLayout.setBottom(aboutBt);
				BorderPane.setAlignment(aboutBt, Pos.CENTER);
				
				Scene secondScene = new Scene (aboutLayout, 500,150);
				Stage secondStage = new Stage ();
				secondStage.setTitle("About");
				secondStage.setScene(secondScene);
				
				secondStage.setX(stage.getX() + 150);
				secondStage.setY(stage.getY() + 100);
				secondStage.show();
				
				aboutBt.setOnAction(e->{
					secondStage.close();
				});
				
			}
		});
			
		
		//add menu items
		fileMenu.getItems().addAll(newFileItem, openFileItem, sep , saveFileItem, saveAsItem, sep2, exitItem);
		genApp.getItems().addAll(loanCalc, sep3, textEditor);
		gameApp.getItems().addAll(puzzleGame, sep4,ticTacToe);
		helpMenu.getItems().addAll(updateItem, reportBugItem, aboutItem);
		
		//add menus to menu bar
		menuBar.getMenus().addAll(fileMenu,genApp,gameApp,helpMenu);
		root.setTop(menuBar);
		
		Scene scene = new Scene(root, SCENE_WIDTH,SCENE_HEIGHT);
		stage.setTitle("Final Project");
		stage.setScene(scene);
		stage.show();
	
}
	//Puzzle Game	
	public void shuffle() {
		
		Random rnd = new Random();
		
		for(int i = 0; i < SHUFFLE_COUNT; i++) {
		
			int a = rnd.nextInt(cells.size());
			int b = rnd.nextInt(cells.size());
			
			if (a==b)
				continue;
			
			//skip bottom right tcell swap, we want one empty tcell
			if (cells.get(a).isEmpty() || cells.get(b).isEmpty())
				continue;
			swap(cells.get(a), cells.get(b));
		
		}
	}
	public void swap(puzzleCell cellA, puzzleCell cellB) {
		ImageView tmp = cellA.getImageView();
		cellA.setImageView(cellB.getImageView());
		cellB.setImageView(tmp);
	}
	
	public boolean checkSolved() {
		
		boolean allSolved = true;
		
		for(puzzleCell tcell : cells) {
			
			if (!tcell.isSolved()) {
				allSolved = false;
				break;
			}
		}
		
		System.out.println ("solved: " + allSolved);
		
		return allSolved;
	}
	
	public void moveCell (Node node) {
		//get current tcell using selected node
		puzzleCell currentCell = null;
		for (puzzleCell tmpuzzleCell : cells) {
			if (tmpuzzleCell.getImageView() == node) {
				currentCell = tmpuzzleCell;
				break;
			}
		}
		
		if (currentCell == null)
			return;
		
		//get empty tcell
		puzzleCell emptyCell = null;
		
		for (puzzleCell tmpuzzleCell : cells) {
			if(tmpuzzleCell.isEmpty()) {
				emptyCell = tmpuzzleCell;
				break;
			}
		}
		
		if (emptyCell == null)
			return;
		
		//check if cells are swappable: neighbor distance either x or y must 1 for valid move
		int steps = Math.abs(currentCell.x - emptyCell.x) + Math.abs(currentCell.y - emptyCell.y);
		if (steps !=1)
			return;
		System.out.println("transition: " + currentCell + "->" + emptyCell);
		
		//cells are swappable => create path transition
		Path path = new Path();
		path.getElements().add(new MoveToAbs(currentCell.getImageView(),
				currentCell.getLayoutX(), currentCell.getLayoutY()));
		path.getElements().add(new LineToAbs(currentCell.getImageView(),
				emptyCell.getLayoutX(), emptyCell.getLayoutY()));
		
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(100));
		pathTransition.setNode(currentCell.getImageView());
		pathTransition.setPath(path);
		pathTransition.setOrientation(PathTransition.OrientationType.NONE);
		pathTransition.setCycleCount(1);
		pathTransition.setAutoReverse(false);
		
		final puzzleCell cellA = currentCell;
		final puzzleCell cellB = emptyCell;
		pathTransition.setOnFinished(actionEvent -> {
			
			swap(cellA, cellB);
			
			checkSolved();
			
		});
		
		pathTransition.play();
	}
		
	private static class puzzleCell {
		int x;
		int y;
		
		ImageView initialImageView;
		ImageView currentImageView;
		
		public puzzleCell (int x, int y, ImageView initialImageView) {
			super();
			this.x = x;
			this.y = y;
			this.initialImageView = initialImageView;
			this.currentImageView = initialImageView;
		}
		
		public double getLayoutX() {
			return x * TILE_SIZE + offsetX;
		}
		
		public double getLayoutY() {
			return y * TILE_SIZE + offsetY;
		}
		public ImageView getImageView() {
			return currentImageView;
		}
		
		public void setImageView (ImageView imageView) {
			this.currentImageView = imageView;
		}
		public boolean isEmpty() {
			return currentImageView == null;
		}
		
		public boolean isSolved() {
			return this.initialImageView == currentImageView;
		}
		public String toString() {
			return "[" + x + "," + y + "]"; 		
		}
		
	}
	// absolute (layoutX/Y) transitions using the pathtransition for moveto
	public static class MoveToAbs extends MoveTo {
		
		public MoveToAbs(Node node) {
			super (node.getLayoutBounds().getWidth() / 2,
					node.getLayoutBounds().getHeight() / 2);
		}
		
		public MoveToAbs (Node node, double x, double y) {
			super(x - node.getLayoutX()+ node.getLayoutBounds().getWidth()/2,
					y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
		}
	}
	// absolute (layout X/Y) Transitions using the pathTransition for LineTo
	public static class LineToAbs extends LineTo {
		
			public LineToAbs (Node node, double x, double y) {
				super(x - node.getLayoutX()+ node.getLayoutBounds().getWidth()/2,
						y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
			}
		}
	
	//Tic Tac Toe
	/** Determine if the tcell are all occupied */
	public boolean isFull() {
	    for (int i = 0; i < 3; i++)
	      for (int j = 0; j < 3; j++)
	        if (tcell[i][j].getToken() == ' ')
	          return false;

	    return true;
	  }

	/** Determine if the player with the specified token wins */
	public boolean isWon(char token) {
	    for (int i = 0; i < 3; i++)
	      if (tcell[i][0].getToken() == token
	          && tcell[i][1].getToken() == token
	          && tcell[i][2].getToken() == token) {
	        return true;
	      }

	    for (int j = 0; j < 3; j++)
	      if (tcell[0][j].getToken() ==  token
	          && tcell[1][j].getToken() == token
	          && tcell[2][j].getToken() == token) {
	        return true;
	      }

	    if (tcell[0][0].getToken() == token 
	        && tcell[1][1].getToken() == token        
	        && tcell[2][2].getToken() == token) {
	      return true;
	    }

	    if (tcell[0][2].getToken() == token
	        && tcell[1][1].getToken() == token
	        && tcell[2][0].getToken() == token) {
	      return true;
	    }

	    return false;
	  }

	// An inner class for a tcell
	public class ticTacCell extends Pane {
	    // Token used for this tcell
	    private char token = ' ';

	    public ticTacCell() {
	      setStyle("-fx-border-color: black"); 
	      this.setPrefSize(2000, 2000);
	      this.setOnMouseClicked(e -> handleMouseClick());
	    }

	    /** Return token */
	    public char getToken() {
	      return token;
	    }

	    /** Set a new token */
	    public void setToken(char c) {
	      token = c;
	      
	      if (token == 'X') {
	        Line line1 = new Line(10, 10, 
	          this.getWidth() - 10, this.getHeight() - 10);
	        line1.endXProperty().bind(this.widthProperty().subtract(10));
	        line1.endYProperty().bind(this.heightProperty().subtract(10));
	        Line line2 = new Line(10, this.getHeight() - 10, 
	          this.getWidth() - 10, 10);
	        line2.startYProperty().bind(
	          this.heightProperty().subtract(10));
	        line2.endXProperty().bind(this.widthProperty().subtract(10));
	        
	        // Add the lines to the pane
	        this.getChildren().addAll(line1, line2); 
	      }
	      else if (token == 'O') {
	        Ellipse ellipse = new Ellipse(this.getWidth() / 2, 
	          this.getHeight() / 2, this.getWidth() / 2 - 10, 
	          this.getHeight() / 2 - 10);
	        ellipse.centerXProperty().bind(
	          this.widthProperty().divide(2));
	        ellipse.centerYProperty().bind(
	            this.heightProperty().divide(2));
	        ellipse.radiusXProperty().bind(
	            this.widthProperty().divide(2).subtract(10));        
	        ellipse.radiusYProperty().bind(
	            this.heightProperty().divide(2).subtract(10));   
	        ellipse.setStroke(Color.BLACK);
	        ellipse.setFill(Color.WHITE);
	        
	        getChildren().add(ellipse); // Add the ellipse to the pane
	      }
	    }

	    /* Handle a mouse click event */
	    private void handleMouseClick() {
	      // If tcell is empty and game is not over
	      if (token == ' ' && whoseTurn != ' ') {
	        setToken(whoseTurn); // Set token in the tcell

	        // Check game status
	        if (isWon(whoseTurn)) {
	          lblStatus.setText(whoseTurn + " won! The game is over");
	          whoseTurn = ' '; // Game is over
	        }
	        else if (isFull()) {
	          lblStatus.setText("Draw! The game is over");
	          whoseTurn = ' '; // Game is over
	        }
	        else {
	          // Change the turn
	          whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
	          // Display whose turn
	          lblStatus.setText(whoseTurn + "'s turn");
	        }
	      }
	    }
	  }
	  
	//Loan Calculator
	private void calculateLoanPayment() {
		// Get values from text fields
		double interest =
		Double.parseDouble(tfAnnualInterestRate.getText());
		int year = Integer.parseInt(tfNumberOfYears.getText());
		double loanAmount =
		Double.parseDouble(tfLoanAmount.getText());

		// Create a loan object. Loan defined in Listing 10.2
		Loan loan = new Loan(interest, year, loanAmount);

		// Display monthly payment and total payment
		tfMonthlyPayment.setText(String.format("$%.2f",
		loan.getMonthlyPayment()));
		tfTotalPayment.setText(String.format("$%.2f",
		loan.getTotalPayment()));
	}
	
	public class Loan {
		  private double annualInterestRate;
		  private int numberOfYears;
		  private double loanAmount;
		  private java.util.Date loanDate;

		  /** Default constructor */
		  public Loan() {
		    this(2.5, 1, 1000);
		  }

		  /** Construct a loan with specified annual interest rate,
		      number of years and loan amount 
		    */
		  public Loan(double annualInterestRate, int numberOfYears,
		      double loanAmount) {
		    this.annualInterestRate = annualInterestRate;
		    this.numberOfYears = numberOfYears;
		    this.loanAmount = loanAmount;
		    loanDate = new java.util.Date();
		  }

		  /** Return annualInterestRate */
		  public double getAnnualInterestRate() {
		    return annualInterestRate;
		  }

		  /** Set a new annualInterestRate */
		  public void setAnnualInterestRate(double annualInterestRate) {
		    this.annualInterestRate = annualInterestRate;
		  }

		  /** Return numberOfYears */
		  public int getNumberOfYears() {
		    return numberOfYears;
		  }

		  /** Set a new numberOfYears */
		  public void setNumberOfYears(int numberOfYears) {
		    this.numberOfYears = numberOfYears;
		  }

		  /** Return loanAmount */
		  public double getLoanAmount() {
		    return loanAmount;
		  }

		  /** Set a newloanAmount */
		  public void setLoanAmount(double loanAmount) {
		    this.loanAmount = loanAmount;
		  }

		  /** Find monthly payment */
		  public double getMonthlyPayment() {
		    double monthlyInterestRate = annualInterestRate / 1200;
		    double monthlyPayment = loanAmount * monthlyInterestRate / (1 -
		      (Math.pow(1 / (1 + monthlyInterestRate), numberOfYears * 12)));
		    return monthlyPayment;    
		  }

		  /** Find total payment */
		  public double getTotalPayment() {
		    double totalPayment = getMonthlyPayment() * numberOfYears * 12;
		    return totalPayment;    
		  }

		  /** Return loan date */
		  public java.util.Date getLoanDate() {
		    return loanDate;
		  }
		}

	
	
	//launch	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
