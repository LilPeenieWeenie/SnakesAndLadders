package slgame;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.collections.*;

import java.io.*;
import java.util.*;

import javafx.scene.control.Alert.AlertType;

public class SLXYChart extends Application {

   private BorderPane root = new BorderPane();

   private final Button runBtn = new Button("Run");
   private final Button clearBtn = new Button("Clear Output");
   private final TextArea display = new TextArea();
   private final StringBuilder output = new StringBuilder(128);
   private String appTitle;
   private int outputCount;

   private Parent createContent() {
      root.setPrefSize(990,600);
      root.setPadding(new Insets(10));
   
      display.setPrefColumnCount(50);
      display.setWrapText(true);
      display.setStyle( "-fx-font-family: monospace;" +
                       "-fx-font-weight: bold;" +
                       "-fx-font-size: 18;" );
   
      runBtn.setOnAction(e -> { run(); });
      clearBtn.setOnAction(e -> { clearOutput(); });
   
      HBox topBox = new HBox(10);
      topBox.setPadding(new Insets(5));
      topBox.getChildren().addAll(runBtn, clearBtn);
   
      root.setTop(topBox);
      root.setCenter(display);
      return root;
   } // end createContent()

   @Override
   public void start(Stage stage) {
      setup();
      stage.setTitle(appTitle);
      Scene scene = new Scene(createContent());
      stage.setScene(scene);
      stage.show();
   } // end start()

   private void setup() {
      appTitle = "App";
   
   } // end setup

   public void run() {
      outputln("                            Snake and Ladders Game");
      outputln("                         Possible Player Positions(x/y)\n");
      outputln("   100     99      98      97      96      95      94      93      92      91");
      outputln("|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|");
      for(int y = 30; y <= 570; y+=60) {
         for(int x = 30; x <= 570; x+=60) {
            if(x < 570)
               output(String.format("%s%3d/%3d", "|", x, y));
            else
               output(String.format("%s%3d/%3d%s","|", x, y,"|"));
         }
         outputln("\n|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|");
      }
      outputln("    1       2       3       4       5       6       7       8       9       10");
   
   } // end run

   // helper methods can go here

   private Optional<String> getDialogText(String prompt) {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Dialog");
      dialog.setHeaderText(prompt);
      Optional<String> text = dialog.showAndWait();
      return text;
   }

   public String input(String prompt) {
      Optional<String> text = getDialogText(prompt);
      return text.orElse("");
   }
   public String[] getLinesFromFile(String fileName) {
      ArrayList<String> lines = readListFromFile(fileName);
      return lines.toArray(new String[lines.size()]);
   }
  
   public ArrayList<String> readListFromFile(String fileName) {
      ArrayList<String> linesFromFile = new ArrayList<String>();
      Scanner reader = null;
      try {
         reader = new Scanner(new File(fileName));
         while(reader.hasNextLine()) {
            linesFromFile.add(reader.nextLine());
         }
      } 
      catch (IOException e) { showMessage(e.getMessage()); }
      if(reader != null) reader.close();
      print("Lines read: " + linesFromFile.size());
      return linesFromFile;   
   }
       
   String getFileAsString(String filename) throws FileNotFoundException, IOException {
      StringBuilder retVal = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass()
                                                      .getResourceAsStream(filename)));
      for (String line; (line = br.readLine()) != null;) {
         retVal.append(line);
         retVal.append("\n");
      }
      return retVal.toString();
   }   

   private void showMessage(String message) {
      Alert alert = new Alert(AlertType.INFORMATION,message);
      alert.showAndWait();
   } // end showMessage()

   private void output(Object value) {
      String stringValue = String.valueOf(value);
      if (stringValue.equals("")) { 
         return; }
      output.append(stringValue);
      updateOutput();
   }

   private void outputln(Object value) {
      String stringValue = String.valueOf(value);
      if (stringValue.equals("")) { 
         return; }
      output.append(stringValue).append("\n");
      updateOutput();
   }

   private void outputln() {
      output.append("\n");
      updateOutput();
   }

   private void updateOutput() {
      display.setText(output.toString());
      outputCount++;
   } // end updateOutput()

   private void clearOutput() {
      output.setLength(0);
      outputCount = 0;
      display.setText(output.toString());
   }

   private void println() { System.out.println(); }
   private void print(Object value) { System.out.print(value); }
   private void println(Object value) { System.out.println(value); }

   public static void main(String[] args) {
      launch(args);
   } // end main

} // end class