package JavaFX;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainPackage.AdminMain;
import mainPackage.Main;

public class AdminUserController implements Initializable {

	private List<Document> allUsers;
	private static User EditUser;

	@FXML
	TextField nameBoks;
	@FXML
	TextField statusBoks;
	@FXML
	TextField pathBoks;

	@FXML
	TextField searchBoks;

	@FXML
	TableView<User> tableId;
	@FXML
	TableColumn<User, String> idCol;
	@FXML
	TableColumn<User, String> firstNameCol;
	@FXML
	TableColumn<User, String> surNameCol;
	@FXML
	TableColumn<User, String> emailCol;
	@FXML
	TableColumn<User, String> phoneCol;
	@FXML
	TableColumn<User, String> birthdayCol;
	@FXML
	TableColumn<User, String> ratingCol;
	@FXML
	TableColumn<User, String> regCol;

	@FXML
	TextField firstNameBoks;
	@FXML
	TextField surNameBoks;
	@FXML
	TextField ratingBoks;
	@FXML
	TextField emailBoks;
	@FXML
	TextField phoneBoks;
	@FXML
	DatePicker birthdayBoks;
	@FXML
	TextField memberSinceBoks;

	public AdminUserController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			idCol.setCellValueFactory(p -> p.getValue().idProperty());
			firstNameCol.setCellValueFactory(p -> p.getValue().firstnameProperty());
			surNameCol.setCellValueFactory(p -> p.getValue().surnameProperty());
			emailCol.setCellValueFactory(p -> p.getValue().emailProperty());
			phoneCol.setCellValueFactory(p -> p.getValue().phoneProperty());
			birthdayCol.setCellValueFactory(p -> p.getValue().birthdayProperty());
			ratingCol.setCellValueFactory(p -> p.getValue().ratingProperty());
			regCol.setCellValueFactory(p -> p.getValue().timeProperty());

			try {
				tableId.getItems().setAll(getAllUsers());

			} catch (Exception e) {
				System.out.println("Feil her");
			}
			tableId.setEditable(true);
			tableId.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

