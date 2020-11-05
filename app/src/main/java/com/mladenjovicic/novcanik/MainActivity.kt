package com.mladenjovicic.novcanik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registre.*
import kotlin.concurrent.thread

var login = false
var autoLogin = false
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val context = this

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.with(this).load(R.drawable.loding).into(imageViewLoding)

        btnRegisterAct.setOnClickListener {
            val intent = Intent(this, RegistreActivity::class.java )
            startActivity(intent)
        }

        if(login == true){
            val intent = Intent(this, RegistreActivity::class.java )
            startActivity(intent)
        }else{

        }

        imageViewLoding.visibility = View.GONE
        text_home.visibility = View.GONE
        loginBox.visibility = View.VISIBLE


            val db = DataBaseHandler(context)
            btnLoginUser.setOnClickListener {
                //Provjera da li je editText za email i šifru prazni
                if(editTextUserEmailLogin.text.toString().isNotEmpty()&& editTextUserPasswordLogin.text.toString().isNotEmpty()){
                    //provjera podataka u bazi podataka
                    val userData = db.readData()
                    for (i in 0 until userData.size) {
                    var userId = userData[i].id.toInt()
                    var userEmail = userData[i].email.toString()
                    var userPassword = userData[i].password.toString()
                    var userName = userData[i].name.toString()
                    var userLastname = userData[i].lastName.toString()
                    var userValute=userData[i].primaryMoney.toString()
                    var userLang = userData[i].userLang.toString()
                    if(userEmail == editTextUserEmailLogin.text.toString()&& userPassword == editTextUserPasswordLogin.text.toString()){
                        val intent = Intent(this, HomeActivity::class.java )
                        intent.putExtra("idUser", userId)
                        intent.putExtra("emailUser", userEmail)
                        intent.putExtra("nameUser", userName)
                        intent.putExtra("lastnameUser", userLastname)
                        intent.putExtra("userValute", userValute)
                        intent.putExtra("userLang", userLang)
                        startActivity(intent)
                        break

                    }else{
                        Toast.makeText(context, "Unesene podaci ne odgovaraju nijednom nalogu", Toast.LENGTH_SHORT).show()
                    }


                }

                if (checkBoxSave.isChecked){
                    autoLogin = true

                }else{
                    autoLogin = false
                }
            }else{
                    Toast.makeText(this, "Niste unijeli podatke ", Toast.LENGTH_SHORT).show()
                }

        }


    }
}
