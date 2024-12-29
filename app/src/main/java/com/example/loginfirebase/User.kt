package com.example.loginfirebase

class User {
    var name: String? = null
    var phone: String? = null
    var email: String? = null
    var pass: String? = null
    var score: String? = null
    var scr: Int?=0

    constructor() {}
    constructor(name: String?, phone: String?, email: String?, pass: String?,score: String?,scr: Int?) {
        this.name = name
        this.phone = phone
        this.email = email
        this.pass = pass
        this.score = score
        this.scr=scr
    }
}