			nameBoks.setText("Hei Admin!");
			pathBoks.setText("../Admin/Users");
			statusBoks.setText("Alt fungerer som vanlig..");

		} catch (Exception e) {
			System.out.println("Klarte ikke laste inn..");
			System.out.println(
					"Er du sikker på at alle brukerne i databasen oppfyller kravene som stilles i AdminUserController?");
		}

	}

	@FXML
	private void searchUser(ActionEvent event) {

	}

	@FXML
	private void deleteUser(ActionEvent event) {
		User user = tableId.getSelectionModel().getSelectedItem();

		try {
			ObjectId userId = new ObjectId(user.getId());
			AdminMain.deleteUser(userId);
			tableId.getItems().remove(user);

		} catch (Exception e) {
			System.out.println("Klarte ikke slette bruker!!");

		}

	}

	@FXML
	private void removeSelected(ActionEvent event) {
		tableId.getSelectionModel().clearSelection();
	}

	@FXML
	private void editUser(ActionEvent event) throws IOException, ParseException {
		MongoCollection<Document> foo = AdminMain.getDatabase().getCollection("Users");

		User user = tableId.getSelectionModel().getSelectedItem();
		if (user == null)
			statusBoks.setText("Du må velge en bruker...");
		else {
			System.out.println(tableId.getSelectionModel().getSelectedItem());
			EditUser = user;

			Parent fo = FXMLLoader.load(getClass().getResource("NewAdminEditUser.fxml"));
			Scene scene = new Scene(fo);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(scene);

			window.show();

			TextField fn = (TextField) scene.lookup("#firstNameBoks");
			fn.setText(EditUser.getFirstName());

			TextField sn = (TextField) scene.lookup("#surNameBoks");
			sn.setText(EditUser.getSurName());

			TextField mail = (TextField) scene.lookup("#emailBoks");
			mail.setText(EditUser.getEmail());

			System.out.println(EditUser.getEmail());

			TextField phone = (TextField) scene.lookup("#phoneBoks");
			phone.setText(EditUser.getPhone());

			TextField rating = (TextField) scene.lookup("#ratingBoks");
			rating.setText(EditUser.getRating());

			TextField member = (TextField) scene.lookup("#memberSinceBoks");
			member.setText(EditUser.getTimeJoined());

			DatePicker birthday = (DatePicker) scene.lookup("#birthdayBoks");
			LocalDate date = AdminMain.dateToLocalDate(AdminMain.stringToDate(EditUser.getBirthday()));
			birthday.setValue(date);

			TextField status = (TextField) scene.lookup("#statusBoks");
			status.setText("Alt fungerer som normalt..");

			TextField path = (TextField) scene.lookup("#pathBoks");
			path.setText("../Admin/Users/Edit");

			TextField name = (TextField) scene.lookup("#nameBoks");
			name.setText("Hei Admin!");

		}

	}

	private ObservableList<User> getAllUsers() {
		ObservableList<User> users = FXCollections.observableArrayList();

		List<Document> foo = AdminMain.getAllUsers();
		allUsers = foo;

		for (Document doc : foo) {
			User k = new User(doc.get("_id").toString(), doc.get("firstname").toString(),
					doc.get("lastname").toString(), doc.get("email").toString(), doc.get("phonenumber").toString(),
					AdminMain.dateToString((Date) doc.get("birthdate")), doc.get("rating").toString(),
					doc.get("time_joined").toString());
			users.add(k);
		}
		return users;
	}

	public String getError() {
		String good = "Alt fungerer normalt...";
		return good;

	}

	@FXML
	private void onUpdateUser(ActionEvent event) throws IOException, ParseException {

		Document user = AdminMain.getUser(new ObjectId(EditUser.getId()));

		if (!firstNameBoks.getText().isEmpty() && !firstNameBoks.getText().toString().matches(".*\\d+.*")) {
			user.replace("firstname", firstNameBoks.getText().toString());

		} else {
			statusBoks.setText("Ugyldig fornavn");
			return;
		}

		if (!surNameBoks.getText().isEmpty() && !surNameBoks.getText().toString().matches(".*\\d+.*")) {
			user.replace("lastname", surNameBoks.getText().toString());
		} else {
			statusBoks.setText("Ugyldig etternavn");
			return;
		}

		if (!emailBoks.getText().isEmpty() && !emailBoks.getText().toString().matches(".*\\d+.*")
				&& emailBoks.getText().toString().contains("@")) {
			user.replace("email", emailBoks.getText().toString());
		} else {
			statusBoks.setText("Ugyldig email");
			return;
		}

		if ((phoneBoks.getText().length() == 8) && phoneBoks.getText().matches(".*\\d+.*")) {
			user.replace("phonenumber", phoneBoks.getText().toString());
		} else {
			statusBoks.setText("Ugyldig telefonnummer");
			return;
		}

		if ((ratingBoks.getText().toString().matches(".*\\d+.*"))) {
			user.replace("rating", Integer.parseInt(ratingBoks.getText().toString()));
		} else {
			statusBoks.setText("Ugyldig rating..");
			return;
		}

		if (!(birthdayBoks.getValue().toString().isEmpty())) {
			try {
				Date dato = AdminMain.localdateToDate(birthdayBoks.getValue());
				user.replace("birthdate", dato);
			} catch (Exception e) {
				System.out.println("Du må fikse på bursdagen");
			}
		} else {
			statusBoks.setText("Ugyldig bursdag");
			return;
		}
		try {
			AdminMain.updateUser(user);
			statusBoks.setText("Oppdateringen av brukeren var vellykket!");
			toAdminUser(event);

		} catch (Exception e) {
			statusBoks.setText("Det skjedde noe galt ved oppdatering av bruker..");
			System.out.println(e.getCause() + "" + e.getClass());
		}

	}

	@FXML
	private void onAbortUpdateUser(ActionEvent event) throws IOException {
		toAdminUser(event);
		/*
		 * Parent menyParent =
		 * FXMLLoader.load(getClass().getResource("NewAdminUser.fxml")); Scene menyScene
		 * = new Scene(menyParent);
		 * 
		 * //This line gets the Stage information Stage window =
		 * (Stage)((Node)event.getSource()).getScene().getWindow();
		 * window.setScene(menyScene); window.show();
		 */
	}

	@FXML
	private void toAdminUser(ActionEvent event) throws IOException {
		Parent menyParent = FXMLLoader.load(getClass().getResource("NewAdminUsers.fxml"));
		Scene menyScene = new Scene(menyParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(menyScene);
		window.show();
	}

	@FXML
	private void toLogin(ActionEvent event) throws IOException {
		Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
		Scene menyScene = new Scene(menyParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(menyScene);
		window.show();
	}

	@FXML
	private void toAdminCommunity(ActionEvent event) throws IOException {
		Parent menyParent = FXMLLoader.load(getClass().getResource("AdminCommunity.fxml"));
		Scene menyScene = new Scene(menyParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(menyScene);
		window.show();
	}

	@FXML
	private void toAdminHistory(ActionEvent event) throws IOException {
		Parent menyParent = FXMLLoader.load(getClass().getResource("NewAdminHistory.fxml"));
		Scene menyScene = new Scene(menyParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(menyScene);
		window.show();
	}

}
