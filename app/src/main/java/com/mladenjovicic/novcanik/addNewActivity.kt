package com.mladenjovicic.novcanik

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class addNewActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new)
        var profilStatus:String
        var profilStatus2:String
        var transfer =  false
        var colorTest = "colorLightGreen"

        val context = this
        val db = DataBaseHandler(context)


        var userId = intent.getIntExtra("idUser", 0)
        var userValute = intent.getStringExtra("userValute")



        //spinner
        var spinnerProfil1 = findViewById<Spinner>(R.id.spinnerCategoria)
        val adapterProfil1 = ArrayAdapter.createFromResource(this, R.array.profil, R.layout.spinner_item)
        adapterProfil1.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerProfil1.adapter = adapterProfil1
        spinnerProfil1.onItemSelectedListener = this


        var spinnerChoseMoney =findViewById<Spinner>(R.id.spinnerChoseMoney)
        val adapter = ArrayAdapter.createFromResource(this, R.array.valute, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerChoseMoney.adapter = adapter
        spinnerChoseMoney.onItemSelectedListener = this
        var spinnerPositon = adapter.getPosition(userValute)
        spinnerChoseMoney.setSelection(spinnerPositon)

        var spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val adapterCategoria = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner_item)
        adapterCategoria.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerCategoria.adapter = adapterCategoria
        spinnerCategoria.onItemSelectedListener = this

        var spinnerProfil = findViewById<Spinner>(R.id.spinnerProfil)
        val adapterProfil = ArrayAdapter.createFromResource(this, R.array.profil, R.layout.spinner_item)
        adapterProfil.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerProfil.adapter = adapterProfil
        spinnerProfil.onItemSelectedListener = this








        btnIncome.setOnClickListener {
            transfer = false
            textViewPON.text="+"
            textViewProfil.text= "   Račun: "
            textViewCategoria.text = "   Kategorija: "
            btnIncome.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnExpense.setBackgroundColor(getColor(R.color.colorDarkGreen))
            btnTransfer.setBackgroundColor(getColor(R.color.colorDarkGreen))

            var spinnerProfil = findViewById<Spinner>(R.id.spinnerProfil)
            val adapterProfil = ArrayAdapter.createFromResource(this, R.array.profil, R.layout.spinner_item)
            adapterProfil.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerProfil.adapter = adapterProfil
            spinnerProfil.onItemSelectedListener = this

            var spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
            val adapterCategoria = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner_item)
            adapterCategoria.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerCategoria.adapter = adapterCategoria
            spinnerCategoria.onItemSelectedListener = this
            spinnerCategoria.visibility = View.VISIBLE

        }
        btnExpense.setOnClickListener {
            transfer = false
            textViewPON.text="-"
            textViewProfil.text= "   Račun: "
            textViewCategoria.text = "   Kategorija: "
            btnIncome.setBackgroundColor(getColor(R.color.colorDarkGreen))
            btnExpense.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnTransfer.setBackgroundColor(getColor(R.color.colorDarkGreen))

            var spinnerProfil = findViewById<Spinner>(R.id.spinnerProfil)
            val adapterProfil = ArrayAdapter.createFromResource(this, R.array.profil, R.layout.spinner_item)
            adapterProfil.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerProfil.adapter = adapterProfil
            spinnerProfil.onItemSelectedListener = this

            var spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
            val adapterCategoria = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner_item)
            adapterCategoria.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerCategoria.adapter = adapterCategoria
            spinnerCategoria.onItemSelectedListener = this
        }
        btnTransfer.setOnClickListener {
            transfer = true
            textViewPON.text=" "
            textViewProfil.text= "   Iz računa: "
            textViewCategoria.text = "   U račun: "
            btnIncome.setBackgroundColor(getColor(R.color.colorDarkGreen))
            btnExpense.setBackgroundColor(getColor(R.color.colorDarkGreen))
            btnTransfer.setBackgroundColor(getColor(R.color.colorLightGreen))
            spinnerCategoria.visibility = View.VISIBLE

            var spinnerProfil = findViewById<Spinner>(R.id.spinnerProfil)
            val adapterProfil = ArrayAdapter.createFromResource(this, R.array.profil, R.layout.spinner_item)
            adapterProfil.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerProfil.adapter = adapterProfil
            spinnerProfil.onItemSelectedListener = this

            var spinnerProfil1 = findViewById<Spinner>(R.id.spinnerCategoria)
            val adapterProfil1 = ArrayAdapter.createFromResource(this, R.array.profil, R.layout.spinner_item)
            adapterProfil1.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerProfil1.adapter = adapterProfil1
            spinnerProfil1.onItemSelectedListener = this
        }









        //dodavanje u bazu
        btnAcceptActivity.setOnClickListener {
            if (editTextEnterMoney.text.isNotEmpty()) {

                if (transfer == false) {
                    var userValute = spinnerChoseMoney.selectedItem.toString()
                    var categoryUser = spinnerCategoria.selectedItem.toString()
                    profilStatus = spinnerProfil.selectedItem.toString()
                    var getMoney: Double = editTextEnterMoney.getText().toString().toDouble()
                    val dateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString()

                    if (textViewPON.text.toString() == "-") {
                        getMoney = getMoney * (-1)
                        val userActvities = UserActvities(
                            userId,
                            userValute,
                            getMoney,
                            categoryUser,
                            profilStatus,
                            dateTime
                        )
                        db.insertActions(userActvities)
                        clear()
                    } else {
                        val userActvities = UserActvities(
                            userId,
                            userValute,
                            getMoney,
                            categoryUser,
                            profilStatus,
                            dateTime
                        )
                        db.insertActions(userActvities)
                        clear()

                    }

                }
            }
            if (transfer == true){





                profilStatus = spinnerProfil.selectedItem.toString()
                profilStatus2 = spinnerProfil1.selectedItem.toString()
                var getMoney: Double = editTextEnterMoney.getText().toString().toDouble()
                var getMoney1 = getMoney*(-1)
                val dateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString()

                val userActvitiesUlaz = UserActvities(
                    userId,
                    userValute,
                    getMoney1,
                    "TransferIzlaz",
                    profilStatus,
                    dateTime
                )

                db.insertActions(userActvitiesUlaz)



                val userActvitiesIzlaz = UserActvities(
                    userId,
                    userValute,
                    getMoney,
                    "TransferUlaz",
                    profilStatus2,
                    dateTime
                )
                db.insertActions(userActvitiesIzlaz)

                clear()
            }


        }


    }



    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val money: String = parent?.getItemAtPosition(position).toString()}

    private fun clear(){

            editTextEnterMoney.text.clear()
    }

    }



