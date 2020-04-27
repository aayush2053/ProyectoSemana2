package application;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SampleController {
	
	@FXML
	private Label alerta3;
	
	@FXML
	private Label alerta2;
	
	@FXML
	private Label alerta1;
	
	@FXML
	private TextField txtCif;
	
	@FXML
	private TextField txtRaz;
	
	@FXML
	private TextField txtReg;
	
	@FXML
	private TextField txtRespons;
	
	@FXML
	private TextField txtSegImp;
	
	@FXML
	private TextField txtFecha;
	
	@FXML
	private TextField txtCif1;
	
	@FXML
	private TextField txtRaz1;
	
	@FXML
	private TextField txtReg1;
	
	@FXML
	private TextField txtRespons1;
	
	@FXML
	private TextField txtSegImp1;
	
	@FXML
	private TextField txtFecha1;
	
	@FXML
	private ListView<String> lvProveedores;
	
	private ArrayList<Proveedor>proveedores;
	
	public static final int MYSQL_DUPLICATE_PK = 1062;

	
	@FXML
	public void subirArchivo() {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("UPLOAD FILE");

        File file = fileChooser.showOpenDialog((Stage) lvProveedores.getScene().getWindow());
        
        if(file != null) 
	        System.out.println(file.getAbsolutePath());

	}
	
	@FXML
	public void cargarProveedores() {
		proveedores = new ArrayList<>();
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/info", "root", null);
			
			
			Statement sentencia = conexion.createStatement();
			ResultSet cursor = sentencia.executeQuery("SELECT * FROM prov_comp");
			
			while (cursor.next()) {

				Proveedor proveedor = new Proveedor(cursor.getString(1),cursor.getString(2),cursor.getInt(3), cursor.getString(4), cursor.getFloat(5),cursor.getDate(6));
			
			    proveedores.add(proveedor);
			}
			
			cursor.close();     // Cerrar ResultSet
			sentencia.close(); // Cerrar Statement
			conexion.close();  // Cerrar conexión
			
		}
		catch (ClassNotFoundException cn) {cn.printStackTrace();} 
		catch (SQLException e) {e.printStackTrace();}
		
		ObservableList<String> items =FXCollections.observableArrayList ();
		
		for(Proveedor prov : proveedores)
			items.add("CIF: " + prov.getCif() + " : " + prov.getRazSoc());
		
		lvProveedores.setItems(items);
		
		lvProveedores.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	        	if(lvProveedores.getSelectionModel().getSelectedIndex()>=0)
	        		mostrarInfo(proveedores.get(lvProveedores.getSelectionModel().getSelectedIndex()));
	        }
	    });
	}
	
	public void mostrarInfo(Proveedor prov) {
		txtCif.setText(prov.getCif());
		txtCif.setDisable(true);
		txtRaz.setText(prov.getRazSoc());
		txtReg.setText(String.valueOf(prov.getRegNot()));
		txtRespons.setText(prov.getSegRespon());
		txtSegImp.setText(String.valueOf(prov.getSegImpo()));
		txtFecha.setText(prov.getFechHomo().toLocalDate().toString());

	}
	
	@FXML
	public void limpiar() {
		txtCif.setDisable(false);
		txtCif.setText("");
		txtRaz.setText("");
		txtReg.setText("");
		txtRespons.setText("");
		txtSegImp.setText("");
		txtFecha.setText("");
		alerta2.setVisible(false);
		alerta3.setVisible(false);
		alerta1.setVisible(false);
	}
	
	
	public void añadir() {
		
		
		
		String cif = txtCif.getText().toString();
		String razSoc = txtRaz.getText().toString();
		int regNot = Integer.parseInt(txtReg.getText().toString());
		String segRespon = txtRespons.getText().toString();
		float segImpo = Float.parseFloat(txtSegImp.getText().toString());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
	
		//convert String to LocalDate
		LocalDate localDate = LocalDate.parse(txtFecha.getText(), formatter);
		
		Date date = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());

		Proveedor prov = new Proveedor(cif,razSoc,regNot,segRespon,segImpo,sqlDate);
		
		Connection conexion = null;
	    PreparedStatement ps;
		try {
			conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/info","root", null);
			ps = conexion.prepareStatement("INSERT INTO prov_comp VALUES(?,?,?,?,?,?)");
			
			ps.setString(1, prov.getCif());
			ps.setString(2, prov.getRazSoc());
			ps.setInt(3, prov.getRegNot());
			ps.setString(4,prov.getSegRespon());
			ps.setFloat(5,prov.getSegImpo());
			ps.setDate(6, prov.getFechHomo());
			 
			ps.executeUpdate();
			
			conexion.close();
			ps.close();
		} catch (SQLException e) {
			 if(e.getErrorCode() == MYSQL_DUPLICATE_PK ){
			        //duplicate primary key 
			    }
		}
		cargarProveedores();
		limpiar();
		alerta2.setVisible(true);
		alerta3.setVisible(false);
		alerta1.setVisible(false);
	}
	
	

	
	
	@FXML
	public void eliminar() {
		
		if(lvProveedores.getSelectionModel().getSelectedIndex()>=0) {
			Proveedor prov = proveedores.get(lvProveedores.getSelectionModel().getSelectedIndex());
			
			Connection conexion = null;
			try {
				
				Class.forName("com.mysql.jdbc.Driver");
				conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/info", "root", null);
									
				PreparedStatement ps = conexion.prepareStatement("DELETE FROM prov_comp WHERE CIF_PROVEEDOR = ? ;");
		
			    ps.setString(1, prov.getCif());
			    ps.executeUpdate();
				
				conexion.close(); 
				ps.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			limpiar();
			cargarProveedores();
			alerta1.setVisible(false);
			alerta2.setVisible(false);
			alerta3.setVisible(true);
			
		}
	}
	
	
	@FXML
	public void modificar() {
		
		if(lvProveedores.getSelectionModel().getSelectedIndex()>=0) {
		
			String cif = txtCif.getText().toString();
			String razSoc = txtRaz.getText().toString();
			int regNot = Integer.parseInt(txtReg.getText().toString());
			String segRespon = txtRespons.getText().toString();
			float segImpo = Float.parseFloat(txtSegImp.getText().toString());
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		
			//convert String to LocalDate
			LocalDate localDate = LocalDate.parse(txtFecha.getText(), formatter);
			
			Date date = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	
			Proveedor prov = new Proveedor(cif,razSoc,regNot,segRespon,segImpo,sqlDate);
			
			Connection conexion = null;
		    PreparedStatement ps;
			try {
				conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/info", "root", null);
				ps = conexion.prepareStatement("UPDATE prov_comp SET RAZ_PROVEEDOR = ?, REG_NOTARIAL = ?, SEG_RESPONSABILIDAD = ?, SEG_IMPORTE = ?, FEC_HOMOLOGACION = ? WHERE CIF_PROVEEDOR = ? ;");
				
				
				ps.setString(1, prov.getRazSoc());
				ps.setInt(2, prov.getRegNot());
				ps.setString(3,prov.getSegRespon());
				ps.setFloat(4,prov.getSegImpo());
				ps.setDate(5, prov.getFechHomo());
				ps.setString(6, prov.getCif());
				 
				
				ps.executeUpdate();
				
				conexion.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			cargarProveedores();
			limpiar();
			alerta1.setVisible(true);
			alerta2.setVisible(false);
			alerta3.setVisible(false);
		}
	}
	
	
}
