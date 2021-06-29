/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.PlayerWithWeight;
import it.polito.tdp.PremierLeague.model.TopPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.txtGoals.getText().equals("")) {
    		this.txtResult.appendText("Selezionare un numero minimo di goal\n");
    		return;
    	}
    	double minGoal = 0.0;
    	try {
    		minGoal = Double.parseDouble(this.txtGoals.getText());
    	}catch(NumberFormatException e ) {
    		this.txtResult.appendText("Il numero minimo di goal deve essere un decimale positivo\n");
    		return;
    	}
    	if(minGoal<0.0) {
    		this.txtResult.appendText("Il numero minimo di goal deve essere un decimale positivo\n");
    		return;
    	}
    	this.model.creaGrafo(minGoal);
    	this.grafoCreato = true;
    	this.txtResult.appendText("Grafo creato\n# vertici: "+this.model.getNumVertici()+"\n# archi: "+this.model.GetNumArchi()+"\n");
    	
    	

    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	this.txtResult.clear();
    	if(!this.grafoCreato) {
    		this.txtResult.appendText("Creare il grafo\n");
    		return;
    	}
    	if(this.txtK.getText().equals("")) {
    		this.txtResult.appendText("Selezionare un numero di giocatori K\n");
    		return;
    	}
    	int k = 0;
    	try {
    		k = Integer.parseInt(this.txtK.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("K deve essere un intero positivo\n");
    		return;
    	}
    	this.txtResult.appendText(this.model.doDreamTeam(k));
    	
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	this.txtResult.clear();
    	if(!this.grafoCreato) {
    		this.txtResult.appendText("Creare il grafo\n");
    		return;
    	}
    	TopPlayer tp = this.model.doTopPlayer();
    	
    	this.txtResult.appendText("Trovato il top player: "+tp.getP()+"\n");
    	this.txtResult.appendText("Sono stati battuti i seguenti giocatori:\n");
    	for(PlayerWithWeight pww : tp.getBattuti()) {
    		this.txtResult.appendText(pww.getP()+" "+pww.getPeso()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
