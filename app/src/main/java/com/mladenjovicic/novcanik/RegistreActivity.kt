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


class RegistreActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

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
                                  Toast.makeText(this, "Email nije kompletan", Toast.LENGTH_SHORT).show()}
                } else{
                         Toast.makeText(this, "Sifra mora imati 8 karaktera", Toast.LENGTH_SHORT).show()}
            }else{
                Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show() }
        }
        btnTestA.setOnClickListener {
            val userAct = db.readActions()
            textViewTestA.visibility = View.VISIBLE

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    }
}
