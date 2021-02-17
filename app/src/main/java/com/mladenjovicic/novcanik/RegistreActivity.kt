package com.mladenjovicic.novcanik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_registre.*
import java.sql.DataTruncation
import java.util.*


class RegistreActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)
        var jezici = Locale.getDefault().language
        var error_email_complet: Int =0
        var error_sifra_manje8: Int= 0
        var error_ppopuniti_sva_polja: Int =0
        var registruj_se: Int = 0
        var ime : Int = 0
        var prezime: Int= 0
        var email: Int=0
        var sifra: Int= 0
        var ponovite_sifru= 0
        if(jezici=="bs" || jezici =="hr"|| jezici == "sr"){
        error_email_complet=R.string.srp_greska_Email_nije_kompletan
        error_sifra_manje8 = R.string.srp_greska_sifra_manje8
        error_ppopuniti_sva_polja = R.string.srp_greska_popuniti_sva_polja
        registruj_se = R.string.srp_registruj_se
        ime = R.string.srp_ime
        prezime = R.string.srp_prezime
        email = R.string.srp_email
        sifra = R.string.srp_sifra
        ponovite_sifru= R.string.srp_ponovite_sifru
        }else{
            error_email_complet=R.string.eng_greska_Email_nije_kompletan
            error_sifra_manje8 = R.string.eng_greska_sifra_manje8
            error_ppopuniti_sva_polja = R.string.eng_greska_popuniti_sva_polja
            registruj_se = R.string.eng_registruj_se
            ime = R.string.eng_ime
            prezime= R.string.eng_prezime
            email =  R.string.eng_email
            sifra = R.string.eng_sifra
            ponovite_sifru= R.string.eng_ponovite_sifru
        }
        //spinner za izbor primarnog novca
        var spinnerMoney= findViewById<Spinner>(R.id.spinnerMoney)

        val adapter = ArrayAdapter.createFromResource(this, R.array.valute, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerMoney.adapter = adapter
        spinnerMoney.onItemSelectedListener = this
        //
        var lang= "Ba"
        var money:String
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        val context = this
        val db = DataBaseHandler(context)
        btnRegistreUser.setText(registruj_se)
        editTextNameUser.setHint(ime)
        editTextLastNameUser.setHint(prezime)
        editTextEmailUser.setHint(email)
        editTextPasswordUser.setHint(sifra)
        editTextPasswordAgainUser.setHint(ponovite_sifru)

        btnRegistreUser.setOnClickListener {

            money= spinnerMoney.selectedItem.toString()
            //Provjera da li su sva polja popunjena
            if (editTextEmailUser.text.toString().isNotEmpty() && editTextNameUser.text.toString().isNotEmpty()
                && editTextLastNameUser.text.toString().isNotEmpty()&& editTextPasswordAgainUser.text.toString().isNotEmpty()
                && editTextPasswordUser.text.toString().isNotEmpty()){
                //provjera da li je sifra duza od 8 karaktera
                if(editTextPasswordUser.text.length  >= 8){
                    //provjera da li je unesen ispravan email
                    if(editTextEmailUser.text.matches(emailPattern.toRegex())){
                        //provjera da li se sifra i ponovljena sifra ista
                         if (editTextPasswordUser.text.toString() == editTextPasswordAgainUser.text.toString()){
                             Toast.makeText(this, "Its toast!", Toast.LENGTH_SHORT).show()
                             val user = User(editTextNameUser.text.toString(), editTextLastNameUser.text.toString(),editTextEmailUser.text.toString(),editTextPasswordUser.text.toString(),money,lang)
                             db.insertData(user)
                             clearField()

                             var intent = Intent(this, MainActivity::class.java)
                             intent.putExtra("login", true)
                             startActivity(intent)

                         }
                    } else{
                                  Toast.makeText(this, error_email_complet, Toast.LENGTH_SHORT).show()}
                } else{
                         Toast.makeText(this, error_sifra_manje8, Toast.LENGTH_SHORT).show()}
            }else{
                Toast.makeText(this, error_ppopuniti_sva_polja, Toast.LENGTH_SHORT).show() }
        }
        btnTestA.setOnClickListener {
            val userAct = db.readActions()


            textViewTestA.text = ""
            for (i in 0 until userAct.size) {
                textViewTestA.append(
                    userAct[i].category
                )

        }
            val context1 = this
            val db1 = DataBaseHandler(context1)
            val userPlan = db1.readPlan()
            textViewTestAC.text = "test "
            for (i in 0 until userPlan.size) {
                textViewTestAC.append(


                 "id user" +  userPlan[i].idUser.toString() +" sledeca uplata  "+  userPlan[i].nextPayment +" visina rate "+ userPlan[i].moneyRata.toString() + " kategorija" + userPlan[i].categoryPlan+
                         " money plan" + userPlan[i].moneyPlan + " plan " + userPlan[i].plan  + " "+ userPlan[i].otherMoney +" \n "

                    //userPlan[i].id.toString() + " e" + userPlan[i].email + " " +" p" + userData[i].password + "n " + userData[i].name+ " l" +userData[i].lastName + userData[i].primaryMoney + userData[i].userLang + "\n"
                )
            }
    }

    }
    //spinner za izbor primarnog novca
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val money: String = parent?.getItemAtPosition(position).toString()


    }
    private fun clearField() {
        editTextEmailUser.text.clear()
        editTextNameUser.text.clear()
        editTextLastNameUser.text.clear()
        editTextPasswordAgainUser.text.clear()
        editTextPasswordUser.text.clear()

    }}

