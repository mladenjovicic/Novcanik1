package com.mladenjovicic.novcanik

class profil{
    var id:Int?= null
    var novac:String?= null
    var kategorija:String?= null
    var datum:String?= null

    constructor(id:Int, novac:String, kategorija:String, datum:String){
        this.id = id
        this.novac = novac
        this.kategorija = kategorija
        this.datum = datum
    }

}