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
        var idUserVale = intent.getStringExtra("idUserVale")
        var cursValues= 0.0
        var valueConvert = 0.0

        val context = this
        val db = DataBaseHandler(context)
        val readValute = db.readValute()


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
                var postion = spinnerChoseMoney.selectedItemPosition
                var getMoney: Double = editTextEnterMoney.getText().toString().toDouble()

                var BAM = readValute[0].BAM
                var RSD = readValute[0].RSD
                var HRK = readValute[0].HRK
                var USD = readValute[0].USD
                var EUR = readValute[0].EUR
                var GBP = readValute[0].GBP
                var CHF = readValute[0].CHF
                var JPY = readValute[0].JPY
                var AUD = readValute[0].AUD
                var CAD = readValute[0].CAD
                var RUB = readValute[0].RUB
                var CHY = readValute[0].CNY
                var dateUpadte= readValute[0].dateCurrency
                var USD1 = 0.0
                when(postion) {
                    0 -> {
                        if (userValute == "BAM"){
                            valueConvert = getMoney
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/BAM)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/BAM
                    }
                    1 -> {//rsd
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            valueConvert = getMoney
                        }
                        if (userValute == "HRK"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/RSD)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/RSD
                    }
                    2 -> {//HRK
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            valueConvert = getMoney
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/HRK)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/HRK

                    }
                    3 -> {//USD??
                        if (userValute == "BAM"){
                            USD1 = getMoney*BAM
                            valueConvert = USD1
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*RSD
                            valueConvert = USD1
                        }
                        if (userValute == "HRK"){
                            USD1 = getMoney*HRK
                            valueConvert = USD1

                        }
                        if (userValute == "USD"){
                            valueConvert = getMoney
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney
                            valueConvert = USD1*CHY
                        }
                        cursValues = USD
                    }
                    4 -> {//EUR
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){

                            valueConvert = getMoney
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/EUR)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/EUR
                    }
                    5 -> {//GBP
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){

                            valueConvert = USD1*getMoney
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/GBP)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/GBP
                    }
                    6 -> {//CHF
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){

                            valueConvert = getMoney
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/CHF)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/CHF
                    }
                    7 -> {//JPY
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){

                            valueConvert = getMoney
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/JPY)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/JPY                }
                    8 -> {//AUD
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){

                            valueConvert = getMoney
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/AUD)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/AUD
                    }
                    9 -> {//CAD
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){

                            valueConvert = getMoney
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/CAD)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/CAD
                    }
                    10 -> {//RUB
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){

                            valueConvert = getMoney
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){
                            USD1 = getMoney*(1/RUB)
                            valueConvert = USD1*CHY
                        }
                        cursValues = 1/RUB
                    }
                    11 -> {//CHY
                        if (userValute == "BAM"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*BAM
                        }
                        if (userValute == "RSD"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*RSD
                        }
                        if (userValute == "HRK"){

                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*HRK
                        }
                        if (userValute == "USD"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1
                        }
                        if (userValute == "EUR"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*EUR
                        }
                        if (userValute == "GBP"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*GBP
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "JPY"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*JPY
                        }
                        if (userValute == "AUD"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*AUD
                        }
                        if (userValute == "CAD"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*CAD
                        }
                        if (userValute == "RUB"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*RUB
                        }
                        if (userValute == "CHF"){
                            USD1 = getMoney*(1/CHY)
                            valueConvert = USD1*CHF
                        }
                        if (userValute == "CHY"){

                            valueConvert = getMoney
                        }
                        cursValues = 1/HRK
                    }
                }
                var userValute = spinnerChoseMoney.selectedItem.toString()
                if (transfer == false) {

                    var categoryUser = spinnerCategoria.selectedItem.toString()
                    profilStatus = spinnerProfil.selectedItem.toString()

                    val dateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString()


                    if (textViewPON.text.toString() == "-") {
                        getMoney = getMoney * (-1)
                        valueConvert = valueConvert * (-1)
                        val userActvities = UserActvities(
                            userId,
                            userValute,
                            getMoney,
                            categoryUser,
                            profilStatus,
                            dateTime,
                            idUserVale,
                            cursValues,
                            valueConvert,
                            dateUpadte
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
                            dateTime,
                            idUserVale,
                            cursValues,
                            valueConvert,
                            dateUpadte
                        )
                        db.insertActions(userActvities)
                        clear()

                    }

                }
                if (transfer == true){





                    profilStatus = spinnerProfil.selectedItem.toString()
                    profilStatus2 = spinnerProfil1.selectedItem.toString()
                    var getMoney: Double = editTextEnterMoney.getText().toString().toDouble()
                    var getMoney1 = getMoney*(-1)
                    var valueConvert1 = valueConvert*(-1)
                    val dateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString()
                    dateUpadte = readValute[0].dateCurrency

                    val userActvitiesUlaz = UserActvities(
                        userId,
                        userValute,
                        getMoney1,
                        "TransferIzlaz",
                        profilStatus,
                        dateTime,
                        idUserVale,
                        cursValues,
                        valueConvert1,
                        dateUpadte
                    )

                    db.insertActions(userActvitiesUlaz)



                    val userActvitiesIzlaz = UserActvities(
                        userId,
                        userValute,
                        getMoney,
                        "TransferUlaz",
                        profilStatus2,
                        dateTime,
                        idUserVale,
                        cursValues,
                        valueConvert,
                        dateUpadte
                    )
                    db.insertActions(userActvitiesIzlaz)

                    clear()
                }
            }



        }


    }



    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val money: String = parent?.getItemAtPosition(position).toString()}

    private fun clear(){

            editTextEnterMoney.text.clear()
    }

    }



