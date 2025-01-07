package adminController;

import exceptions.NoCorrectCardNumberException;
import exceptions.TwelveEncyptionsDone;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import serverFX.DataSingleton;

public class AdminController {


    @FXML
    private Button Crypt;

    @FXML
    private TextField CryptedTxt;

    @FXML
    private TextField DecrypedtTxt;

    @FXML
    private Button Decrypt;

    @FXML
    private Button DisasbleCrypt;

    @FXML
    private Button EnableCrypt;

    @FXML
    private TextField NameOfUser;

    @FXML
    private Button SortByCard;

    @FXML
    private Button SortByCrypt;

    private DataSingleton dataSingelton = DataSingleton.getInstance();

    @FXML
    public void textDecrypt(ActionEvent event) throws NoCorrectCardNumberException, TwelveEncyptionsDone {
        dataSingelton.setName(NameOfUser.getText());
        DecrypedtTxt.setText(dataSingelton.getSystem().getPersonByName(dataSingelton.getName()).getCard().decrypt());
    }

    @FXML
    public void texEncrypt(ActionEvent event) throws NoCorrectCardNumberException, TwelveEncyptionsDone {
        dataSingelton.setName(NameOfUser.getText());
        CryptedTxt.setText(makeString(dataSingelton.getSystem().getPersonByName(dataSingelton.getName()).getCard().encrypt()));
    }
    private String makeString(int[] number){
        String result = new String();
        for (int i = 0; i < number.length; i++) {
            result+=number[i];
            result+="/";
        }
        return result;
    }
    @FXML
    public void enableCrypt(ActionEvent event) {
        dataSingelton.setName(NameOfUser.getText());
        String name = NameOfUser.getText();
        dataSingelton.getSystem().getPersonByName(name).setCanCrypt(true);
        dataSingelton.getSystem().saveToFile();
    }

    @FXML
    public void disableCrypt(ActionEvent event) {
        dataSingelton.setName(NameOfUser.getText());
        String name = NameOfUser.getText();
        dataSingelton.getSystem().getPersonByName(name).setCanCrypt(false);
        dataSingelton.getSystem().saveToFile();
    }

    @FXML
    public void sortByCard(ActionEvent event) {
        dataSingelton.getCollection().printSortedByBankNumbers();
    }

    @FXML
    public void SortByCrypt(ActionEvent event) {
        dataSingelton.getCollection().printSortedByCryptNumbers();
    }

    @FXML
    void getEncryptedNumber(ActionEvent event) {
        dataSingelton.setName(NameOfUser.getText());
        CryptedTxt.setText( makeString(dataSingelton.getCollection().getUser(dataSingelton.getName()).getCard().encryptedNumber()));
    }
    @FXML
    void getDecryptedNumber(ActionEvent event) {
        dataSingelton.setName(NameOfUser.getText());
        DecrypedtTxt.setText( dataSingelton.getCollection().getUser(dataSingelton.getName()).getCard().getCardNumber());

    }
    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
    }
}
