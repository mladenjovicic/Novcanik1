package com.mladenjovicic.novcanik

class userActionFirebase ( var id:Int = 0, var idUser:String = "", var idValuta:String = "", var money:Double = 0.0, var category:String = "", var profil:String = "", var timeDate:String= "", var idUserPrimarValut:String="",
                          var cursValut:Double = 0.0, var valueConvert:Double = 0.0, var dateCurrencyConvert:String="", var idUid:String = "" ){

        constructor():this( 0,"", "",0.0,"","","","",0.0,0.0,"", "")
    }