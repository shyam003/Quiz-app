package com.example.loginfirebase

class Question {
    var questionF: String? = null
    var option1: String? = null
    var option2: String? = null
    var option3: String? = null
    var option4: String? = null
    var answer: String? = null
    var index: Int = 0

    constructor() {}
    constructor(questionF: String?, option1: String?, option2: String?, option3: String?, option4: String?, answer: String?, index: Int) {
        this.questionF = questionF
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.option4 = option4
        this.answer = answer
        this.index= index
    }
}