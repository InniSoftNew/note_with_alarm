package com.innisoft.notewithalarm.model

class ModelForAlarm{
    var title: String? = null
    var note: String? = null
    var category: String? = null
    var id: String? = null
    var setAlarm:Boolean=false
    var alarmTime:Long=0

    constructor(title: String?, note: String?, category: String?, id: String?,setAlarm:Boolean,alarmTime:Long) {
        this.title = title
        this.note = note
        this.category = category
        this.id = id
        this.setAlarm=setAlarm
        this.alarmTime=alarmTime
    }
}
