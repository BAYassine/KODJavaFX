/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Services.CommandeService;
import Services.PaymentOrder;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class PaymentController {

    @FXML
    private TextField name;
    @FXML
    private TextField cc;
    @FXML
    private TextField exp_m;
    @FXML
    private TextField exp_y;
    @FXML
    private TextField cvv;
    @FXML
    private Button pay;

    @FXML
    private void paynow(ActionEvent event) {
        System.out.println("paynow");
        CommandeService cs = new CommandeService();
        float total = cs.getTotal(Main.user.getId());
        PaymentOrder payment = new PaymentOrder(cc.getText(), cvv.getText(), exp_m.getText(), exp_y.getText(), (double) total, "abir.lahmar@€sprit.tn");
        try {
            Charge charge = payment.createCharge("sk_test_pqwVr0qaXLRcMry0G9xsJWvN", payment.getAmmount(), payment.getName(), payment.getCardnumber(), payment.getExp_month(), payment.getExp_year(), payment.getCvv(), "abir.lahmer@esprit.tn");
            System.out.println("charge : " + charge.getStatus());
            if (charge.getStatus().equalsIgnoreCase("succeeded")) {


                System.out.println("succeed");
            } else {
                System.out.println(charge.getDescription());
            }
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {

            if (ex.getMessage() != null || !(ex.getMessage().equals(""))) {

            }

        }

    }

}
